/**
 * 
 */
package samplers.followers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import configurations.StatisticDefinition;
import configurations.followers.FollowEventSamplerDefinition;

import events.Event;
import events.users.FollowEvent;
import graphs.elements.WeightedEdge;
import samplers.BaseSampler;
import statistics.StatisticInteraction;
import statistics.builders.StatisticBuilder;
import statistics.followers.FollowStatisticMap;
import statistics.followers.SenderReceiverStatistic;
import statistics.users.UserStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class FollowEventSampler extends BaseSampler {

	private static final Logger LOG = LoggerFactory
			.getLogger(FollowEventSampler.class.getName());

	protected FollowStatisticMap followStatisticMap;

	/**
	 * 
	 */
	public FollowEventSampler() {
	}

	@Override
	public void setSamplerDefinition(String samplerDefinitionFile,
			StatisticBuilder statisticBuilder) {

		samplerDefinition = new FollowEventSamplerDefinition(
				samplerDefinitionFile);

		LOG.info(samplerDefinition.toString());

		followStatisticMap = new FollowStatisticMap();

		constructStatistics(statisticBuilder);

		finalizeStatistics();
	}

	protected void constructStatistics(StatisticBuilder statisticBuilder) {

		for (Iterator<StatisticDefinition> it = ((FollowEventSamplerDefinition) samplerDefinition)
				.getSenderStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition senderStatisticDefinition = it.next();
			UserStatistic userStatistic = (UserStatistic) statisticBuilder
					.createStatistic(senderStatisticDefinition,
							commonStatisticParameterMap);
			followStatisticMap.addSenderStatistic(userStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((FollowEventSamplerDefinition) samplerDefinition)
				.getReceiverStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition receiverStatisticDefinition = it.next();
			UserStatistic repoStatistic = (UserStatistic) statisticBuilder
					.createStatistic(receiverStatisticDefinition,
							commonStatisticParameterMap);
			followStatisticMap.addReceiverStatistic(repoStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((FollowEventSamplerDefinition) samplerDefinition)
				.getSenderReceiverStatisticDefinitions().iterator(); it
				.hasNext();) {
			StatisticDefinition senderReceiverStatisticDefinition = it.next();
			SenderReceiverStatistic senderReceiverStatistic = (SenderReceiverStatistic) statisticBuilder
					.createStatistic(senderReceiverStatisticDefinition,
							commonStatisticParameterMap);
			followStatisticMap
					.addSenderReceiverStatistic(senderReceiverStatistic);
		}

		for (Iterator<StatisticDefinition> it = ((FollowEventSamplerDefinition) samplerDefinition)
				.getInteractionStatisticDefinitions().iterator(); it.hasNext();) {
			StatisticDefinition interactionStatisticDefinition = it.next();
			followStatisticMap.addInteractionStatistic(
					interactionStatisticDefinition.getName(),
					followStatisticMap
							.getNamedStatistic(interactionStatisticDefinition
									.getInteractionStatistic1()),
					followStatisticMap
							.getNamedStatistic(interactionStatisticDefinition
									.getInteractionStatistic2()));
		}

	}

	protected void finalizeStatistics() {
		int numOfStatistics = (followStatisticMap.getSenderStatistics() != null) ? followStatisticMap
				.getSenderStatistics().size() : 0;
		numOfStatistics += (followStatisticMap.getReceiverStatistics() != null) ? followStatisticMap
				.getReceiverStatistics().size() : 0;
		numOfStatistics += (followStatisticMap.getSenderReceiverStatistics() != null) ? followStatisticMap
				.getSenderReceiverStatistics().size() : 0;
		numOfStatistics += (followStatisticMap.getInteractionStatistics() != null) ? followStatisticMap
				.getInteractionStatistics().size() : 0;
		currentStatistics = new double[numOfStatistics];
	}

	@Override
	protected void writeCaseControlSet(Event event,
			TreeSet<WeightedEdge> selectedEdges) {
		FollowEvent commitEvent = (FollowEvent) event;
		writeRelationalStatistics(commitEvent, commitEvent.getSenderID(),
				commitEvent.getReceiverID(), true, 1.0);
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
		for (UserStatistic senderStatistic : followStatisticMap
				.getSenderStatistics()) {
			double value = senderStatistic.getValue(senderID, event);
			currentStatistics[statIndex++] = value;
			statValues.put(senderStatistic.getName(), value);
		}

		// Compute repo statistics
		for (UserStatistic receiverStatistic : followStatisticMap
				.getReceiverStatistics()) {
			double value = receiverStatistic.getValue(receiverID, event);
			currentStatistics[statIndex++] = value;
			statValues.put(receiverStatistic.getName(), value);
		}

		// Compute edge statistics
		for (SenderReceiverStatistic senderReceiverStatistic : followStatisticMap
				.getSenderReceiverStatistics()) {
			double value = senderReceiverStatistic.getValue(senderID,
					receiverID, event);
			currentStatistics[statIndex++] = value;
			statValues.put(senderReceiverStatistic.getName(), value);
		}

		// Compute interaction statistics
		if (interactionsIncluded)
			for (StatisticInteraction statisticInteraction : followStatisticMap
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

		for (UserStatistic senderStatistic : followStatisticMap
				.getSenderStatistics())
			inputStr += " " + senderStatistic.getName() + " \n";

		for (UserStatistic receiverStatistic : followStatisticMap
				.getReceiverStatistics())
			inputStr += " " + receiverStatistic.getName() + " \n";

		for (SenderReceiverStatistic senderReceiverStatistic : followStatisticMap
				.getSenderReceiverStatistics())
			inputStr += " " + senderReceiverStatistic.getName() + " \n";

		inputStr += "; \n";

		return inputStr;
	}

	@Override
	public String getSASModelDescription() {

		String modelStr = "";

		for (UserStatistic senderStatistic : followStatisticMap
				.getSenderStatistics())
			modelStr += " " + senderStatistic.getName() + " \n";

		for (UserStatistic receiverStatistic : followStatisticMap
				.getReceiverStatistics())
			modelStr += " " + receiverStatistic.getName() + " \n";

		for (SenderReceiverStatistic senderReceiverStatistic : followStatisticMap
				.getSenderReceiverStatistics())
			modelStr += " " + senderReceiverStatistic.getName() + " \n";

		for (StatisticInteraction statisticInteraction : followStatisticMap
				.getInteractionStatistics())
			modelStr += " " + statisticInteraction.getName() + " \n";

		modelStr += "; \n";

		for (StatisticInteraction statisticInteraction : followStatisticMap
				.getInteractionStatistics())
			modelStr += " " + statisticInteraction.getName() + " = "
					+ statisticInteraction.statistic1.getName() + "*"
					+ statisticInteraction.statistic2.getName() + "; \n";

		return modelStr;
	}

}
