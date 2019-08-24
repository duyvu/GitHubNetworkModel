/**
 * 
 */
package statistics.comments.issues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalUniplexNetworkStatistic;
import statistics.comments.UserIssueStatistic;

/**
 * @author duyv
 * 
 */
public abstract class IssueCommentUserIssueStatistic extends
		RelationalUniplexNetworkStatistic implements UserIssueStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueCommentUserIssueStatistic.class.getName());

	public IssueCommentUserIssueStatistic() {
		senderNodeProperty = "userID";
		receiverNodeProperty = "issueID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped senderNodeProperty =  {}", senderNodeProperty);
		LOG.info(">> Mapped receiverNodeProperty =  {}", receiverNodeProperty);
	}

	@Override
	public double[] getValues(int userID, int repoID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, repoID, event);
		return results;
	}

}
