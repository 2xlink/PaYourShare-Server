package entities;

import java.util.List;

public class GetEventsResponse {
	private String status;
	private List<Event> eventList;
	
	public GetEventsResponse(String status, List<Event> eventList) {
		setStatus(status);
		setEventList(eventList);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Event> getEventList() {
		return eventList;
	}
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
}
