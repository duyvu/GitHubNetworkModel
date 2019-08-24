/**
 * 
 */
package statistics.users.pullRequests;

import java.util.ArrayList;

import statistics.queries.UniplexEventQuery;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class TimeWeightedActivityPullRequestUserStatistic extends
		EgocentricPullRequestUserStatistic {

	protected double exponentialWeight = -1.0;

	public void setExponentialWeight(Double exponentialWeight) {
		this.exponentialWeight = exponentialWeight;
	}

	public void setExponentialWeight(double exponentialWeight) {
		this.exponentialWeight = exponentialWeight;
	}

	public TimeWeightedActivityPullRequestUserStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, Event event) {

		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(userID);
		if (myEvents != null)
			return UniplexEventQuery.getTimeWeightedCount(myEvents,
					event.getTime(), exponentialWeight);
		else
			return 0;

	}
}