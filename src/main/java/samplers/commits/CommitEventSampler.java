/**
 * 
 */
package samplers.commits;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import configurations.StatisticDefinition;
import configurations.commits.CommitEventSamplerDefinition;

import events.Event;
import events.commits.CommitEvent;
import graphs.elements.WeightedEdge;
import samplers.BaseSampler;
import statistics.StatisticInteraction;
import statistics.builders.StatisticBuilder;
import statistics.commits.CommitStatisticMap;
import statistics.commits.UserRepoStatistic;
import statistics.repos.RepoStatistic;
import statistics.users.UserStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class CommitEventSampler extends BaseSampler {

	private static final Logger LOG = LoggerFactory
			.getLogger(CommitEventSampler.class.getName());

	protected CommitStatisticMap commitStatisticMap;

	/**
	 * 
	 */
	public CommitEventSampler() {
	}

	@Override
	public void setSamplerDefinition(String samplerDefinitionFile,
			StatisticBuilder statisticBuilder) {

		samplerDefinition = new CommitEventSamplerDefinition(
				samplerDefinitionFile);

		LOG.info(samplerDefinition.toString());

		commitStatisticMap = new CommitStatisticMap();

		constructStatistics(statisticBuilder);

		finalizeStatistics();
	}

	protected void constructStatistics(StatisticBuilder statisticBuilder) {

		for (Iterator<StatisticDefinition> it = ((CommitEventSamplerDefinition) samplerDefinition)
				.getUserStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition userStatisticDefinition = it.next();
			UserStatistic userStatistic = (UserStatistic) statisticBuilder
					.createStatistic(userStatisticDefinition,
							commonStatisticParameterMap);
			commitStatisticMap.addUserStatistic(userStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((CommitEventSamplerDefinition) samplerDefinition)
				.getRepoStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition repoStatisticDefinition = it.next();
			RepoStatistic repoStatistic = (RepoStatistic) statisticBuilder
					.createStatistic(repoStatisticDefinition,
							commonStatisticParameterMap);
			commitStatisticMap.addRepoStatistic(repoStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((CommitEventSamplerDefinition) samplerDefinition)
				.getUserRepoStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition userRepoStatisticDefinition = it.next();
			UserRepoStatistic userRepoStatistic = (UserRepoStatistic) statisticBuilder
					.createStatistic(userRepoStatisticDefinition,
							commonStatisticParameterMap);
			commitStatisticMap.addUserRepoStatistic(userRepoStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((CommitEventSamplerDefinition) samplerDefinition)
				.getInteractionStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition interactionStatisticDefinition = it.next();
			commitStatisticMap.addInteractionStatistic(
					interactionStatisticDefinition.getName(),
					commitStatisticMap
							.getNamedStatistic(interactionStatisticDefinition
									.getInteractionStatistic1()),
					commitStatisticMap
							.getNamedStatistic(interactionStatisticDefinition
									.getInteractionStatistic2()));
		}

	}

	protected void finalizeStatistics() {
		int numOfStatistics = (commitStatisticMap.getUserStatistics() != null) ? commitStatisticMap
				.getUserStatistics().size() : 0;
		numOfStatistics += (commitStatisticMap.getRepoStatistics() != null) ? commitStatisticMap
				.getRepoStatistics().size() : 0;
		numOfStatistics += (commitStatisticMap.getUserRepoStatistics() != null) ? commitStatisticMap
				.getUserRepoStatistics().size() : 0;
		numOfStatistics += (commitStatisticMap.getInteractionStatistics() != null) ? commitStatisticMap
				.getInteractionStatistics().size() : 0;
		currentStatistics = new double[numOfStatistics];
	}

	@Override
	protected void writeCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {
		CommitEvent commitEvent = (CommitEvent) event;
		writeRelationalStatistics(commitEvent, commitEvent.getAuthorID(),
				commitEvent.getRepoID(), true, 1.0);
		for (Iterator<WeightedEdge> it = selectedEdges.iterator(); it.hasNext();) {
			WeightedEdge selectedEdge = it.next();
			writeRelationalStatistics(commitEvent, selectedEdge.getSenderID(),
					selectedEdge.getReceiverID(), false, 1.0);
		}
	}

	@Override
	protected void computeCurrentStatistics(Event event, int senderID,
			int receiverID, boolean interactionsIncluded) {

		HashMap<String, Double> statValues = new HashMap<String, Double>();

		int statIndex = 0;

		// Compute user statistics
		for (UserStatistic userStatistic : commitStatisticMap
				.getUserStatistics()) {
			double value = userStatistic.getValue(senderID, event);
			currentStatistics[statIndex++] = value;
			statValues.put(userStatistic.getName(), value);
		}

		// Compute repo statistics
		for (RepoStatistic repoStatistic : commitStatisticMap
				.getRepoStatistics()) {
			double value = repoStatistic.getValue(receiverID, event);
			currentStatistics[statIndex++] = value;
			statValues.put(repoStatistic.getName(), value);
		}

		// Compute edge statistics
		for (UserRepoStatistic userRepoStatistic : commitStatisticMap
				.getUserRepoStatistics()) {
			double value = userRepoStatistic.getValue(senderID, receiverID,
					event);
			currentStatistics[statIndex++] = value;
			statValues.put(userRepoStatistic.getName(), value);
		}

		// Compute interaction statistics
		if (interactionsIncluded)
			for (StatisticInteraction statisticInteraction : commitStatisticMap
					.getInteractionStatistics()) {
				double value1 = statValues.get(statisticInteraction.statistic1
						.getName());
				double value2 = statValues.get(statisticInteraction.statistic2
						.getName());
				currentStatistics[statIndex++] = value1 * value2;
			}

	}

	@Override
	public String getSASInputStatistics() {

		String inputStr = "";

		for (UserStatistic userStatistic : commitStatisticMap
				.getUserStatistics())
			inputStr += " " + userStatistic.getName() + " \n";
		for (RepoStatistic repoStatistic : commitStatisticMap
				.getRepoStatistics())
			inputStr += " " + repoStatistic.getName() + " \n";

		for (UserRepoStatistic userRepoStatistic : commitStatisticMap
				.getUserRepoStatistics())
			inputStr += " " + userRepoStatistic.getName() + " \n";

		inputStr += "; \n";

		return inputStr;
	}

	@Override
	public String getSASModelDescription() {

		String modelStr = "";

		for (UserStatistic userStatistic : commitStatisticMap
				.getUserStatistics())
			modelStr += " " + userStatistic.getName() + " \n";

		for (RepoStatistic repoStatistic : commitStatisticMap
				.getRepoStatistics())
			modelStr += " " + repoStatistic.getName() + " \n";

		for (UserRepoStatistic userRepoStatistic : commitStatisticMap
				.getUserRepoStatistics())
			modelStr += " " + userRepoStatistic.getName() + " \n";

		for (StatisticInteraction statisticInteraction : commitStatisticMap
				.getInteractionStatistics())
			modelStr += " " + statisticInteraction.getName() + " \n";

		modelStr += "; \n";

		for (StatisticInteraction statisticInteraction : commitStatisticMap
				.getInteractionStatistics())
			modelStr += " " + statisticInteraction.getName() + " = "
					+ statisticInteraction.statistic1.getName() + "*"
					+ statisticInteraction.statistic2.getName() + "; \n";

		return modelStr;
	}

}
