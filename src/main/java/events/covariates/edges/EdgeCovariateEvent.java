package events.covariates.edges;

import statistics.types.ExternalDynamicStatistic;
import events.covariates.CovariateEvent;

public class EdgeCovariateEvent extends CovariateEvent {

	public int senderID;
	public int receiverID;
	public double value;

	public EdgeCovariateEvent(double time, int senderID, int receiverID,
			double value, ExternalDynamicStatistic statistic) {
		super(time, statistic);
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.value = value;
	}

	public boolean equals(Object object) {
		if (object instanceof EdgeCovariateEvent) {
			EdgeCovariateEvent anotherEvent = (EdgeCovariateEvent) object;
			if (time == anotherEvent.time && senderID == anotherEvent.senderID
					&& receiverID == anotherEvent.receiverID
					&& value == anotherEvent.value)
				return true;
			else
				return false;
		}
		return false;
	}

	public int hashCode() {
		return ((new Double(time)).hashCode()
				+ (new Integer(senderID)).hashCode()
				+ (new Integer(receiverID)).hashCode() + (new Double(value))
				.hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":senderID=" + senderID + ":receiverID=" + receiverID
				+ ":value=" + value;
	}

}
