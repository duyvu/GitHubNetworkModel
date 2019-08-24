/**
 * 
 */
package events.projects;

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
public class ProjectMemberEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProjectMemberEvent.class.getName());

	protected int userID;
	protected int repoID;

	/**
	 * 
	 */
	public ProjectMemberEvent() {
	}

	/**
	 * @param time
	 */
	public ProjectMemberEvent(double time, int userID, int repoID) {
		super(time);
		this.userID = userID;
		this.repoID = repoID;
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
		ProjectMemberEvent anotherEvent = (ProjectMemberEvent) object;
		if (anotherEvent.time == time && anotherEvent.userID == userID
				&& anotherEvent.repoID == repoID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode()) + (new Integer(userID).hashCode())
				+ (new Integer(repoID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":userID=" + userID + ":repoID=" + repoID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading project member events from {}", filePath);

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

				int userID = Integer.parseInt(myTokenizer.nextToken().trim());

				int repoID = Integer.parseInt(myTokenizer.nextToken().trim());

				ProjectMemberEvent projectMemberEvent = new ProjectMemberEvent(
						time, userID, repoID);
				events.add(projectMemberEvent);

				LOG.trace("Reading a project member event[{}]",
						projectMemberEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading project member events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
