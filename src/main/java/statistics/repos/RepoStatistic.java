package statistics.repos;

import events.Event;

import statistics.Statistic;

public abstract interface RepoStatistic extends Statistic {

	abstract public double getValue(int repoID, Event event);

	abstract public double[] getValues(int repoID, Event event);

}
