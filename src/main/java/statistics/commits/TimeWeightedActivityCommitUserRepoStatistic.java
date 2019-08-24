/**
 * 
 */
package statistics.commits;

import java.util.ArrayList;

import events.Event;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class TimeWeightedActivityCommitUserRepoStatistic extends
		CommitUserRepoStatistic {

	protected double exponentialWeight = -1.0;

	public void setExponentialWeight(Double exponentialWeight) {
		this.exponentialWeight = exponentialWeight;
	}

	public void setExponentialWeight(double exponentialWeight) {
		this.exponentialWeight = exponentialWeight;
	}

	public TimeWeightedActivityCommitUserRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int repoID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty).get(userID);
		if (myEvents != null)
			return UniplexEventQuery.getTimeWeightedCount(UniplexEventQuery
					.filterEventsByProperty(myEvents, receiverNodeProperty,
							repoID), event.getTime(), exponentialWeight);
		else
			return 0;
	}

}
