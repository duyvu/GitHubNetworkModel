package samplers;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import configurations.SamplerDefinition;
import events.Event;
import graphs.elements.WeightedEdge;

public abstract class BaseSampler implements Sampler {

	private static final Logger LOG = LoggerFactory.getLogger(BaseSampler.class
			.getName());

	protected String name;

	protected long randomSeed;

	protected double eventSamplingRatio = 1.0;

	protected int controlSamplingSize = 100;

	protected DecimalFormat timestampFormatter;

	protected SamplerDefinition samplerDefinition;
	
	protected String samplingDataFilePath;

	protected String riskSetFilePath;

	protected HashMap<String, Double> commonStatisticParameterMap;

	protected Random randomGenerator;

	protected int strataIndex;

	protected double[] currentStatistics;

	protected PrintWriter samplingWriter;

	protected PrintWriter riskSetWriter = null;

	public BaseSampler() {
	}

	@Override
	public void startSampling() {

		randomGenerator = new Random(randomSeed);
		strataIndex = 0;

		try {
			samplingWriter = new PrintWriter(new File(samplingDataFilePath));
		} catch (Exception e) {
			LOG.error("Can not open the sampling data file: {}",
					samplingDataFilePath);
			System.exit(-1);
		}

		if (riskSetFilePath != null) {
			try {
				riskSetWriter = new PrintWriter(new File(riskSetFilePath));
			} catch (Exception e) {
				LOG.error("Can not open the risk set file: {}", riskSetFilePath);
				LOG.error(
						"Dynamic risk set size data from {} will not not be logged",
						this.getClass().getSimpleName());
			}
		}

	}

	@Override
	public void sample(Event event) {

		strataIndex++;

		if (eventSamplingRatio >= 1.0
				|| randomGenerator.nextDouble() < eventSamplingRatio) {

			// Sample the case-control data set for the current commit event

			// 1: Sample the control set which does not contain the case
			TreeSet<WeightedEdge> selectedEdges = new TreeSet<WeightedEdge>();
			getCaseControlSet(event, selectedEdges);

			// 2: Write both the case and the control set to file
			writeCaseControlSet(event, selectedEdges);

			if (riskSetWriter != null)
				riskSetWriter.println(getCurrentRiskSetData());

		}

	}

	protected abstract String getCurrentRiskSetData();

	protected abstract void getCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges);

	protected abstract void writeCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges);

	protected void writeRelationalStatistics(Event event, int senderID,
			int receiverID, boolean isEvent, double weight) {
		samplingWriter.write(strataIndex + "\t");
		samplingWriter.write(timestampFormatter.format(event.time) + "\t");
		samplingWriter.write(senderID + "\t");
		samplingWriter.write(receiverID + "\t");
		samplingWriter.write(weight + "\t");
		if (isEvent)
			samplingWriter.write(1 + "\t");
		else
			samplingWriter.write(0 + "\t");
		computeCurrentStatistics(event, senderID, receiverID, false);
		for (int statIndex = 0; statIndex < currentStatistics.length; statIndex++)
			samplingWriter.write(currentStatistics[statIndex] + "\t");
		samplingWriter.write("\n");
	}

	protected abstract void computeCurrentStatistics(Event event, int senderID,
			int receiverID, boolean interactionsIncluded);

	@Override
	public void finishSampling() {

		if (samplingWriter != null) {
			LOG.debug("Closing sampling data file: {}", samplingDataFilePath);
			samplingWriter.close();
		}

		if (riskSetWriter != null) {
			LOG.debug("Closing risk set file: {}", riskSetFilePath);
			riskSetWriter.close();
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getRandomSeed() {
		return randomSeed;
	}

	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}

	public double getEventSamplingRatio() {
		return eventSamplingRatio;
	}

	public void setEventSamplingRatio(double eventSamplingRatio) {
		this.eventSamplingRatio = eventSamplingRatio;
	}

	public int getControlSamplingSize() {
		return controlSamplingSize;
	}

	public void setControlSamplingSize(int controlSamplingSize) {
		this.controlSamplingSize = controlSamplingSize;
	}

	public DecimalFormat getTimestampFormatter() {
		return timestampFormatter;
	}

	public void setTimestampFormatter(DecimalFormat timestampFormatter) {
		this.timestampFormatter = timestampFormatter;
	}

	public String getSamplingDataFilePath() {
		return samplingDataFilePath;
	}

	public void setSamplingDataFilePath(String samplingDataFilePath) {
		this.samplingDataFilePath = samplingDataFilePath;
	}

	public String getRiskSetFilePath() {
		return riskSetFilePath;
	}

	public void setRiskSetFilePath(String riskSetFilePath) {
		this.riskSetFilePath = riskSetFilePath;
	}

	public HashMap<String, Double> getCommonStatisticParameterMap() {
		return commonStatisticParameterMap;
	}

	public void setCommonStatisticParameterMap(
			HashMap<String, Double> commonStatisticParameterMap) {
		this.commonStatisticParameterMap = commonStatisticParameterMap;
	}

}
