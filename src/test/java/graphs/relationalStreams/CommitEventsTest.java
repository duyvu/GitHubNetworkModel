package graphs.relationalStreams;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import events.Event;
import events.commits.CommitEvent;
import graphs.relationalStreams.IndexedCommitEventStream;

public class CommitEventsTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(CommitEventsTest.class.getName());

	IndexedCommitEventStream commitEventStream = null;

	@BeforeMethod
	protected void resetCommitEvents() {

		LOG.debug(">> resetCommitEvents()");

		commitEventStream = IndexedCommitEventStream.getInstance();
		commitEventStream
				.setIndexedPropertyNames(commitEventStream.DEFAULT_INDEXED_PROPERTY_NAMES);
	}

	@Test(dependsOnMethods = { "getInstance" })
	public void addCommitEvent() {

		LOG.debug(">> addCommitEvent()");

		double time = 123.456;
		int commitID = 1;
		int authorID = 2;
		int committerID = 3;
		int repoID = 4;
		CommitEvent commitEvent = new CommitEvent(time, commitID, authorID,
				committerID, repoID);
		commitEventStream.processRelationalEvent(commitEvent);
		for (String propertyName : commitEventStream.indexedPropertyNames) {
			assertThat(commitEventStream.indexedEventStream.get(propertyName),
					is(notNullValue()));
			assertThat(
					1,
					is(equalTo(commitEventStream.indexedEventStream.get(
							propertyName).size())));
		}
	}

	@Test
	public void getInstance() {

		LOG.debug(">> getInstance()");

		IndexedCommitEventStream commitEvents1 = IndexedCommitEventStream.getInstance();
		IndexedCommitEventStream commitEvents2 = IndexedCommitEventStream.getInstance();
		assertThat(commitEvents1, is(notNullValue()));
		assertThat(commitEvents2, is(notNullValue()));
		assertThat(commitEvents1, is(sameInstance(commitEvents2)));
	}

	@Test(dependsOnMethods = { "getInstance", "addCommitEvent" })
	public void getPropertyMap() {

		LOG.debug(">> getPropertyMap()");

		double time = 123.456;
		int commitID = 1;
		int authorID = 2;
		int committerID = 3;
		int repoID = 4;
		CommitEvent commitEvent = new CommitEvent(time, commitID, authorID,
				committerID, repoID);

		System.out.println("Commit Maps: "
				+ commitEventStream.indexedEventStream.keySet());

		commitEventStream.processRelationalEvent(commitEvent);
		for (String propertyName : commitEventStream.indexedPropertyNames) {
			ConcurrentHashMap<Integer, ArrayList<Event>> myPropertyMap = commitEventStream
					.getPropertyMap(propertyName);
			assertThat(myPropertyMap, is(notNullValue()));
			assertThat(1, is(equalTo(myPropertyMap.size())));
		}
	}

	@Test(dependsOnMethods = { "getInstance" })
	public void setIndexedPropertyNames() {

		LOG.debug(">> setIndexedPropertyNames()");

		String[] indexedPropertyNames = { "authorID", "repoID" };
		commitEventStream.setIndexedPropertyNames(indexedPropertyNames);
		assertThat(2, is(equalTo(commitEventStream.indexedEventStream.size())));
	}

	@Test(dependsOnMethods = { "getInstance" })
	public void setIndexedPropertyNamesNull() {

		LOG.debug(">> setIndexedPropertyNamesNull()");

		String[] indexedPropertyNames = null;
		commitEventStream.setIndexedPropertyNames(indexedPropertyNames);
		assertThat(0, is(equalTo(commitEventStream.indexedEventStream.size())));
	}

}
