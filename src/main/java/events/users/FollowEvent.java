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
public class FollowEvent extends Event {

	private static final Logger LOG = LoggerFactory.getLogger(FollowEvent.class
			.getName());

	protected int senderID;
	protected int receiverID;

	/**
	 * 
	 */
	public FollowEvent() {
	}

	/**
	 * @param time
	 */
	public FollowEvent(double time, int senderID, int receiverID) {
		super(time);
		this.senderID = senderID;
		this.receiverID = receiverID;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public int getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(int receiverID) {
		this.receiverID = receiverID;
	}

	public boolean equals(Object object) {
		FollowEvent anotherEvent = (FollowEvent) object;
		if (anotherEvent.time == time && anotherEvent.senderID == senderID
				&& anotherEvent.receiverID == receiverID)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode())
				+ (new Integer(senderID).hashCode())
				+ (new Integer(receiverID).hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + "time=" + Double.toString(time)
				+ ":senderID=" + senderID + ":receiverID=" + receiverID;
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading follow events from {}", filePath);

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filePath)));

			// Read follow times, senders, and receivers
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

				int senderID = Integer.parseInt(myTokenizer.nextToken().trim());

				int receiverID = Integer.parseInt(myTokenizer.nextToken()
						.trim());

				FollowEvent followEvent = new FollowEvent(time, senderID,
						receiverID);
				events.add(followEvent);

				LOG.trace("Reading a follow event[{}]", followEvent);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading follow events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
