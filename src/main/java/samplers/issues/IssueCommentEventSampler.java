/**
 * 
 */
package samplers.issues;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import configurations.StatisticDefinition;
import configurations.issues.IssueCommentEventSamplerDefinition;

import events.Event;
import events.issues.IssueCommentEvent;
import graphs.elements.WeightedEdge;
import samplers.BaseSampler;
import statistics.StatisticInteraction;
import statistics.builders.StatisticBuilder;
import statistics.comments.UserIssueStatistic;
import statistics.comments.issues.IssueCommentStatisticMap;
import statistics.issues.comments.IssueStatistic;
import statistics.users.UserStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class IssueCommentEventSampler extends BaseSampler {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueCommentEventSampler.class.getName());

	protected IssueCommentStatisticMap issueCommentStatisticMap;

	/**
	 * 
	 */
	public IssueCommentEventSampler() {
	}

	@Override
	public void setSamplerDefinition(String samplerDefinitionFile,
			StatisticBuilder statisticBuilder) {

		samplerDefinition = new IssueCommentEventSamplerDefinition(
				samplerDefinitionFile);

		LOG.info(samplerDefinition.toString());

		issueCommentStatisticMap = new IssueCommentStatisticMap();

		constructStatistics(statisticBuilder);

		finalizeStatistics();
	}

	protected void constructStatistics(StatisticBuilder statisticBuilder) {

		for (Iterator<StatisticDefinition> it = ((IssueCommentEventSamplerDefinition) samplerDefinition)
				.getUserStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition userStatisticDefinition = it.next();
			UserStatistic userStatistic = (UserStatistic) statisticBuilder
					.createStatistic(userStatisticDefinition,
							commonStatisticParameterMap);
			issueCommentStatisticMap.addUserStatistic(userStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((IssueCommentEventSamplerDefinition) samplerDefinition)
				.getIssueStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition issueStatisticDefinition = it.next();
			IssueStatistic issueStatistic = (IssueStatistic) statisticBuilder
					.createStatistic(issueStatisticDefinition,
							commonStatisticParameterMap);
			issueCommentStatisticMap.addIssueStatistic(issueStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((IssueCommentEventSamplerDefinition) samplerDefinition)
				.getUserIssueStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition userIssueStatisticDefinition = it.next();
			UserIssueStatistic userIssueStatistic = (UserIssueStatistic) statisticBuilder
					.createStatistic(userIssueStatisticDefinition,
							commonStatisticParameterMap);
			issueCommentStatisticMap.addUserIssueStatistic(userIssueStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((IssueCommentEventSamplerDefinition) samplerDefinition)
				.getInteractionStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition interactionStatisticDefinition = it.next();
			issueCommentStatisticMap.addInteractionStatistic(
					interactionStatisticDefinition.getName(),
					issueCommentStatisticMap
							.getNamedStatistic(interactionStatisticDefinition
									.getInteractionStatistic1()),
					issueCommentStatisticMap
							.getNamedStatistic(interactionStatisticDefinition
									.getInteractionStatistic2()));
		}

	}

	protected void finalizeStatistics() {
		int numOfStatistics = (issueCommentStatisticMap.getUserStatistics() != null) ? issueCommentStatisticMap
				.getUserStatistics().size() : 0;
		numOfStatistics += (issueCommentStatisticMap.getIssueStatistics() != null) ? issueCommentStatisticMap
				.getIssueStatistics().size() : 0;
		numOfStatistics += (issueCommentStatisticMap.getUserIssueStatistics() != null) ? issueCommentStatisticMap
				.getUserIssueStatistics().size() : 0;
		numOfStatistics += (issueCommentStatisticMap.getInteractionStatistics() != null) ? issueCommentStatisticMap
				.getInteractionStatistics().size() : 0;
		currentStatistics = new double[numOfStatistics];
	}

	@Override
	protected void writeCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {
		IssueCommentEvent issueCommentEvent = (IssueCommentEvent) event;
		writeRelationalStatistics(issueCommentEvent,
				issueCommentEvent.getUserID(), issueCommentEvent.getIssueID(),
				true, 1.0);
		for (Iterator<WeightedEdge> it = selectedEdges.iterator(); it.hasNext();) {
			WeightedEdge selectedEdge = it.next();
			writeRelationalStatistics(issueCommentEvent,
					selectedEdge.getSenderID(), selectedEdge.getReceiverID(),
					false, 1.0);
		}
	}

	@Override
	protected void computeCurrentStatistics(Event event, int senderID,
			int receiverID, boolean interactionsIncluded) {

		HashMap<String, Double> statValues = new HashMap<String, Double>();

		int statIndex = 0;

		// Compute user statistics
		for (UserStatistic userStatistic : issueCommentStatisticMap
				.getUserStatistics()) {
			double value = userStatistic.getValue(senderID, event);
			currentStatistics[statIndex++] = value;
			statValues.put(userStatistic.getName(), value);
		}

		// Compute issue statistics
		for (IssueStatistic issueStatistic : issueCommentStatisticMap
				.getIssueStatistics()) {
			double value = issueStatistic.getValue(receiverID, event);
			currentStatistics[statIndex++] = value;
			statValues.put(issueStatistic.getName(), value);
		}

		// Compute edge statistics
		for (UserIssueStatistic userIssueStatistic : issueCommentStatisticMap
				.getUserIssueStatistics()) {
			double value = userIssueStatistic.getValue(senderID, receiverID,
					event);
			currentStatistics[statIndex++] = value;
			statValues.put(userIssueStatistic.getName(), value);
		}

		// Compute interaction statistics
		if (interactionsIncluded)
			for (StatisticInteraction statisticInteraction : issueCommentStatisticMap
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

		for (UserStatistic userStatistic : issueCommentStatisticMap
				.getUserStatistics())
			inputStr += " " + userStatistic.getName() + " \n";
		for (IssueStatistic issueStatistic : issueCommentStatisticMap
				.getIssueStatistics())
			inputStr += " " + issueStatistic.getName() + " \n";

		for (UserIssueStatistic userIssueStatistic : issueCommentStatisticMap
				.getUserIssueStatistics())
			inputStr += " " + userIssueStatistic.getName() + " \n";

		inputStr += "; \n";

		return inputStr;
	}

	@Override
	public String getSASModelDescription() {

		String modelStr = "";

		for (UserStatistic userStatistic : issueCommentStatisticMap
				.getUserStatistics())
			modelStr += " " + userStatistic.getName() + " \n";

		for (IssueStatistic issueStatistic : issueCommentStatisticMap
				.getIssueStatistics())
			modelStr += " " + issueStatistic.getName() + " \n";

		for (UserIssueStatistic userIssueStatistic : issueCommentStatisticMap
				.getUserIssueStatistics())
			modelStr += " " + userIssueStatistic.getName() + " \n";

		for (StatisticInteraction statisticInteraction : issueCommentStatisticMap
				.getInteractionStatistics())
			modelStr += " " + statisticInteraction.getName() + " \n";

		modelStr += "; \n";

		for (StatisticInteraction statisticInteraction : issueCommentStatisticMap
				.getInteractionStatistics())
			modelStr += " " + statisticInteraction.getName() + " = "
					+ statisticInteraction.statistic1.getName() + "*"
					+ statisticInteraction.statistic2.getName() + "; \n";

		return modelStr;
	}

}
