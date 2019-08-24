package events.covariates.nodes;

import statistics.types.ExternalDynamicStatistic;
import events.covariates.CovariateEvent;

public abstract class NodeCovariateEvent extends CovariateEvent {

	protected int nodeID;

	public NodeCovariateEvent(double time, int nodeID,
			ExternalDynamicStatistic statistic) {
		super(time, statistic);
		this.nodeID = nodeID;
	}

	public boolean equals(Object object) {
		if (object instanceof NodeCovariateEvent) {
			NodeCovariateEvent anotherEvent = (NodeCovariateEvent) object;
			if (time == anotherEvent.time && nodeID == anotherEvent.nodeID)
				return true;
			else
				return false;
		}
		return false;
	}

	public int hashCode() {
		return ((new Double(time)).hashCode() + (new Integer(nodeID))
				.hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":nodeID=" + nodeID;
	}

	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

}
