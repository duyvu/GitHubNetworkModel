/**
 * 
 */
package statistics.issues.comments;

import java.util.ArrayList;

import statistics.queries.UniplexEventQuery;
import events.Event;

/**
 * @author duyvu
 * 
 */
public class DegreeIssueCommentIssueStatistic extends
		RelationalIssueCommentIssueStatistic {

	/**
	 * 
	 */
	public DegreeIssueCommentIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int issueID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(receiverNodeProperty).get(issueID);
		if (myEvents != null)
			return UniplexEventQuery.getUniquePropertyValues(myEvents,
					senderNodeProperty).size();
		else
			return 0;
	}

}
