/**
 * 
 */
package events.issues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.EventStreams;
import events.issues.IssueCommentEvent;

/**
 * @author duyv
 * 
 */
public class IssueCommentEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueCommentEvent.class.getName());

	protected int commentID;
	protected int userID;
	protected int issueID;
	protected int repoID;

	/**
	 * 
	 */
	public IssueCommentEvent() {
	}

	/**
	 * @param time
	 */
	public IssueCommentEvent(double time, int commentID, int userID,
			int issueID, int repoID) {
		super(time);
		this.commentID = commentID;
		this.userID = userID;
		this.issueID = issueID;
		this.repoID = repoID;
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

	public int getIssueID() {
		return issueID;
	}

	public void setIssueID(int issueID) {
		this.issueID = issueID;
	}

	public int getRepoID() {
		return repoID;
	}

	public void setRepoID(int repoID) {
		this.repoID = repoID;
	}

	public boolean equals(Object object) {
		IssueCommentEvent anotherEvent = (IssueCommentEvent) object;
		if (anotherEvent.time == time && anotherEvent.commentID == commentID
				&& anotherEvent.userID == userID
				&& anotherEvent.issueID == issueID
				&& anotherEvent.repoID == repoID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(commentID).hashCode())
				+ (new Integer(userID).hashCode())
				+ (new Integer(issueID).hashCode())
				+ (new Integer(repoID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":commentID=" + commentID + ":userID=" + userID + ":issueID="
				+ issueID + ":repoID=" + repoID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading issue comment events from {}", filePath);

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filePath)));

			// Read users and their enter times
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

				int issueID = Integer.parseInt(myTokenizer.nextToken().trim());

				int repoID = Integer.parseInt(myTokenizer.nextToken().trim());

				IssueCommentEvent issueCommentEvent = new IssueCommentEvent(
						time, commentID, userID, issueID, repoID);
				events.add(issueCommentEvent);

				LOG.trace("Reading an issue comment event[{}]",
						issueCommentEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading issue comment events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
