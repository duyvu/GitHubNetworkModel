package graphs.elements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

public class WeightedNodeTest {

	@Test
	public void Constructor() {
		int nodeID = 1;
		double weight = 1.234;
		WeightedNode wNode = new WeightedNode(nodeID, weight);
		assertThat(nodeID, is(equalTo(wNode.nodeID)));
		assertThat(weight, is(equalTo(wNode.weight)));
	}

	@Test(dependsOnMethods = { "Constructor" })
	public void compareTo() {
		int nodeID1 = 1;
		double weight1 = 1.0;
		WeightedNode wNode1 = new WeightedNode(nodeID1, weight1);
		WeightedNode wNode2 = new WeightedNode(nodeID1, weight1);
		assertThat(wNode1, comparesEqualTo(wNode2));

		double weight2 = 2.0;
		WeightedNode wNode3 = new WeightedNode(nodeID1, weight1);
		WeightedNode wNode4 = new WeightedNode(nodeID1, weight2);
		assertThat(wNode3, lessThan(wNode4));
		assertThat(wNode4, greaterThan(wNode3));

		int nodeID2 = 2;
		WeightedNode wNode5 = new WeightedNode(nodeID1, weight1);
		WeightedNode wNode6 = new WeightedNode(nodeID2, weight1);
		assertThat(wNode5, lessThan(wNode6));
		assertThat(wNode6, greaterThan(wNode5));
	}

	@Test(dependsOnMethods = { "Constructor" })
	public void equals() {
		int nodeID1 = 1;
		double weight1 = 1.234;
		WeightedNode wNode1 = new WeightedNode(nodeID1, weight1);
		WeightedNode wNode2 = new WeightedNode(nodeID1, weight1);
		assertTrue(wNode1.equals(wNode2),
				"Nodes 1 and 2 having the same initial values must be equal");

		int nodeID2 = 2;
		WeightedNode wNode3 = new WeightedNode(nodeID2, weight1);
		assertFalse(wNode1.equals(wNode3),
				"Nodes 1 and 3 having different nodeID values must not be equal");

		double weight2 = 234.5;
		WeightedNode wNode4 = new WeightedNode(nodeID1, weight2);
		assertFalse(wNode1.equals(wNode4),
				"Nodes 1 and 4 having different weight values must not be equal");

	}
}
