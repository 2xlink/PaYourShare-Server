package entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventResponse {
	private Event event;
	private Error error;
	
	public EventResponse() {}
	public EventResponse(Event e, Error er) {
		setEvent(e);
		setError(er);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
