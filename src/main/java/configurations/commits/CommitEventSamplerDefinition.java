package configurations.commits;

import java.util.ArrayList;
import java.util.Iterator;

import configurations.SamplerDefinition;
import configurations.StatisticDefinition;

public class CommitEventSamplerDefinition extends SamplerDefinition {

	protected ArrayList<StatisticDefinition> userStatisticDefinitions;
	protected ArrayList<StatisticDefinition> repoStatisticDefinitions;
	protected ArrayList<StatisticDefinition> userRepoStatisticDefinitions;
	protected ArrayList<StatisticDefinition> interactionStatisticDefinitions;

	public CommitEventSamplerDefinition() {
		createStatisticsLists();
	}

	public CommitEventSamplerDefinition(String samplerDefinitionFile) {
		createStatisticsLists();
		readModelDescription(samplerDefinitionFile);
	}

	protected void createStatisticsLists() {
		userStatisticDefinitions = new ArrayList<StatisticDefinition>();
		repoStatisticDefinitions = new ArrayList<StatisticDefinition>();
		userRepoStatisticDefinitions = new ArrayList<StatisticDefinition>();
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
		else if (statisticDefinition.getType().equalsIgnoreCase("Repo")
				|| statisticDefinition.getType().equalsIgnoreCase("Project"))
			repoStatisticDefinitions.add(statisticDefinition);
		else if (statisticDefinition.getType().equalsIgnoreCase("Edge"))
			userRepoStatisticDefinitions.add(statisticDefinition);
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
		results.append("Repo Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = repoStatisticDefinitions
				.iterator(); it.hasNext();) {
			StatisticDefinition statDesc = it.next();
			results.append(statDesc.toString() + "\n");
		}

		results.append("---------------------------------------------" + "\n");
		results.append("Edge Statistics" + "\n");
		results.append("---------------------------------------------" + "\n");

		for (Iterator<StatisticDefinition> it = userRepoStatisticDefinitions
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

	public ArrayList<StatisticDefinition> getRepoStatisticDefinitions() {
		return repoStatisticDefinitions;
	}

	public void setRepoStatisticDefinitions(
			ArrayList<StatisticDefinition> repoStatisticDefinitions) {
		this.repoStatisticDefinitions = repoStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getUserRepoStatisticDefinitions() {
		return userRepoStatisticDefinitions;
	}

	public void setUserRepoStatisticDefinitions(
			ArrayList<StatisticDefinition> edgeStatisticDefinitions) {
		this.userRepoStatisticDefinitions = edgeStatisticDefinitions;
	}

	public ArrayList<StatisticDefinition> getInteractionStatisticDefinitions() {
		return interactionStatisticDefinitions;
	}

	public void setInteractionStatisticDefinitions(
			ArrayList<StatisticDefinition> interactionStatisticDefinitions) {
		this.interactionStatisticDefinitions = interactionStatisticDefinitions;
	}

}
