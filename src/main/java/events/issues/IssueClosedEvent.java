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
import events.issues.IssueClosedEvent;

/**
 * @author duyv
 * 
 */
public class IssueClosedEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(IssueClosedEvent.class.getName());

	protected int issueID;
	protected int userID;

	/**
	 * 
	 */
	public IssueClosedEvent() {
	}

	/**
	 * @param time
	 */
	public IssueClosedEvent(double time, int issueID, int userID) {
		super(time);
		this.issueID = issueID;
		this.userID = userID;
	}

	public int getIssueID() {
		return issueID;
	}

	public void setIssueID(int issueID) {
		this.issueID = issueID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public boolean equals(Object object) {
		IssueClosedEvent anotherEvent = (IssueClosedEvent) object;
		if (anotherEvent.time == time && anotherEvent.issueID == issueID
				&& anotherEvent.userID == userID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(issueID).hashCode())
				+ (new Integer(userID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":issueID=" + issueID + ":userID=" + userID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading issue closed events from {}", filePath);

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

				int issueID = Integer.parseInt(myTokenizer.nextToken().trim());

				int userID = Integer.parseInt(myTokenizer.nextToken().trim());

				IssueClosedEvent issueClosedEvent = new IssueClosedEvent(time,
						issueID, userID);
				events.add(issueClosedEvent);

				LOG.trace("Reading a issue closed event[{}]", issueClosedEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading issue closed events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
