package statistics.maps;

import java.util.ArrayList;

import statistics.comments.UserIssueStatistic;

public interface UserIssueStatisticMap {

	public void addUserIssueStatistic(UserIssueStatistic userIssueStatistic);

	public ArrayList<UserIssueStatistic> getUserIssueStatistics();

}
