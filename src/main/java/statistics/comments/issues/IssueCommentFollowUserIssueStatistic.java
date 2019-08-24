/**
 * 
 */
package statistics.comments.issues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalBiplexNetworkStatistic;
import statistics.comments.UserIssueStatistic;

/**
 * @author duyv
 * 
 */
public abstract class IssueCommentFollowUserIssueStatistic extends
		RelationalBiplexNetworkStatistic implements UserIssueStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueCommentFollowUserIssueStatistic.class.getName());

	public IssueCommentFollowUserIssueStatistic() {
		primarySenderNodeProperty = "userID";
		primaryReceiverNodeProperty = "issueID";

		secondarySenderNodeProperty = "senderID";
		secondaryReceiverNodeProperty = "receiverID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped primarySenderNodeProperty =  {}",
				primarySenderNodeProperty);
		LOG.info(">> Mapped primaryReceiverNodeProperty =  {}",
				primaryReceiverNodeProperty);
		LOG.info(">> Mapped secondarySenderNodeProperty =  {}",
				secondarySenderNodeProperty);
		LOG.info(">> Mapped secondaryReceiverNodeProperty =  {}",
				secondaryReceiverNodeProperty);
	}

	@Override
	public double[] getValues(int userID, int repoID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, repoID, event);
		return results;
	}

}
