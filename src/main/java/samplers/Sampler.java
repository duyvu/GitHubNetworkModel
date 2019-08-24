package samplers;

import java.text.DecimalFormat;
import java.util.HashMap;

import statistics.builders.StatisticBuilder;

import events.Event;

public interface Sampler {

	public void setName(String name);

	public String getName();

	public void setRandomSeed(long randomSeed);

	public long getRandomSeed();

	public void setEventSamplingRatio(double eventSamplingRatio);

	public double getEventSamplingRatio();

	public void setControlSamplingSize(int controlSamplingSize);

	public int getControlSamplingSize();

	public void setTimestampFormatter(DecimalFormat timestampFormatter);

	public DecimalFormat getTimestampFormatter();

	public void setSamplingDataFilePath(String samplingDataFilePath);

	public String getSamplingDataFilePath();

	public void setRiskSetFilePath(String riskSetFilePath);

	public String getRiskSetFilePath();

	public void setSamplerDefinition(String samplerDefinitionFile,
			StatisticBuilder statisticBuilder);

	public void setCommonStatisticParameterMap(
			HashMap<String, Double> commonStatisticParameterMap);

	public HashMap<String, Double> getCommonStatisticParameterMap();

	public void startSampling();

	public void sample(Event event);

	public void finishSampling();

	public String getSASInputStatistics();
	
	public String getSASModelDescription();
	
}
