package graphs.atRiskStreams;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import events.Event;
import events.projects.ProjectOpenedEvent;

public class ProjectAtRiskStatus extends NodeAtRisk {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProjectAtRiskStatus.class.getName());

	private static volatile ProjectAtRiskStatus instance = null;

	public static ProjectAtRiskStatus getInstance() {
		if (instance == null) {
			synchronized (ProjectAtRiskStatus.class) {
				// Double check
				if (instance == null) {
					instance = new ProjectAtRiskStatus();
				}
			}
		}
		return instance;
	}

	protected ConcurrentHashMap<Integer, ProjectOpenedEvent> projects = null;

	// private constructor
	private ProjectAtRiskStatus() {
		projects = new ConcurrentHashMap<>();
	}

	/**
	 * Add a new project to the database this implies the project starts to be
	 * at risk In case the project is already in the database, the project event
	 * is updated
	 */
	public void addAtRiskProjectEvent(ProjectOpenedEvent projectOpenedEvent) {

		LOG.trace("Add a project: {}" + projectOpenedEvent);

		projects.put(projectOpenedEvent.getRepoID(), projectOpenedEvent);
		atRiskSet.add(projectOpenedEvent.getRepoID());
	}

	/**
	 * Remove a project from the risk set
	 * 
	 * @return true is returned if the project is removed from the set;
	 *         otherwise, false is returned if the project is not in the set
	 */
	public boolean removeAtRiskProject(int projectID) {

		LOG.trace("Remove a project from the at risk set: {}"
				+ projects.get(projectID));

		return atRiskSet.remove(projectID);
	}

	@Override
	public void processAtRiskEvent(Event event) {
		if (event instanceof ProjectOpenedEvent) {
			ProjectOpenedEvent projectOpenedEvent = (ProjectOpenedEvent) event;
			addAtRiskProjectEvent(projectOpenedEvent);
		} else {
			super.processAtRiskEvent(event);
		}
	}
}
