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
public class PullRequestCommentEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(PullRequestCommentEvent.class.getName());

	protected int commentID;
	protected int userID;
	protected int pullRequestID;
	protected int commitID;

	/**
	 * 
	 */
	public PullRequestCommentEvent() {
	}

	/**
	 * @param time
	 */
	public PullRequestCommentEvent(double time, int commentID, int userID,
			int pullRequestID, int commitID) {
		super(time);
		this.commentID = commentID;
		this.userID = userID;
		this.pullRequestID = pullRequestID;
		this.commitID = commitID;
	}

	public int getCommentID() {
		return commentID;
	}

	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getPullRequestID() {
		return pullRequestID;
	}

	public void setPullRequestID(int pullRequestID) {
		this.pullRequestID = pullRequestID;
	}

	public int getCommitID() {
		return commitID;
	}

	public void setCommitID(int commitID) {
		this.commitID = commitID;
	}

	public boolean equals(Object object) {
		PullRequestCommentEvent anotherEvent = (PullRequestCommentEvent) object;
		if (anotherEvent.time == time && anotherEvent.commentID == commentID
				&& anotherEvent.userID == userID
				&& anotherEvent.pullRequestID == pullRequestID
				&& anotherEvent.commitID == commitID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(commentID).hashCode())
				+ (new Integer(userID).hashCode())
				+ (new Integer(pullRequestID).hashCode())
				+ (new Integer(commitID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":commentID=" + commentID + ":userID=" + userID
				+ ":pullRequestID=" + pullRequestID + ":commitID=" + commitID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading pull request comment events from {}", filePath);

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filePath)));

			// Read times, comments, users, pull requests, and commits
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

				int commentID = Integer
						.parseInt(myTokenizer.nextToken().trim());

				int userID = Integer.parseInt(myTokenizer.nextToken().trim());

				int pullRequestID = Integer.parseInt(myTokenizer.nextToken()
						.trim());
				int commitID = Integer.parseInt(myTokenizer.nextToken().trim());

				PullRequestCommentEvent pullRequestCommentEvent = new PullRequestCommentEvent(
						time, commentID, userID, pullRequestID, commitID);
				events.add(pullRequestCommentEvent);

				LOG.trace("Reading a pull request comment event[{}]",
						pullRequestCommentEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading pull request comment events: "
					+ filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}
}
