/**
 * 
 */
package statistics.issues.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalUniplexNetworkStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class RelationalIssueCommentIssueStatistic extends
		RelationalUniplexNetworkStatistic implements IssueStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(RelationalIssueCommentIssueStatistic.class.getName());

	/**
	 * 
	 */
	public RelationalIssueCommentIssueStatistic() {
		senderNodeProperty = "userID";
		receiverNodeProperty = "issueID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped senderNodeProperty =  {}", senderNodeProperty);
		LOG.info(">> Mapped receiverNodeProperty =  {}", receiverNodeProperty);
	}

	@Override
	public double[] getValues(int userID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, event);
		return results;
	}

}
