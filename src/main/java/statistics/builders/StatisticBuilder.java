package statistics.builders;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import statistics.NetworkStatistic;
import statistics.Statistic;
import statistics.types.ExternalDynamicStatistic;
import statistics.types.PiecewiseStatistic;
import statistics.types.StaticStatistic;
import configurations.StatisticDefinition;
import graphs.atRiskStreams.CommitAtRiskStatus;

public class StatisticBuilder {

	private static final Logger LOG = LoggerFactory
			.getLogger(StatisticBuilder.class.getName());

	private static volatile StatisticBuilder instance = null;

	public static StatisticBuilder getInstance() {
		if (instance == null) {
			synchronized (CommitAtRiskStatus.class) {
				// Double check
				if (instance == null) {
					instance = new StatisticBuilder();
				}
			}
		}
		return instance;
	}

	// private constructor
	private StatisticBuilder() {
	}

	public Statistic createStatistic(StatisticDefinition statisticDefinition,
			HashMap<String, Double> commonStatisticParameterMap) {

		Statistic statistic = null;
		try {
			String statClassDef = statisticDefinition.getDefinition();
			String statName = statisticDefinition.getName();
			statistic = (Statistic) getStatisticObject(statClassDef, statName);

			LOG.info("Creating a statistic {} under the name {} ", statistic
					.getClass().getSimpleName(), statName);

			setOtherParameters(statistic, statisticDefinition,
					commonStatisticParameterMap);

			return statistic;
		} catch (Exception e) {
			LOG.error("Failled to create the statistic: {}",
					statisticDefinition);
			e.printStackTrace();
		}

		return statistic;
	}

	protected NetworkStatistic getStatisticObject(String statClassDef,
			String statName) throws ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		Class<?> statClass = Class.forName(statClassDef);
		Constructor<?> ctor = statClass.getConstructor();
		NetworkStatistic statObject = (NetworkStatistic) ctor.newInstance();

		Method method = statObject.getClass()
				.getMethod("setName", String.class);
		method.invoke(statObject, statName);

		return statObject;
	}

	protected void setOtherParameters(Statistic statistic,
			StatisticDefinition statisticDefinition,
			HashMap<String, Double> commonStatisticParameterMap)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {

		Method method;

		String relationalEventClassName = statisticDefinition
				.getRelationalEventClassName();
		if (relationalEventClassName != null
				&& !relationalEventClassName.isEmpty()) {
			LOG.info("Setting relationalEventClassName: {}",
					relationalEventClassName);
			method = statistic.getClass().getMethod(
					"setRelationalEventClassName", String.class);
			method.invoke(statistic, relationalEventClassName);
		}

		String staticData = statisticDefinition.getStaticData();
		if (staticData != null && !staticData.isEmpty()
				&& statistic instanceof StaticStatistic) {
			LOG.info("Setting loadData: {}", staticData);
			method = statistic.getClass().getMethod("loadData", String.class);
			method.invoke(statistic, staticData);
		}

		String dynamicData = statisticDefinition.getDynamicData();
		if (dynamicData != null && !dynamicData.isEmpty()
				&& statistic instanceof ExternalDynamicStatistic) {
			LOG.info("Setting loadDynamicData: {}", dynamicData);
			method = statistic.getClass().getMethod("loadDynamicData",
					String.class);
			method.invoke(statistic, dynamicData);
		}

		Double startInterval = statisticDefinition.getStartInterval();
		Double endInterval = statisticDefinition.getEndInterval();
		if (startInterval != null && endInterval != null
				&& statistic instanceof PiecewiseStatistic) {
			LOG.info("Setting Start Interval = {} - End Interval = {}",
					startInterval, endInterval);
			method = statistic.getClass().getMethod("setInterval",
					Double.class, Double.class);
			method.invoke(statistic, startInterval, endInterval);
		}

		for (String commonStatisticParameterName : commonStatisticParameterMap
				.keySet()) {
			try {
				double commonStatisticParameterValue = commonStatisticParameterMap
						.get(commonStatisticParameterName);
				PropertyUtils.setSimpleProperty(statistic,
						commonStatisticParameterName,
						commonStatisticParameterValue);
				LOG.info("Setting {} = {}", commonStatisticParameterName,
						commonStatisticParameterValue);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				LOG.warn("Failed to set the property {} of {}",
						commonStatisticParameterName, statistic.getClass()
								.getSimpleName());
			}
		}

	}

}
