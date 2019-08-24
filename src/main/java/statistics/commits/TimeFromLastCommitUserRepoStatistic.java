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
public class TimeFromLastCommitUserRepoStatistic extends
		CommitUserRepoStatistic {

	public TimeFromLastCommitUserRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int repoID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty).get(userID);
		double lastEventTime;

		if (myEvents != null)
			lastEventTime = UniplexEventQuery.getRecentEventByProperty(
					myEvents, receiverNodeProperty, repoID).time;
		else
			lastEventTime = 0;
		return (event.time - lastEventTime);
	}

}
