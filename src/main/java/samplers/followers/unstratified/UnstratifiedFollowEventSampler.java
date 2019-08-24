package samplers.followers.unstratified;

import events.users.FollowEvent;
import graphs.atRiskStreams.UserAtRiskStatus;
import graphs.elements.WeightedEdge;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import samplers.followers.FollowEventSampler;

public abstract class UnstratifiedFollowEventSampler extends FollowEventSampler {

	public UnstratifiedFollowEventSampler() {
	}

	@Override
	protected String getCurrentRiskSetData() {
		return UserAtRiskStatus.getInstance().getRiskSet().size() + "\t"
				+ (UserAtRiskStatus.getInstance().getRiskSet().size() - 1);
	}

	protected void getUnstratifiedFullControls(FollowEvent followEvent,
			TreeSet<WeightedEdge> selectedEdges) {

		Set<Integer> atRiskUsers = UserAtRiskStatus.getInstance().getRiskSet();

		for (int senderIndex : atRiskUsers) {
			for (int receiverIndex : atRiskUsers) {
				// Ignore self edges
				if (senderIndex == receiverIndex)
					continue;
				// If the candidate edge is NOT the event edge
				if (followEvent.getSenderID() != senderIndex
						|| followEvent.getReceiverID() != receiverIndex)
					// then add it to the selected list
					selectedEdges.add(new WeightedEdge(senderIndex,
							receiverIndex, 1.0));
			}
		}

	}

	protected void getUnstratifiedRandomControls(FollowEvent followEvent,
			TreeSet<WeightedEdge> selectedEdges) {

		ArrayList<Integer> atRiskUsers = new ArrayList<Integer>(
				UserAtRiskStatus.getInstance().getRiskSet());

		for (int i = 0; i < controlSamplingSize; i++) {

			WeightedEdge selectedEdge;
			int selectedSender;
			int selectedReceiver;

			do {
				selectedSender = atRiskUsers.get(randomGenerator
						.nextInt(atRiskUsers.size()));
				selectedReceiver = atRiskUsers.get(randomGenerator
						.nextInt(atRiskUsers.size()));
				selectedEdge = new WeightedEdge(selectedSender,
						selectedReceiver, 1.0);
			} while (
			// is a self edge
			(selectedSender == selectedReceiver
			// is the event edge
					|| (followEvent.getSenderID() == selectedSender && followEvent
							.getReceiverID() == selectedReceiver))
					// is already selected in the control set
					|| selectedEdges.contains(selectedEdge));

			// add the edge to the selected list
			selectedEdges.add(selectedEdge);
		}

	}

}
