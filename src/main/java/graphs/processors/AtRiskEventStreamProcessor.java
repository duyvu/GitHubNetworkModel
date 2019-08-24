package graphs.processors;

import events.Event;

public interface AtRiskEventStreamProcessor {

	public void processAtRiskEvent(Event event);

}
