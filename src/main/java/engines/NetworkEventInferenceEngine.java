package engines;

import events.Event;
import events.EventStreams;
import graphs.processors.GitHubEventStreamProcessors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import samplers.Sampler;

public class NetworkEventInferenceEngine {

	private static final Logger LOG = LoggerFactory
			.getLogger(NetworkEventInferenceEngine.class.getName());

	protected int probingEventStep = 1000;

	protected boolean standardizingStatistics = false;

	protected boolean removingScript = true;

	protected boolean removingData = true;

	// The starting observation time
	public double startingObservationTime;

	// The ending observation time
	public double endingObservationTime;

	protected EventStreams eventStreams;

	protected GitHubEventStreamProcessors gitHubEventStreamProcessor;

	protected HashMap<Class<?>, ArrayList<Sampler>> samplers;

	public NetworkEventInferenceEngine(double startingObservationTime,
			double endingObservationTime, EventStreams eventStreams,
			GitHubEventStreamProcessors eventStreamProcessor) {

		this.startingObservationTime = startingObservationTime;
		LOG.info("The starting observation time = {}", startingObservationTime);

		this.endingObservationTime = endingObservationTime;
		LOG.info("The ending observation time = {}", endingObservationTime);

		this.eventStreams = eventStreams;

		this.gitHubEventStreamProcessor = eventStreamProcessor;

		samplers = new HashMap<Class<?>, ArrayList<Sampler>>();

	}

	public void addSampler(Class<?> eventClass, Sampler sampler) {
		LOG.info("Adding a sampler {}  for {} events", sampler.getName(),
				eventClass.getName());
		if (samplers.get(eventClass) != null)
			samplers.get(eventClass).add(sampler);
		else {
			ArrayList<Sampler> eventClassSamplers = new ArrayList<Sampler>();
			eventClassSamplers.add(sampler);
			samplers.put(eventClass, eventClassSamplers);
		}
	}

	public HashMap<Class<?>, ArrayList<Sampler>> getSamplers() {
		return samplers;
	}

	public void runEventStreams() {

		LOG.info(">> runEventStreams()");

		PriorityQueue<Event> eventQueue = eventStreams.getEventQueue();

		LOG.info("The total number of events to be processed is {}",
				eventQueue.size());

		// Start all samplers
		for (ArrayList<Sampler> eventClassSamplers : samplers.values())
			for (Sampler sampler : eventClassSamplers)
				sampler.startSampling();

		LOG.info("Start rolling events until the time right before the starting observation time!");
		
		while (eventQueue.peek() != null
				&& eventQueue.peek().time < startingObservationTime) {
			Event event = eventQueue.poll();
			gitHubEventStreamProcessor.processEvent(event);
		}

		LOG.info("Finish rolling events until the time right before the starting observation time!");

		LOG.info("Start rolling events in the observation window");

		int eventCounter = 0;
		while (eventQueue.peek() != null
				&& eventQueue.peek().time <= endingObservationTime) {

			Event event = eventQueue.poll();

			eventCounter++;
			if ((eventCounter % probingEventStep) == 0)
				LOG.info("Processing event {} at time {}", eventCounter,
						event.time);

			for (Class<?> eventType : samplers.keySet()) {
				if (eventType.isAssignableFrom(event.getClass())) {
					ArrayList<Sampler> eventClassSamplers = samplers
							.get(eventType);
					if (eventClassSamplers != null) {
						for (Sampler sampler : eventClassSamplers)
							sampler.sample(event);
					}
				}
			}

			gitHubEventStreamProcessor.processEvent(event);
		}

		// Finish all samplers
		for (ArrayList<Sampler> eventClassSamplers : samplers.values())
			for (Sampler sampler : eventClassSamplers)
				sampler.finishSampling();

		LOG.info("Finish rolling events in the observation window");

		LOG.info("<< runEventStreams()");
	}

	public int getProbingEventStep() {
		return probingEventStep;
	}

	public void setProbingEventStep(int probingEventStep) {
		this.probingEventStep = probingEventStep;
	}

	public boolean isStandardizingStatistics() {
		return standardizingStatistics;
	}

	public void setStandardizingStatistics(boolean standardizingStatistics) {
		this.standardizingStatistics = standardizingStatistics;
	}

	public boolean isRemovingScript() {
		return removingScript;
	}

	public void setRemovingScript(boolean removingScript) {
		this.removingScript = removingScript;
	}

	public boolean isRemovingData() {
		return removingData;
	}

	public void setRemovingData(boolean removingData) {
		this.removingData = removingData;
	}

	public void estimateNetworkEventModels(boolean isParallel) {

		for (ArrayList<Sampler> eventClassSamplers : samplers.values())
			for (Sampler sampler : eventClassSamplers)
				try {
					runSAS(sampler, standardizingStatistics, removingScript);

					if (removingData) {
						LOG.info("Delete the case-control sampling file: {}",
								sampler.getSamplingDataFilePath());
						File samplingDataFile = new File(
								sampler.getSamplingDataFilePath());
						samplingDataFile.delete();
					}

				} catch (InterruptedException e) {
					LOG.error(
							"Failed to do inference for the sampler {} due to a Java process interrupted exception ",
							sampler.getName());
					e.printStackTrace();
				} catch (IOException e) {
					LOG.error(
							"Failed to do inference for the sampler {} due to an IO exception",
							sampler.getName());
					e.printStackTrace();
				}

	}

	protected void runSAS(Sampler sampler, boolean standardizingStatistics,
			boolean removingScript) throws IOException, InterruptedException {

		String outputDirectory = (new File(sampler.getSamplingDataFilePath()))
				.getParent();

		String sasFilePath = sampler.getSamplingDataFilePath().substring(0,
				sampler.getSamplingDataFilePath().lastIndexOf("."))
				+ ".sas";
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
				new File(sasFilePath))));

		// Read data
		String dataObject = "samples_" + sampler.getRandomSeed();
		writer.write("data " + dataObject + "; \n");
		writer.write("infile '" + sampler.getSamplingDataFilePath()
				+ "' firstobs=1 delimiter='09'x lrecl=10000;" + "\n");

		writer.write("input Index Time Sender Receiver Weight Censoring "
				+ sampler.getSASInputStatistics());
		writer.write("Outcome = 2 - Censoring;\n");
		writer.write("run;\n");

		// Standardize data
		String standardizedDataObject = dataObject;
		if (standardizingStatistics) {
			standardizedDataObject = "standardized_samples_"
					+ sampler.getRandomSeed();
			writer.write("proc standard data=" + dataObject
					+ " mean=0 std=1 out=" + standardizedDataObject + ";\n");
			writer.write("var " + sampler.getSASInputStatistics() + ";\n");
			writer.write("run;\n");
		}

		// Summarize data
		writer.write("proc means data=" + standardizedDataObject + ";\n");
		writer.write("run;\n");

		// Run Cox regression
		writer.write("proc phreg data=" + standardizedDataObject + "; \n");
		writer.write("model Outcome*Censoring(0) = "
				+ sampler.getSASModelDescription());
		writer.write("strata Index;\n");
		writer.write("run;\n");

		writer.close();

		Process ps = Runtime.getRuntime().exec(
				"sas -MEMSIZE 10G " + " -work " + outputDirectory.trim() + " "
						+ sasFilePath + " -log '" + outputDirectory.trim()
						+ "' -print '" + outputDirectory.trim() + "'");

		LOG.info("Start the SAS script");

		ps.waitFor();

		LOG.info("Finish the SAS script");

		if (removingScript) {
			LOG.info("Delete the SAS script: {}", sasFilePath);
			File sasFile = new File(sasFilePath);
			sasFile.delete();
		}

	}

}
