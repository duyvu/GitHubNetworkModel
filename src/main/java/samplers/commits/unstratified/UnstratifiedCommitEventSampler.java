package samplers.commits.unstratified;

import events.commits.CommitEvent;
import graphs.atRiskStreams.ProjectAtRiskStatus;
import graphs.atRiskStreams.UserAtRiskStatus;
import graphs.elements.WeightedEdge;

import java.util.ArrayList;
import java.util.TreeSet;

import samplers.commits.CommitEventSampler;

public abstract class UnstratifiedCommitEventSampler extends CommitEventSampler {

	public UnstratifiedCommitEventSampler() {
	}

	@Override
	protected String getCurrentRiskSetData() {
		return UserAtRiskStatus.getInstance().getRiskSet().size() + "\t"
				+ ProjectAtRiskStatus.getInstance().getRiskSet().size();
	}

	protected void getUnstratifiedFullControls(CommitEvent commitEvent,
			TreeSet<WeightedEdge> selectedEdges) {

		for (int userIndex : UserAtRiskStatus.getInstance().getRiskSet()) {
			for (int projectIndex : ProjectAtRiskStatus.getInstance()
					.getRiskSet()) {
				// if the candidate edge is NOT the event edge
				if (commitEvent.getAuthorID() != userIndex
						|| commitEvent.getRepoID() != projectIndex)
					// then add it to the selected list
					selectedEdges.add(new WeightedEdge(userIndex, projectIndex,
							1.0));
			}
		}

	}

	protected void getUnstratifiedRandomControls(CommitEvent commitEvent,
			TreeSet<WeightedEdge> selectedEdges) {

		ArrayList<Integer> atRiskUsers = new ArrayList<Integer>(
				UserAtRiskStatus.getInstance().getRiskSet());

		ArrayList<Integer> atRiskProjects = new ArrayList<Integer>(
				ProjectAtRiskStatus.getInstance().getRiskSet());

		for (int i = 0; i < controlSamplingSize; i++) {

			WeightedEdge selectedEdge;
			int selectedUser;
			int selectedRepo;

			do {
				selectedUser = atRiskUsers.get(randomGenerator
						.nextInt(atRiskUsers.size()));
				selectedRepo = (Integer) atRiskProjects.get(randomGenerator
						.nextInt(atRiskProjects.size()));
				selectedEdge = new WeightedEdge(selectedUser, selectedRepo,
						1.0);
			} while (
			// is the event edge
			(commitEvent.getAuthorID() == selectedUser && commitEvent
					.getRepoID() == selectedRepo)
			// is already selected in the control set
					|| selectedEdges.contains(selectedEdge));

			// add the edge to the selected list
			selectedEdges.add(selectedEdge);
		}

	}

}
