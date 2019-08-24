package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import engines.NetworkEventInferenceEngine;
import events.EventStreams;
import graphs.atRiskStreams.AtRiskStatuses;
import graphs.processors.GitHubEventStreamProcessors;
import graphs.relationalStreams.IndexedRelationalEventStreams;

import samplers.Sampler;
import samplers.commits.CommitEventSampler;
import statistics.builders.StatisticBuilder;

public class ConfigurableExperiment {

	private static final Logger LOG = LoggerFactory
			.getLogger(CommitEventSampler.class.getName());

	public static NetworkEventInferenceEngine engine;

	public static double startingObservationTime;

	public static double endingObservationTime;

	public static EventStreams eventStreams = EventStreams.getInstance();

	public static AtRiskStatuses atRiskStatuses = AtRiskStatuses.getInstance();

	public static IndexedRelationalEventStreams indexedRelationalEventStreams = IndexedRelationalEventStreams
			.getInstance();

	public static GitHubEventStreamProcessors eventStreamProcessor = GitHubEventStreamProcessors
			.getInstance();

	public static StatisticBuilder statisticBuilder = StatisticBuilder
			.getInstance();

	public static int probingEventStep;

	public static boolean standardizingStatistics;

	public static boolean removingScript;

	public static boolean removingData;

	public static long randomSeed;

	public static int controlSamplingSize;

	public static double eventSamplingRate;

	public static String outputDirectory;

	public static double exponentialWeight;

	public static HashMap<String, Double> commonStatisticParameterMap = new HashMap<String, Double>();

	public static DecimalFormat timestampFormatter = new DecimalFormat(".00000");

	public static void main(String[] args) throws Exception {

		String xmlConfigurationFile = args[0].trim();
		XMLConfiguration config = null;

		try {
			config = new XMLConfiguration(xmlConfigurationFile);
		} catch (ConfigurationException cex) {
			LOG.error("Failed to read the XML configuration file: {}",
					xmlConfigurationFile);
			cex.printStackTrace();
			System.exit(-1);
		}

		createNetworkEventInferenceEngine(config);

		createEventSamplers(args, config);

		LOG.info("Start to play event streams and generate case-control sampling data sets");

		engine.runEventStreams();

		LOG.info("Finish to play event streams and generate case-control sampling data sets");

		LOG.info("Start to run scripts and estimate network event models ");

		engine.estimateNetworkEventModels(false);

		LOG.info("Finish to run scripts and estimate network event models ");

	}

	protected static void createNetworkEventInferenceEngine(
			XMLConfiguration config) throws IOException {
		if (config.getString("observationTime.start") != null) {
			startingObservationTime = config.getDouble("observationTime.start");
			LOG.info("The starting observation time is {}",
					startingObservationTime);
		} else {
			startingObservationTime = 0.0;
			LOG.info("By default, the starting observation time is ",
					startingObservationTime);
		}

		if (config.getString("observationTime.end") != null) {
			endingObservationTime = config.getDouble("observationTime.end");
			LOG.info("The ending observation time is {}", endingObservationTime);
		} else {
			endingObservationTime = Double.MAX_VALUE;
			LOG.info("By default, the ending observation time is ",
					endingObservationTime);
		}

		String mappedEventStreamsFile = config.getString("eventFile");
		if (mappedEventStreamsFile != null) {
			LOG.info("Process the file for mapped event streams: {}",
					mappedEventStreamsFile);
			eventStreams.readEventStreams(mappedEventStreamsFile);
		} else {
			LOG.error("There is no file for mapped event streams");
			System.exit(-1);
		}

		String mappedAtRiskEventStreamProcessorsFile = config
				.getString("atRiskEventStreamProcessorsFile");
		if (mappedAtRiskEventStreamProcessorsFile != null) {
			LOG.info(
					"Process the file for mapped at risk event stream processors: {}",
					mappedAtRiskEventStreamProcessorsFile);
			eventStreamProcessor
					.loadAtRiskEventStreamProcessors(mappedAtRiskEventStreamProcessorsFile);
		} else {
			LOG.info("There is no file for mapped at risk event stream processors");
			System.exit(-1);
		}

		String mappedRelationalEventStreamProcessorsFile = config
				.getString("relationalEventStreamProcessorsFile");
		if (mappedRelationalEventStreamProcessorsFile != null) {
			LOG.info(
					"Process the file for mapped relational event stream processors: {}",
					mappedRelationalEventStreamProcessorsFile);
			eventStreamProcessor
					.loadRelationalEventStreamProcessors(mappedRelationalEventStreamProcessorsFile);
		} else {
			LOG.info("There is no file for mapped relational event stream processors");
			System.exit(-1);
		}

		String mappedAtRiskStatusesFile = config
				.getString("atRiskStatusesFile");
		if (mappedAtRiskStatusesFile != null) {
			LOG.info("Process the file for mapped at risk statuses: {}",
					mappedAtRiskStatusesFile);
			atRiskStatuses.loadAtRiskStatuses(mappedAtRiskStatusesFile);
		} else {
			LOG.info("There is no file for mapped at risk statuses");
			System.exit(-1);
		}

		String mappedIndexedRelationalEventStreamsFile = config
				.getString("indexedRelationalEventStreamsFile");
		if (mappedIndexedRelationalEventStreamsFile != null) {
			LOG.info(
					"Process the file for mapped indexed relational event streams: {}",
					mappedIndexedRelationalEventStreamsFile);
			indexedRelationalEventStreams
					.loadIndexedRelationalEventStreams(mappedIndexedRelationalEventStreamsFile);
		} else {
			LOG.info("There is no file for mapped indexed relational event streams");
			System.exit(-1);
		}

		engine = new NetworkEventInferenceEngine(startingObservationTime,
				endingObservationTime, eventStreams, eventStreamProcessor);

		if (config.getString("probingEventStep") != null)
			probingEventStep = config.getInt("probingEventStep");
		else
			probingEventStep = 1000;
		engine.setProbingEventStep(probingEventStep);
		LOG.info("Probing Event Step = {}", probingEventStep);

		if (config.getDouble("standardizingStatistics") != 0)
			standardizingStatistics = true;
		else
			standardizingStatistics = false;
		engine.setStandardizingStatistics(standardizingStatistics);
		LOG.info("Standardizing Statistics = {}", standardizingStatistics);

		if (config.getDouble("removingScript") != 0)
			removingScript = true;
		else
			removingScript = false;
		engine.setRemovingScript(removingScript);
		LOG.info("Removing Script = {}", removingScript);

		if (config.getDouble("removingData") != 0)
			removingData = true;
		else
			removingData = false;
		engine.setRemovingData(removingData);
		LOG.info("Removing Data = {}", removingData);
	}

	protected static void createEventSamplers(String[] args,
			XMLConfiguration config) throws NumberFormatException {
		if (config.getString("sampling.controlSize") != null) {
			controlSamplingSize = config.getInt("sampling.controlSize");
		} else
			controlSamplingSize = 1;
		LOG.info("The control sampling size: {}", controlSamplingSize);

		if (config.getString("sampling.eventRate") != null) {
			eventSamplingRate = config.getDouble("sampling.eventRate");
		} else
			eventSamplingRate = 1.0;
		LOG.info("The event sampling rate: {}", eventSamplingRate);

		if (config.getString("sampling.randomSeed") != null) {
			randomSeed = config.getInt("sampling.randomSeed");
		} else
			randomSeed = 1;
		LOG.info("The random seed: {}", randomSeed);

		if (args.length >= 3) {
			outputDirectory = args[1].trim();
			exponentialWeight = Double.parseDouble(args[2].trim());
		} else {

			if (config.getString("timeWeightedStatistic.exponentialWeight") != null) {
				exponentialWeight = config
						.getDouble("timeWeightedStatistic.exponentialWeight");
				LOG.info("Exponential weight of time weighted statistics: {}",
						exponentialWeight);
			}

			if (config.getString("sampling.outputDirectory") != null) {
				outputDirectory = config.getString("sampling.outputDirectory");
				LOG.info("Sampling output directory: {}", outputDirectory);
			}

		}

		commonStatisticParameterMap.put("exponentialWeight", exponentialWeight);

		String mappedEventSamplersFile = config.getString("eventSamplersFile");
		if (mappedEventSamplersFile != null) {

			LOG.info("Process the file for mapped event samplers: {}",
					mappedEventSamplersFile);

			try (FileReader reader = new FileReader(new File(
					mappedEventSamplersFile))) {

				Properties props = new Properties();

				props.load(reader);

				for (Object _eventClass : props.keySet()) {

					String eventClassName = (String) _eventClass;

					StringTokenizer tokenizer = new StringTokenizer(props
							.getProperty(eventClassName).trim(), "<>");

					if (tokenizer.countTokens() < 2) {
						LOG.info("There is no data mapping for item [{} = {}]",
								eventClassName,
								props.getProperty(eventClassName));
						continue;
					} else {
						String samplerClassName = tokenizer.nextToken();
						String samplerDefinitionFile = tokenizer.nextToken();
						createEventSampler(eventClassName, samplerClassName,
								samplerDefinitionFile);
					}
				}

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} else {
			LOG.info("There is no file for mapped event samplers");
			System.exit(-1);
		}
	}

	protected static void createEventSampler(String eventClassName,
			String samplerClassName, String samplerDefinitionFile) {
		try {
			Class<?> eventClass = Class.forName(eventClassName);

			Class<?> samplerClass = Class.forName(samplerClassName);
			Constructor<?> samplerConstructor = samplerClass.getConstructor();
			Sampler sampler = (Sampler) samplerConstructor.newInstance();

			LOG.info("Created the sampler {} for the event class {}", sampler
					.getClass().getSimpleName(), eventClass.getSimpleName());

			setSamplerParameters(samplerDefinitionFile, eventClass, sampler);

			engine.addSampler(eventClass, sampler);

			LOG.info("Added the sampler {} to the inference engine", sampler
					.getClass().getSimpleName());

		} catch (Exception e) {
			LOG.error("Failed to create the sampler {} for the event class {}",
					samplerClassName, eventClassName);
			e.printStackTrace();
		}
	}

	protected static void setSamplerParameters(String samplerDefinitionFile,
			Class<?> eventClass, Sampler sampler) {

		LOG.info(">> setSamplerParameters()");

		sampler.setName(sampler.getClass().getSimpleName());
		sampler.setRandomSeed(randomSeed);
		sampler.setControlSamplingSize(controlSamplingSize);
		sampler.setEventSamplingRatio(eventSamplingRate);
		sampler.setTimestampFormatter(timestampFormatter);
		sampler.setCommonStatisticParameterMap(commonStatisticParameterMap);

		sampler.setSamplerDefinition(samplerDefinitionFile, statisticBuilder);

		String samplingDataFilePath = outputDirectory + "/NCC_"
				+ eventClass.getSimpleName() + "_"
				+ Math.floor(startingObservationTime) + "_"
				+ Math.floor(endingObservationTime) + "_"
				+ Math.floor(controlSamplingSize) + "_"
				+ timestampFormatter.format(100 * eventSamplingRate) + "_"
				+ Math.floor(randomSeed) + "_"
				+ timestampFormatter.format(Math.abs(exponentialWeight))
				+ ".txt";
		sampler.setSamplingDataFilePath(samplingDataFilePath);

		String riskSetFilePath = outputDirectory + "/RiskSet_"
				+ eventClass.getSimpleName() + "_"
				+ Math.floor(startingObservationTime) + "_"
				+ Math.floor(endingObservationTime) + "_"
				+ Math.floor(controlSamplingSize) + "_"
				+ timestampFormatter.format(100 * eventSamplingRate) + "_"
				+ Math.floor(randomSeed) + "_"
				+ timestampFormatter.format(Math.abs(exponentialWeight))
				+ ".txt";
		sampler.setRiskSetFilePath(riskSetFilePath);

	}
}
