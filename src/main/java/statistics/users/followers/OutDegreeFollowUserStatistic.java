/**
 * 
 */
package statistics.users.followers;

import java.util.ArrayList;

import statistics.queries.UniplexEventQuery;
import events.Event;

/**
 * @author duyvu
 * 
 */
public class OutDegreeFollowUserStatistic extends RelationalFollowUserStatistic {

	/**
	 * 
	 */
	public OutDegreeFollowUserStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(senderNodeProperty).get(userID);
		if (myEvents != null)
			return UniplexEventQuery.getUniquePropertyValues(myEvents,
					receiverNodeProperty).size();
		else
			return 0;
	}
}
