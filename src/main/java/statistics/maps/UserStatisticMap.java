package statistics.maps;

import java.util.ArrayList;

import statistics.users.UserStatistic;

public interface UserStatisticMap {

	public void addUserStatistic(UserStatistic userStatistic);

	public ArrayList<UserStatistic> getUserStatistics();

}
