package statistics.maps;

import java.util.ArrayList;

import statistics.repos.RepoStatistic;

public interface RepoStatisticMap {

	public void addRepoStatistic(RepoStatistic repoStatistic);
	
	public ArrayList<RepoStatistic> getRepoStatistics();

}
