package events;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Event implements Comparable<Event> {

	private static final Logger LOG = LoggerFactory.getLogger(Event.class
			.getName());

	public double time;

	public Event() {
		time = Double.NEGATIVE_INFINITY;
	}

	public Event(double time) {
		this.time = time;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int compareTo(Event anotherEvent) {
		if (time < anotherEvent.time)
			return -1;
		else if (time > anotherEvent.time)
			return 1;
		else {
			// Specify a specific order for different event types when event
			// times are tied

			// By default, it is not a matter
			return 0;
		}
	}

	public boolean equals(Object object) {
		Event anotherEvent = (Event) object;
		if (time == anotherEvent.time)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return (new Double(time).hashCode());
	}

	public String toString() {
		return Double.toString(time);
	}

	public static int readEvents(String filePath, ArrayList<Event> events) {

		try {

			LOG.info("Reading events from {}", filePath);

			BufferedReader reader = new BufferedReader(new FileReader(new File(
					filePath)));

			// Read event times
			String line = null;
			int lineCounter = 0;
			while ((line = reader.readLine()) != null) {

				lineCounter++;
				if (lineCounter % 10000 == 0)
					System.out.println("Reading up to " + lineCounter);

				// Ignore the header
				if (lineCounter == 1)
					continue;

				StringTokenizer myTokenizer = new StringTokenizer(line,
						EventStreams.separateString);

				double time = EventStreams.dateFormatter.parse(
						myTokenizer.nextToken().trim()).getTime()
						/ EventStreams.ONE_DAY_IN_MILLISECONDS;				

				Event event = new Event(time);
				events.add(event);

				LOG.trace("Reading an event[{}]", event);

			}
			reader.close();
		} catch (Exception e) {
			LOG.error("Errors in reading events: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}

		return events.size();

	}

}
