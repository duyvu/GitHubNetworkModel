package graphs.processors;

import events.Event;

public interface RelationalEventStreamProcessor {

	public void processRelationalEvent(Event event);

}
