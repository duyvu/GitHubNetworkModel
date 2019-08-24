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
import events.commits.CommitEvent;

/**
 * @author duyv
 * 
 */
public class CommitEvent extends Event {

	private static final Logger LOG = LoggerFactory.getLogger(CommitEvent.class
			.getName());

	protected int commitID;
	protected int authorID;
	protected int committerID;
	protected int repoID;

	/**
	 * 
	 */
	public CommitEvent() {
	}

	/**
	 * @param time
	 */
	public CommitEvent(double time, int commitID, int authorID,
			int committerID, int repoID) {
		super(time);
		this.commitID = commitID;
		this.authorID = authorID;
		this.committerID = committerID;
		this.repoID = repoID;
	}

	public int getCommitID() {
		return commitID;
	}

	public void setCommitID(int commitID) {
		this.commitID = commitID;
	}

	public int getAuthorID() {
		return authorID;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public int getCommitterID() {
		return committerID;
	}

	public void setCommitterID(int committerID) {
		this.committerID = committerID;
	}

	public int getRepoID() {
		return repoID;
	}

	public void setRepoID(int repoID) {
		this.repoID = repoID;
	}

	public boolean equals(Object object) {
		CommitEvent anotherEvent = (CommitEvent) object;
		if (anotherEvent.time == time && anotherEvent.commitID == commitID
				&& anotherEvent.authorID == authorID
				&& anotherEvent.committerID == committerID
				&& anotherEvent.repoID == repoID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(commitID).hashCode())
				+ (new Integer(authorID).hashCode())
				+ (new Integer(committerID).hashCode())
				+ (new Integer(repoID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":commitID=" + commitID + ":authorID=" + authorID
				+ ":committerID=" + committerID + ":repoID=" + repoID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading commit events from {}", filePath);

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

				int commitID = Integer.parseInt(myTokenizer.nextToken().trim());

				int authorID = Integer.parseInt(myTokenizer.nextToken().trim());

				int committerID = Integer.parseInt(myTokenizer.nextToken()
						.trim());

				int repoID = Integer.parseInt(myTokenizer.nextToken().trim());

				CommitEvent commitEvent = new CommitEvent(time, commitID,
						authorID, committerID, repoID);
				events.add(commitEvent);

				LOG.trace("Reading a commit event[{}]", commitEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading commit events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
