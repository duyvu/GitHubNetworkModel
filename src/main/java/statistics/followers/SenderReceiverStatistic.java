package statistics.followers;

import events.Event;

import statistics.Statistic;

public abstract interface SenderReceiverStatistic extends Statistic {

	abstract public double getValue(int senderID, int receiverID, Event event);

	abstract public double[] getValues(int senderID, int receiverID, Event event);

}
