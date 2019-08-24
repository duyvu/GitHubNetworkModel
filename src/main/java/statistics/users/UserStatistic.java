package statistics.users;

import events.Event;

import statistics.Statistic;

public abstract interface UserStatistic extends Statistic {

	abstract public double getValue(int userID, Event event);

	abstract public double[] getValues(int userID, Event event);

}
