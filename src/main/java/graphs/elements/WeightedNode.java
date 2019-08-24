package graphs.elements;

public class WeightedNode implements Comparable<WeightedNode> {

	protected int nodeID;
	protected double weight;

	public WeightedNode() {
	}

	public WeightedNode(int nodeID, double weight) {
		this.nodeID = nodeID;
		this.weight = weight;
	}

	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int compareTo(WeightedNode anotherWeightedNode) {
		if (nodeID == anotherWeightedNode.nodeID
				&& weight == anotherWeightedNode.weight)
			return 0;
		else if (weight > anotherWeightedNode.weight)
			return 1;
		else if (weight < anotherWeightedNode.weight)
			return -1;
		else if (nodeID > anotherWeightedNode.nodeID)
			return 1;
		else
			return -1;
	}

	public boolean equals(Object obj) {
		WeightedNode anotherWeightedNode = (WeightedNode) obj;
		if (nodeID == anotherWeightedNode.nodeID
				&& weight == anotherWeightedNode.weight)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Integer(nodeID)).hashCode()
				+ (new Double(weight)).hashCode();
	}

}
