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
import events.issues.IssueOpenedEvent;

/**
 * @author duyv
 * 
 */
public class IssueOpenedEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueOpenedEvent.class.getName());

	protected int issueID;
	protected int repoID;
	protected int pullRequestID;

	/**
	 * 
	 */
	public IssueOpenedEvent() {
	}

	/**
	 * @param time
	 */
	public IssueOpenedEvent(double time, int issueID, int repoID,
			int pullRequestID) {
		super(time);
		this.issueID = issueID;
		this.repoID = repoID;
		this.pullRequestID = pullRequestID;
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

	public int getPullRequestID() {
		return pullRequestID;
	}

	public void setPullRequestID(int pullRequestID) {
		this.pullRequestID = pullRequestID;
	}

	public boolean equals(Object object) {
		IssueOpenedEvent anotherEvent = (IssueOpenedEvent) object;
		if (anotherEvent.time == time && anotherEvent.issueID == issueID
				&& anotherEvent.repoID == repoID
				&& anotherEvent.pullRequestID == pullRequestID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(issueID).hashCode())
				+ (new Integer(repoID).hashCode())
				+ (new Integer(pullRequestID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":issueID=" + issueID + ":repoID=" + repoID
				+ ":pullRequestID=" + pullRequestID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading issue opened events from {}", filePath);

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filePath)));

			// Read issues and their enter times
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

				int issueID = Integer.parseInt(myTokenizer.nextToken().trim());

				int repoID = Integer.parseInt(myTokenizer.nextToken().trim());

				int pullRequestID = Integer.parseInt(myTokenizer.nextToken()
						.trim());

				IssueOpenedEvent issueOpenedEvent = new IssueOpenedEvent(time,
						issueID, repoID, pullRequestID);
				events.add(issueOpenedEvent);

				LOG.trace("Reading an issue opened event[{}]", issueOpenedEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading issue opened events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
