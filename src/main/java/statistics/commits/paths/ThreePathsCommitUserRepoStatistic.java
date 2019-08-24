/**
 * 
 */
package statistics.commits.paths;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import events.Event;
import statistics.commits.CommitUserRepoStatistic;
import statistics.queries.UniplexEventQuery;

/**
 * @author duyv
 * 
 */
public class ThreePathsCommitUserRepoStatistic extends CommitUserRepoStatistic {

	public ThreePathsCommitUserRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, int repoID, Event event) {
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty);
		return UniplexEventQuery.getThreePaths(userID, repoID,
				senderNodeProperty, indexedSenderEvents, receiverNodeProperty,
				indexedReceiverEvents);
	}
}
