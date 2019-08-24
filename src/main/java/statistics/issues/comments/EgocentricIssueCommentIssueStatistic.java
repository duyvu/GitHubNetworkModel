/**
 * 
 */
package statistics.issues.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.EgocentricUniplexNetworkStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class EgocentricIssueCommentIssueStatistic extends
		EgocentricUniplexNetworkStatistic implements IssueStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(EgocentricIssueCommentIssueStatistic.class.getName());

	/**
	 * 
	 */
	public EgocentricIssueCommentIssueStatistic() {
		nodeProperty = "issueID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped nodeProperty =  {}", nodeProperty);
	}

	@Override
	public double[] getValues(int issueID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(issueID, event);
		return results;
	}

}
