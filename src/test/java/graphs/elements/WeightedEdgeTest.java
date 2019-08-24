package graphs.elements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class WeightedEdgeTest {

	@Test
	public void Constructor() {
		int senderID = 1;
		int receiverID = 2;
		double weight = 123.456;
		WeightedEdge wEdge = new WeightedEdge(senderID, receiverID, weight);
		assertThat(senderID, is(equalTo(wEdge.senderID)));
		assertThat(receiverID, is(equalTo(wEdge.receiverID)));
		assertThat(weight, is(equalTo(wEdge.weight)));
	}

	@Test(dependsOnMethods = { "Constructor" })
	public void compareTo() {
		int senderID = 1;
		int receiverID = 2;
		double weight = 123.456;
		WeightedEdge wEdge1 = new WeightedEdge(senderID, receiverID, weight);
		WeightedEdge wEdge2 = new WeightedEdge(senderID, receiverID, weight);
		assertThat(wEdge1, comparesEqualTo(wEdge2));

		double weight2 = 456.789;
		WeightedEdge wEdge3 = new WeightedEdge(senderID, receiverID, weight2);
		assertThat(wEdge1, lessThan(wEdge3));
		assertThat(wEdge3, greaterThan(wEdge1));

		int senderID2 = 3;
		WeightedEdge wEdge4 = new WeightedEdge(senderID2, receiverID, weight);
		assertThat(wEdge1, lessThan(wEdge4));
		assertThat(wEdge4, greaterThan(wEdge1));

		int receiverID2 = 4;
		WeightedEdge wEdge5 = new WeightedEdge(senderID, receiverID2, weight);
		assertThat(wEdge1, lessThan(wEdge5));
		assertThat(wEdge5, greaterThan(wEdge1));
	}

	@Test(dependsOnMethods = { "Constructor" })
	public void equals() {
		int senderID = 1;
		int receiverID = 2;
		double weight = 123.456;
		WeightedEdge wEdge1 = new WeightedEdge(senderID, receiverID, weight);
		WeightedEdge wEdge2 = new WeightedEdge(senderID, receiverID, weight);
		assertTrue(wEdge1.equals(wEdge2),
				"Edges 1 and 2 having the same initial values must be equal");

		int senderID2 = 3;
		WeightedEdge wEdge3 = new WeightedEdge(senderID2, receiverID, weight);
		assertFalse(wEdge1.equals(wEdge3),
				"Edges 1 and 3 having different senderID values must not be equal");

		int receiverID2 = 4;
		WeightedEdge wEdge4 = new WeightedEdge(senderID, receiverID2, weight);
		assertFalse(wEdge1.equals(wEdge4),
				"Edges 1 and 4 having different receiverID values must not be equal");

		double weight2 = 456.789;
		WeightedEdge wEdge5 = new WeightedEdge(senderID, receiverID, weight2);
		assertFalse(wEdge1.equals(wEdge5),
				"Edges 1 and 5 having different weight values must not be equal");

	}

}
