package statistics.commits;

import events.Event;

import statistics.Statistic;

public abstract interface UserRepoStatistic extends Statistic {

	abstract public double getValue(int userID, int repoID, Event event);

	abstract public double[] getValues(int userID, int repoID, Event event);

}
