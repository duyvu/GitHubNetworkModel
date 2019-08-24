package statistics.issues.comments;

import events.Event;

import statistics.Statistic;

public abstract interface IssueStatistic extends Statistic {

	abstract public double getValue(int issueID, Event event);

	abstract public double[] getValues(int issueID, Event event);

}
