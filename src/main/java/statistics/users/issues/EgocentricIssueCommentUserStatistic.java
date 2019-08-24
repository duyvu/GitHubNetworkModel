/**
 * 
 */
package statistics.users.issues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import statistics.EgocentricUniplexNetworkStatistic;
import statistics.users.UserStatistic;

/**
 * @author duyvu
 * 
 */
public abstract class EgocentricIssueCommentUserStatistic extends
		EgocentricUniplexNetworkStatistic implements UserStatistic {

	private static final Logger LOG = LoggerFactory
			.getLogger(EgocentricIssueCommentUserStatistic.class.getName());

	/**
	 * 
	 */
	public EgocentricIssueCommentUserStatistic() {
		nodeProperty = "userID";

		LOG.info("Creating a statistic named of class {} ", this.getClass()
				.getSimpleName());
		LOG.info(">> Mapped nodeProperty =  {}", nodeProperty);
	}

	@Override
	public double[] getValues(int userID, Event event) {
		double[] results = new double[1];
		results[0] = getValue(userID, event);
		return results;
	}

}
