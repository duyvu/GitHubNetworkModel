/**
 * 
 */
package statistics.followers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.RelationalUniplexNetworkStatistic;

/**
 * @author duyv
 * 
 */
public abstract class IssueCommentSenderReceiverStatistic extends
		RelationalUniplexNetworkStatistic implements SenderReceiverStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueCommentSenderReceiverStatistic.class.getName());

	public IssueCommentSenderReceiverStatistic() {
		senderNodeProperty = "userID";
		receiverNodeProperty = "issueID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped senderNodeProperty =  {}", senderNodeProperty);
		LOG.info(">> Mapped receiverNodeProperty =  {}", receiverNodeProperty);
	}

	@Override
	public double[] getValues(int senderID, int receiverID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(senderID, receiverID, event);
		return results;
	}

}
