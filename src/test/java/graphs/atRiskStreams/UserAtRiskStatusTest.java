package graphs.atRiskStreams;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

import events.users.UserOpenedEvent;
import graphs.atRiskStreams.UserAtRiskStatus;

public class UserAtRiskStatusTest {

	@Test
	public void getInstance() {
		UserAtRiskStatus userAtRiskStatus1 = UserAtRiskStatus.getInstance();
		UserAtRiskStatus userAtRiskStatus2 = UserAtRiskStatus.getInstance();
		assertThat(userAtRiskStatus1, is(notNullValue()));
		assertThat(userAtRiskStatus2, is(notNullValue()));
		assertThat(userAtRiskStatus1, is(sameInstance(userAtRiskStatus2)));
	}

	@Test(dependsOnMethods = { "getInstance" })
	public void addUserEvent() {
		UserAtRiskStatus userAtRiskStatus = UserAtRiskStatus.getInstance();
		double time = 123.456;
		int userID = 1;
		UserOpenedEvent userOpenedEvent = new UserOpenedEvent(time, userID);
		userAtRiskStatus.addAtRiskUserEvent(userOpenedEvent);

		assertTrue(userAtRiskStatus.users.containsKey(userID),
				"Newly added user opened event must be in the map");
		assertTrue(userAtRiskStatus.atRiskSet.contains(userID),
				"Newly added user opened event must be at risk");
	}

	@Test(dependsOnMethods = { "getInstance" })
	public void addUserEventForUserAlreadyInList() {
		UserAtRiskStatus userAtRiskStatus = UserAtRiskStatus.getInstance();

		double time = 123.456;
		int userID = 1;
		UserOpenedEvent userOpenedEvent = new UserOpenedEvent(time, userID);
		userAtRiskStatus.addAtRiskUserEvent(userOpenedEvent);

		double time2 = 456.789;
		UserOpenedEvent userOpenedEvent2 = new UserOpenedEvent(time2, userID);
		userAtRiskStatus.addAtRiskUserEvent(userOpenedEvent2);
		assertThat(1, is(equalTo(userAtRiskStatus.users.size())));
		assertThat(time2, is(equalTo(userAtRiskStatus.users.get(userID)
				.getTime())));
	}

	@Test(dependsOnMethods = { "getInstance", "addUserEvent" })
	public void isUserAtRisk() {

		UserAtRiskStatus userAtRiskStatus = UserAtRiskStatus.getInstance();

		double time = 123.456;
		int userID = 1;
		UserOpenedEvent userOpenedEvent = new UserOpenedEvent(time, userID);
		userAtRiskStatus.addAtRiskUserEvent(userOpenedEvent);

		assertTrue(userAtRiskStatus.isAtRisk(userID),
				"The status of an at risk user must be true");
		assertFalse(userAtRiskStatus.isAtRisk(userID + 1),
				"The status of a made-up user must be false");
	}

	@Test(dependsOnMethods = { "getInstance", "addUserEvent" })
	public void removeAtRiskUser() {
		UserAtRiskStatus userAtRiskStatus = UserAtRiskStatus.getInstance();

		double time = 123.456;
		int userID = 1;
		UserOpenedEvent userOpenedEvent = new UserOpenedEvent(time, userID);
		userAtRiskStatus.addAtRiskUserEvent(userOpenedEvent);

		int prevAtRiskSize = userAtRiskStatus.atRiskSet.size();
		userAtRiskStatus.removeAtRiskUser(userID);
		assertThat(prevAtRiskSize - 1,
				is(equalTo(userAtRiskStatus.atRiskSet.size())));
	}
}
