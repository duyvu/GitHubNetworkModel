package samplers.issues.unstratified;

import events.issues.IssueCommentEvent;
import graphs.atRiskStreams.UserAtRiskStatus;
import graphs.atRiskStreams.IssueAtRiskStatus;
import graphs.elements.WeightedEdge;

import java.util.ArrayList;
import java.util.TreeSet;

import samplers.issues.IssueCommentEventSampler;

public abstract class UnstratifiedIssueCommentEventSampler extends
		IssueCommentEventSampler {

	public UnstratifiedIssueCommentEventSampler() {
	}

	@Override
	protected String getCurrentRiskSetData() {
		return UserAtRiskStatus.getInstance().getRiskSet().size() + "\t"
				+ IssueAtRiskStatus.getInstance().getRiskSet().size();
	}

	protected void getUnstratifiedFullControls(
			IssueCommentEvent issueCommentEvent,
			TreeSet<WeightedEdge> selectedEdges) {

		for (int userIndex : UserAtRiskStatus.getInstance().getRiskSet()) {
			for (int issueIndex : IssueAtRiskStatus.getInstance().getRiskSet()) {
				// if the candidate edge is NOT the event edge
				if (issueCommentEvent.getUserID() != userIndex
						|| issueCommentEvent.getIssueID() != issueIndex)
					// then add it to the selected list
					selectedEdges.add(new WeightedEdge(userIndex, issueIndex,
							1.0));
			}
		}

	}

	protected void getUnstratifiedRandomControls(
			IssueCommentEvent issueCommentEvent,
			TreeSet<WeightedEdge> selectedEdges) {

		ArrayList<Integer> atRiskUsers = new ArrayList<Integer>(
				UserAtRiskStatus.getInstance().getRiskSet());

		ArrayList<Integer> atRiskIssues = new ArrayList<Integer>(
				IssueAtRiskStatus.getInstance().getRiskSet());

		for (int i = 0; i < controlSamplingSize; i++) {

			WeightedEdge selectedEdge;
			int selectedUser;
			int selectedIssue;

			do {
				selectedUser = atRiskUsers.get(randomGenerator
						.nextInt(atRiskUsers.size()));
				selectedIssue = (Integer) atRiskIssues.get(randomGenerator
						.nextInt(atRiskIssues.size()));
				selectedEdge = new WeightedEdge(selectedUser, selectedIssue,
						1.0);
			} while (
			// is the event edge
			(issueCommentEvent.getUserID() == selectedUser && issueCommentEvent
					.getIssueID() == selectedIssue)
			// is already selected in the control set
					|| selectedEdges.contains(selectedEdge));

			// add the edge to the selected list
			selectedEdges.add(selectedEdge);
		}

	}

}
