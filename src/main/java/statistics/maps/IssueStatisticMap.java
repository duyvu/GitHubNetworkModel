package statistics.maps;

import java.util.ArrayList;

import statistics.issues.comments.IssueStatistic;

public interface IssueStatisticMap {

	public void addIssueStatistic(IssueStatistic issueStatistic);
	
	public ArrayList<IssueStatistic> getIssueStatistics();

}
