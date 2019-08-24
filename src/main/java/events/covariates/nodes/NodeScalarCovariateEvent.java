package events.covariates.nodes;

import statistics.types.ExternalDynamicStatistic;

public class NodeScalarCovariateEvent extends NodeCovariateEvent {

	public double value;

	public NodeScalarCovariateEvent(double time, int nodeID, double value,
			ExternalDynamicStatistic statistic) {
		super(time, nodeID, statistic);
		this.value = value;
	}

	public boolean equals(Object object) {
		if (object instanceof NodeScalarCovariateEvent) {
			NodeScalarCovariateEvent anotherEvent = (NodeScalarCovariateEvent) object;
			if (time == anotherEvent.time && nodeID == anotherEvent.nodeID
					&& value == anotherEvent.value)
				return true;
			else
				return false;
		}
		return false;
	}

	public int hashCode() {
		return ((new Double(time)).hashCode()
				+ (new Integer(nodeID)).hashCode() + (new Double(value))
				.hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":nodeID=" + nodeID + ":value=" + value;
	}

}
