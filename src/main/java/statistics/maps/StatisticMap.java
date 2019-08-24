package statistics.maps;

import java.util.HashMap;

import statistics.Statistic;

public abstract class StatisticMap {

	protected HashMap<String, Statistic> namedStatistics;

	public StatisticMap() {
		namedStatistics = new HashMap<String, Statistic>();
	}

	protected void addNamedStatistic(Statistic statistic) {
		namedStatistics.put(statistic.getName(), statistic);
	}

	public Statistic getNamedStatistic(String name) {
		return namedStatistics.get(name);
	}

}
