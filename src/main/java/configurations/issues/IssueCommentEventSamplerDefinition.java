package configurations.issues;

import java.util.ArrayList;
import java.util.Iterator;

import configurations.SamplerDefinition;
import configurations.StatisticDefinition;

public class IssueCommentEventSamplerDefinition extends SamplerDefinition {

	protected ArrayList<StatisticDefinition> userStatisticDefinitions;
	protected ArrayList<StatisticDefinition> issueStatisticDefinitions;
	protected ArrayList<StatisticDefinition> userIssueStatisticDefinitions;
	protected ArrayList<StatisticDefinition> interactionStatisticDefinitions;

	public IssueCommentEventSamplerDefinition() {
		createStatisticsLists();
	}

	public IssueCommentEventSamplerDefinition(String samplerDefinitionFile) {
		createStatisticsLists();
		readModelDescription(samplerDefinitionFile);
	}

	protected void createStatisticsLists() {
		userStatisticDefinitions = new ArrayList<StatisticDefinition>();
		issueStatisticDefinitions = new ArrayList<StatisticDefinition>();
		userIssueStatisticDefinitions = new ArrayList<StatisticDefinition>();
		interactionStatisticDefinitions = new ArrayList<StatisticDefinition>();
	}

	@Override
	public StatisticDefinition createStatisticDefinition() {
		return new StatisticDefinition();
	}

	@Override
	protected void addStatisticDefinition(
			StatisticDefinition statisticDefinition) {
		if (statisticDefinition.getType().equalsIgnoreCase("User"))
			userStatisticDefinitions.add(statisticDefinition);
		else if (statisticDefinition.getType().equalsIgnoreCase("Issue"))
			issueStatisticDefinitions.add(statisticDefinition);
		else if (statisticDefinition.getType().equalsIgnoreCase("Edge"))
			userIssueStatisticDefinitions.add(statisticDefinition);
		else if (statisticDefinition.getType().equalsIgnoreCase("Interaction"))
			interactionStatisticDefinitions.add(statisticDefinition);
	}

	public String toString() {

		StringBuilder results = new StringBuilder();

		results.append("---------------------------------------------" + "\n");
		results.append("User Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = userStatisticDefinitions
				.iterator(); it.hasNext();) {
			StatisticDefinition statDesc = it.next();
			results.append(statDesc.toString() + "\n");
		}

		results.append("---------------------------------------------" + "\n");
		results.append("Issue Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = issueStatisticDefinitions
				.iterator(); it.hasNext();) {
			StatisticDefinition statDesc = it.next();
			results.append(statDesc.toString() + "\n");
		}

		results.append("---------------------------------------------" + "\n");
		results.append("Edge Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = userIssueStatisticDefinitions
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

	public ArrayList<StatisticDefinition> getUserStatisticDefinitions() {
		return userStatisticDefinitions;
	}

	public void setUserStatisticDefinitions(
			ArrayList<StatisticDefinition> userStatisticDefinitions) {
		this.userStatisticDefinitions = userStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getIssueStatisticDefinitions() {
		return issueStatisticDefinitions;
	}

	public void setIssueStatisticDefinitions(
			ArrayList<StatisticDefinition> issueStatisticDefinitions) {
		this.issueStatisticDefinitions = issueStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getUserIssueStatisticDefinitions() {
		return userIssueStatisticDefinitions;
	}

	public void setUserIssueStatisticDefinitions(
			ArrayList<StatisticDefinition> edgeStatisticDefinitions) {
		this.userIssueStatisticDefinitions = edgeStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getInteractionStatisticDefinitions() {
		return interactionStatisticDefinitions;
	}

	public void setInteractionStatisticDefinitions(
			ArrayList<StatisticDefinition> interactionStatisticDefinitions) {
		this.interactionStatisticDefinitions = interactionStatisticDefinitions;
	}

}
