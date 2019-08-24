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
public class ActivityCommitUserRepoStatistic extends CommitUserRepoStatistic {

	public ActivityCommitUserRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int repoID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty).get(userID);
		if (myEvents != null)
			return UniplexEventQuery.filterEventsByProperty(myEvents,
					receiverNodeProperty, repoID).size();
		else
			return 0;
	}

}
