/**
 * 
 */
package events.pullRequests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.EventStreams;

/**
 * @author duyv
 * 
 */
public class PullRequestOpenedEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(PullRequestOpenedEvent.class.getName());

	protected int pullRequestID;
	protected int userID;
	protected int repoID;

	/**
	 * 
	 */
	public PullRequestOpenedEvent() {
	}

	/**
	 * @param time
	 */
	public PullRequestOpenedEvent(double time, int pullRequestID, int userID,
			int repoID) {
		super(time);
		this.pullRequestID = pullRequestID;
		this.userID = userID;
		this.repoID = repoID;
	}

	public int getPullRequestID() {
		return pullRequestID;
	}

	public void setPullRequestID(int pullRequestID) {
		this.pullRequestID = pullRequestID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getRepoID() {
		return repoID;
	}

	public void setRepoID(int repoID) {
		this.repoID = repoID;
	}

	public boolean equals(Object object) {
		PullRequestOpenedEvent anotherEvent = (PullRequestOpenedEvent) object;
		if (anotherEvent.time == time
				&& anotherEvent.pullRequestID == pullRequestID
				&& anotherEvent.userID == userID
				&& anotherEvent.repoID == repoID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(pullRequestID).hashCode())
				+ (new Integer(userID).hashCode())
				+ (new Integer(repoID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + "time=" + Double.toString(time)
				+ ":pullRequestID=" + pullRequestID + ":userID=" + userID
				+ ":repoID=" + repoID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading pull request opened events from {}", filePath);

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filePath)));

			// Read times, pull requests, users, and repos
			String line = null;
			int lineCounter = 0;
			while ((line = reader.readLine()) != null) {

				lineCounter++;
				
				// Ignore the header
				if (lineCounter == 1)
					continue;

				StringTokenizer myTokenizer = new StringTokenizer(line,
						EventStreams.separateString);

				double time = EventStreams.dateFormatter.parse(
						myTokenizer.nextToken().trim()).getTime()
						/ EventStreams.ONE_DAY_IN_MILLISECONDS;

				int pullRequestID = Integer.parseInt(myTokenizer.nextToken()
						.trim());

				int userID = Integer.parseInt(myTokenizer.nextToken().trim());

				int repoID = Integer.parseInt(myTokenizer.nextToken().trim());

				PullRequestOpenedEvent pullRequestOpenedEvent = new PullRequestOpenedEvent(
						time, pullRequestID, userID, repoID);
				events.add(pullRequestOpenedEvent);

				LOG.trace("Reading a pull request opened event[{}]",
						pullRequestOpenedEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading pull request opened events: "
					+ filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
