package graphs.elements;

public class WeightedEdge implements Comparable<WeightedEdge> {

	public int senderID;
	public int receiverID;
	public double weight;

	public WeightedEdge() {
	}

	public WeightedEdge(int senderID, int receiverID, double weight) {
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.weight = weight;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public int getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(int receiverID) {
		this.receiverID = receiverID;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int compareTo(WeightedEdge anotherWeightedEdge) {
		if (senderID == anotherWeightedEdge.senderID
				&& receiverID == anotherWeightedEdge.receiverID
				&& weight == anotherWeightedEdge.weight)
			return 0;
		else if (weight > anotherWeightedEdge.weight)
			return 1;
		else if (weight < anotherWeightedEdge.weight)
			return -1;
		else if (senderID > anotherWeightedEdge.senderID)
			return 1;
		else if (senderID < anotherWeightedEdge.senderID)
			return -1;
		else if (receiverID > anotherWeightedEdge.receiverID)
			return 1;
		else
			return -1;
	}

	public boolean equals(Object obj) {
		WeightedEdge edge = (WeightedEdge) obj;
		if (senderID == edge.senderID && receiverID == edge.receiverID
				&& weight == edge.weight)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Integer(senderID)).hashCode()
				+ (new Integer(receiverID)).hashCode()
				+ (new Double(weight)).hashCode();
	}

}
