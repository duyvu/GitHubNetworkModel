package events.covariates.nodes;

import java.util.TreeSet;

import statistics.types.ExternalDynamicStatistic;
import utils.ArrayMethods;

public class NodeSetCovariateEvent extends NodeCovariateEvent {

	public TreeSet<Integer> values;

	public NodeSetCovariateEvent(double time, int nodeID,
			TreeSet<Integer> values, ExternalDynamicStatistic statistic) {
		super(time, nodeID, statistic);
		this.values = values;
	}

	public boolean equals(Object object) {
		if (object instanceof NodeSetCovariateEvent) {
			NodeSetCovariateEvent anotherEvent = (NodeSetCovariateEvent) object;
			if (time == anotherEvent.time && nodeID == anotherEvent.nodeID
					&& values == anotherEvent.values)
				return true;
			else
				return false;
		}
		return false;
	}

	public int hashCode() {
		return ((new Double(time)).hashCode()
				+ (new Integer(nodeID)).hashCode() + values.hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":nodeID=" + nodeID + ":values="
				+ ArrayMethods.toStringIntegerSet(values);
	}

}
