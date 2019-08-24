package events.covariates;

import events.Event;

import statistics.types.ExternalDynamicStatistic;

public abstract class CovariateEvent extends Event {

	protected ExternalDynamicStatistic statistic;

	public CovariateEvent(double eventTime) {
		super(eventTime);
	}

	public CovariateEvent(double eventTime, ExternalDynamicStatistic statistic) {
		super(eventTime);
		setListenerStatistic(statistic);
	}

	public ExternalDynamicStatistic getListenerStatistic() {
		return statistic;
	}

	public void setListenerStatistic(ExternalDynamicStatistic statistic) {
		this.statistic = statistic;
	}

}
