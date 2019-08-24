/**
 * 
 */
package statistics.users.issues;

import java.util.ArrayList;

import statistics.queries.UniplexEventQuery;
import events.Event;

/**
 * @author duyvu
 * 
 */
public class DegreeIssueCommentProjectLevelUserStatistic extends
		RelationalIssueCommentUserStatistic {

	/**
	 * 
	 */
	public DegreeIssueCommentProjectLevelUserStatistic() {
		super();
		receiverNodeProperty = "repoID";
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
