/**
 * 
 */
package events.users;

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
public class UserOpenedEvent extends Event {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserOpenedEvent.class.getName());

	protected int userID;

	/**
	 * 
	 */
	public UserOpenedEvent() {
	}

	/**
	 * @param time
	 */
	public UserOpenedEvent(double time, int userID) {
		super(time);
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public boolean equals(Object object) {
		UserOpenedEvent anotherEvent = (UserOpenedEvent) object;
		if (anotherEvent.time == time && anotherEvent.userID == userID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode()) + (new Integer(userID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + "time=" + Double.toString(time)
				+ ":userID=" + userID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading user opened events from {}", filePath);

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

				UserOpenedEvent userOpenedEvent = new UserOpenedEvent(time,
						userID);
				events.add(userOpenedEvent);

				LOG.trace("Reading a user opened event[{}]", userOpenedEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading user opened events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
