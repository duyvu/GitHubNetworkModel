package configurations.followers;

import java.util.ArrayList;
import java.util.Iterator;

import configurations.SamplerDefinition;
import configurations.StatisticDefinition;

public class FollowEventSamplerDefinition extends SamplerDefinition {

	protected ArrayList<StatisticDefinition> senderStatisticDefinitions;
	protected ArrayList<StatisticDefinition> receiverStatisticDefinitions;
	protected ArrayList<StatisticDefinition> senderReceiverStatisticDefinitions;
	protected ArrayList<StatisticDefinition> interactionStatisticDefinitions;

	public FollowEventSamplerDefinition() {
		createStatisticsLists();
	}

	public FollowEventSamplerDefinition(String samplerDefinitionFile) {
		createStatisticsLists();
		readModelDescription(samplerDefinitionFile);
	}

	protected void createStatisticsLists() {
		senderStatisticDefinitions = new ArrayList<StatisticDefinition>();
		receiverStatisticDefinitions = new ArrayList<StatisticDefinition>();
		senderReceiverStatisticDefinitions = new ArrayList<StatisticDefinition>();
		interactionStatisticDefinitions = new ArrayList<StatisticDefinition>();
	}

	@Override
	public StatisticDefinition createStatisticDefinition() {
		return new StatisticDefinition();
	}

	@Override
	protected void addStatisticDefinition(
			StatisticDefinition statisticDefinition) {
		if (statisticDefinition.getType().equalsIgnoreCase("Sender"))
			senderStatisticDefinitions.add(statisticDefinition);
		else if (statisticDefinition.getType().equalsIgnoreCase("Receiver"))
			receiverStatisticDefinitions.add(statisticDefinition);
		else if (statisticDefinition.getType().equalsIgnoreCase("Edge")
				|| statisticDefinition.getType().equalsIgnoreCase(
						"SenderReceiver"))
			senderReceiverStatisticDefinitions.add(statisticDefinition);
		else if (statisticDefinition.getType().equalsIgnoreCase("Interaction"))
			interactionStatisticDefinitions.add(statisticDefinition);
	}

	public String toString() {

		StringBuilder results = new StringBuilder();

		results.append("---------------------------------------------" + "\n");
		results.append("Sender Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = senderStatisticDefinitions
				.iterator(); it.hasNext();) {
			StatisticDefinition statDesc = it.next();
			results.append(statDesc.toString() + "\n");
		}

		results.append("---------------------------------------------" + "\n");
		results.append("Receiver Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = receiverStatisticDefinitions
				.iterator(); it.hasNext();) {
			StatisticDefinition statDesc = it.next();
			results.append(statDesc.toString() + "\n");
		}

		results.append("---------------------------------------------" + "\n");
		results.append("Edge Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = senderReceiverStatisticDefinitions
				.iterator(); it.hasNext();) {
			StatisticDefinition statDesc = it.next();
			results.append(statDesc.toString() + "\n");
		}

		results.append("---------------------------------------------" + "\n");
		results.append("Interaction Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = interactionStatisticDefinitions
				.iterator(); it.hasNext();) {
			StatisticDefinition statDesc = it.next();
			results.append(statDesc.toString() + "\n");
		}

		return results.toString();
	}

	public ArrayList<StatisticDefinition> getSenderStatisticDefinitions() {
		return senderStatisticDefinitions;
	}

	public void setSenderStatisticDefinitions(
			ArrayList<StatisticDefinition> senderStatisticDefinitions) {
		this.senderStatisticDefinitions = senderStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getReceiverStatisticDefinitions() {
		return receiverStatisticDefinitions;
	}

	public void setReceiverStatisticDefinitions(
			ArrayList<StatisticDefinition> receiverStatisticDefinitions) {
		this.receiverStatisticDefinitions = receiverStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getSenderReceiverStatisticDefinitions() {
		return senderReceiverStatisticDefinitions;
	}

	public void setSenderReceiverStatisticDefinitions(
			ArrayList<StatisticDefinition> senderReceiverStatisticDefinitions) {
		this.senderReceiverStatisticDefinitions = senderReceiverStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getInteractionStatisticDefinitions() {
		return interactionStatisticDefinitions;
	}

	public void setInteractionStatisticDefinitions(
			ArrayList<StatisticDefinition> interactionStatisticDefinitions) {
		this.interactionStatisticDefinitions = interactionStatisticDefinitions;
	}

}
