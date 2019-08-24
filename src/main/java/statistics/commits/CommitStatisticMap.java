package statistics.commits;

import java.util.ArrayList;

import statistics.Statistic;
import statistics.StatisticInteraction;
import statistics.maps.InteractionStatisticMap;
import statistics.maps.RepoStatisticMap;
import statistics.maps.StatisticMap;
import statistics.maps.UserRepoStatisticMap;
import statistics.maps.UserStatisticMap;
import statistics.repos.RepoStatistic;
import statistics.users.UserStatistic;

public class CommitStatisticMap extends StatisticMap implements
		UserStatisticMap, RepoStatisticMap, UserRepoStatisticMap,
		InteractionStatisticMap {

	protected ArrayList<UserStatistic> userStatistics;
	protected ArrayList<RepoStatistic> repoStatistics;
	protected ArrayList<UserRepoStatistic> userRepoStatistics;
	protected ArrayList<StatisticInteraction> interactionStatistics;

	public CommitStatisticMap() {
		userStatistics = new ArrayList<UserStatistic>();
		repoStatistics = new ArrayList<RepoStatistic>();
		userRepoStatistics = new ArrayList<UserRepoStatistic>();
		interactionStatistics = new ArrayList<StatisticInteraction>();
	}

	public void addUserStatistic(UserStatistic userStatistic) {
		userStatistics.add(userStatistic);
		addNamedStatistic(userStatistic);
	}

	public void addRepoStatistic(RepoStatistic repoStatistic) {
		repoStatistics.add(repoStatistic);
		addNamedStatistic(repoStatistic);
	}

	public void addUserRepoStatistic(UserRepoStatistic userRepoStatistic) {
		userRepoStatistics.add(userRepoStatistic);
		addNamedStatistic(userRepoStatistic);
	}

	public void addInteractionStatistic(String name, Statistic statistic1,
			Statistic statistic2) {
		StatisticInteraction statisticInteraction = new StatisticInteraction(
				name, statistic1, statistic2);
		interactionStatistics.add(statisticInteraction);
		addNamedStatistic(statisticInteraction);
	}

	public ArrayList<UserStatistic> getUserStatistics() {
		return userStatistics;
	}

	public ArrayList<RepoStatistic> getRepoStatistics() {
		return repoStatistics;
	}

	public ArrayList<UserRepoStatistic> getUserRepoStatistics() {
		return userRepoStatistics;
	}

	public ArrayList<StatisticInteraction> getInteractionStatistics() {
		return interactionStatistics;
	}
}
