package statistics.maps;

import java.util.ArrayList;

import statistics.commits.UserRepoStatistic;

public interface UserRepoStatisticMap {

	public void addUserRepoStatistic(UserRepoStatistic userRepoStatistic);

	public ArrayList<UserRepoStatistic> getUserRepoStatistics();

}
