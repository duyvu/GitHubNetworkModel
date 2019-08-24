package statistics.comments;

import events.Event;

import statistics.Statistic;

public abstract interface UserIssueStatistic extends Statistic {

	abstract public double getValue(int userID, int issueID, Event event);

	abstract public double[] getValues(int userID, int issueID, Event event);

}
