/**
 * 
 */
package statistics.issues.comments;

import java.util.ArrayList;

import events.Event;

/**
 * @author duyvu
 * 
 */
public class ActivityIssueCommentIssueStatistic extends
		EgocentricIssueCommentIssueStatistic {

	/**
	 * 
	 */
	public ActivityIssueCommentIssueStatistic() {
		super();
	}

	@Override
	public double getValue(int issueID, Event event) {
		ArrayList<Event> myEvents = indexedRelationalEventStream
				.getPropertyMap(nodeProperty).get(issueID);
		if (myEvents != null)
			return myEvents.size();
		else
			return 0;
	}

}
