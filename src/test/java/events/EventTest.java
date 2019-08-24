package events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

public class EventTest {

	@Test
	public void Constructor() {
		double time = 123.456;
		Event event = new Event(time);
		assertThat(time, is(equalTo(event.time)));
	}

	@Test(dependsOnMethods = { "Constructor" })
	public void compareTo() {

		double time1 = 1.0;
		double time2 = 2.0;

		Event event1 = new Event(time1);
		Event event2 = new Event(time1);
		assertThat(event1, comparesEqualTo(event2));

		Event event3 = new Event(time1);
		Event event4 = new Event(time2);
		assertThat(event3, lessThan(event4));
		assertThat(event4, greaterThan(event3));

	}

	@Test(dependsOnMethods = { "Constructor" })
	public void equals() {		
		double time1 = 123.456;
		Event event1 = new Event(time1);
		Event event2 = new Event(time1);
		assertTrue(event1.equals(event2),
				"Events 1 and 2 having the same time stamp must be equal");

		double time2 = 456.789;
		Event event3 = new Event(time2);
		assertFalse(event1.equals(event3),
				"Events 1 and 3 having different time stamps must not be equal");		
	}

	@Test(dependsOnMethods = { "Constructor" })
	public void getTime() {
		double time = 123.456;
		Event event = new Event(time);
		assertThat(time, is(equalTo(event.getTime())));
	}

	@Test(dependsOnMethods = { "Constructor" })
	public void setTime() {
		double time = 123.456;
		Event event = new Event();
		event.setTime(time);
		assertThat(time, is(equalTo(event.time)));
	}
}
