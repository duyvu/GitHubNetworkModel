/**
 * 
 */
package events.commits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.EventStreams;
import events.commits.CommitCommentEvent;

/**
 * @author duyv
 * 
 */
public class CommitCommentEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(CommitCommentEvent.class.getName());

	protected int commentID;
	protected int userID;
	protected int commitID;
	protected int line;
	protected int position;

	/**
	 * 
	 */
	public CommitCommentEvent() {
	}

	/**
	 * @param time
	 */
	public CommitCommentEvent(double time, int commentID, int userID,
			int commitID, int line, int position) {
		super(time);
		this.commentID = commentID;
		this.userID = userID;
		this.commitID = commitID;
		this.line = line;
		this.position = position;
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

	public int getCommitID() {
		return commitID;
	}

	public void setCommitID(int commitID) {
		this.commitID = commitID;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean equals(Object object) {
		CommitCommentEvent anotherEvent = (CommitCommentEvent) object;
		if (anotherEvent.time == time && anotherEvent.commentID == commentID
				&& anotherEvent.userID == userID
				&& anotherEvent.commitID == commitID
				&& anotherEvent.line == line
				&& anotherEvent.position == position)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(commentID).hashCode())
				+ (new Integer(userID).hashCode())
				+ (new Integer(commitID).hashCode())
				+ (new Integer(line).hashCode())
				+ (new Integer(position).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":commentID=" + commentID + ":userID=" + userID
				+ ":commitID=" + commitID + ":line=" + line + ":position="
				+ position;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading commit comment events from {}", filePath);

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

				int commitID = Integer.parseInt(myTokenizer.nextToken().trim());

				int lineNumber = Integer.parseInt(myTokenizer.nextToken()
						.trim());

				int position = Integer.parseInt(myTokenizer.nextToken().trim());

				CommitCommentEvent commitCommentEvent = new CommitCommentEvent(
						time, commentID, userID, commitID, lineNumber, position);
				events.add(commitCommentEvent);

				LOG.trace("Reading a commit comment event[{}]",
						commitCommentEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading commit comment events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}
}
