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
public class ProjectOpenedEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProjectOpenedEvent.class.getName());

	protected int repoID;
	protected int ownerID;
	protected String name;
	protected String language;

	/**
	 * 
	 */
	public ProjectOpenedEvent() {
	}

	/**
	 * @param time
	 */
	public ProjectOpenedEvent(double time, int repoID, int ownerID,
			String name, String language) {
		super(time);
		this.repoID = repoID;
		this.ownerID = ownerID;
		this.name = name;
		this.language = language;
	}

	public int getRepoID() {
		return repoID;
	}

	public void setRepoID(int repoID) {
		this.repoID = repoID;
	}

	public int getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean equals(Object object) {
		ProjectOpenedEvent anotherEvent = (ProjectOpenedEvent) object;
		if (anotherEvent.time == time && anotherEvent.repoID == repoID
				&& anotherEvent.ownerID == ownerID && anotherEvent.name == name
				&& anotherEvent.language == language)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode()) + (new Integer(repoID).hashCode())
				+ (new Integer(ownerID).hashCode()) + (name.hashCode())
				+ (language.hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + ":time=" + Double.toString(time)
				+ ":repoID=" + repoID + ":ownerID=" + ownerID + ":name=" + name
				+ ":language=" + language;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading project opened events from {}", filePath);

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filePath)));

			// Read projects and their enter times
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

				int repoID = Integer.parseInt(myTokenizer.nextToken().trim());

				int ownerID = Integer.parseInt(myTokenizer.nextToken().trim());

				String name = myTokenizer.nextToken().trim();

				String language = myTokenizer.nextToken().trim();

				ProjectOpenedEvent projectOpenedEvent = new ProjectOpenedEvent(
						time, repoID, ownerID, name, language);
				events.add(projectOpenedEvent);

				LOG.trace("Reading a project opened event[{}]",
						projectOpenedEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading project opened events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
