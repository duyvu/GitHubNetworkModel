/**
 * 
 */
package statistics.repos.commits.paths;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import statistics.queries.UniplexEventQuery;
import statistics.repos.commits.RelationalCommitRepoStatistic;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class TwoPathsCommitRepoStatistic extends RelationalCommitRepoStatistic {
	/**
	 * 
	 */
	public TwoPathsCommitRepoStatistic() {
		super();
	}

	@Override
	public double getValue(int repoID, Event event) {
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedSenderEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty);
		ConcurrentHashMap<Integer, ArrayList<Event>> indexedReceiverEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty);
		return UniplexEventQuery.getTwoPaths(repoID, receiverNodeProperty,
				indexedReceiverEvents, senderNodeProperty, indexedSenderEvents);
	}
}
