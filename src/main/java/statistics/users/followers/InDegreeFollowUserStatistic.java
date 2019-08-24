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
public class InDegreeFollowUserStatistic extends RelationalFollowUserStatistic {

	/**
	 * 
	 */
	public InDegreeFollowUserStatistic() {
		super();
	}

	@Override
	public double getValue(int userID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty).get(userID);
		if (myEvents != null)
			return UniplexEventQuery.getUniquePropertyValues(myEvents,
					senderNodeProperty).size();
		else
			return 0;
	}
}
