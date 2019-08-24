package statistics.comments.issues;

import java.util.ArrayList;

import statistics.Statistic;
import statistics.StatisticInteraction;
import statistics.issues.comments.IssueStatistic;
import statistics.maps.InteractionStatisticMap;
import statistics.maps.IssueStatisticMap;
import statistics.maps.StatisticMap;
import statistics.maps.UserIssueStatisticMap;
import statistics.maps.UserStatisticMap;
import statistics.comments.UserIssueStatistic;
import statistics.users.UserStatistic;

public class IssueCommentStatisticMap extends StatisticMap implements
		UserStatisticMap, IssueStatisticMap, UserIssueStatisticMap,
		InteractionStatisticMap {

	protected ArrayList<UserStatistic> userStatistics;
	protected ArrayList<IssueStatistic> issueStatistics;
	protected ArrayList<UserIssueStatistic> userIssueStatistics;
	protected ArrayList<StatisticInteraction> interactionStatistics;

	public IssueCommentStatisticMap() {
		userStatistics = new ArrayList<UserStatistic>();
		issueStatistics = new ArrayList<IssueStatistic>();
		userIssueStatistics = new ArrayList<UserIssueStatistic>();
		interactionStatistics = new ArrayList<StatisticInteraction>();
	}

	public void addUserStatistic(UserStatistic userStatistic) {
		userStatistics.add(userStatistic);
		addNamedStatistic(userStatistic);
	}

	public void addIssueStatistic(IssueStatistic issueStatistic) {
		issueStatistics.add(issueStatistic);
		addNamedStatistic(issueStatistic);
	}

	public void addUserIssueStatistic(UserIssueStatistic userIssueStatistic) {
		userIssueStatistics.add(userIssueStatistic);
		addNamedStatistic(userIssueStatistic);
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

	public ArrayList<IssueStatistic> getIssueStatistics() {
		return issueStatistics;
	}

	public ArrayList<UserIssueStatistic> getUserIssueStatistics() {
		return userIssueStatistics;
	}

	public ArrayList<StatisticInteraction> getInteractionStatistics() {
		return interactionStatistics;
	}

}
