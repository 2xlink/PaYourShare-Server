package restRequest;

import entities.*;
import entities.Error;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/event")
public class eventResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@Path("create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String createEvent(simpleRequest req) {
		System.out.println(req.getName());
		for (User u : req.getUsers()) {
			System.out.println(u.getEmail());
		}
		//String userId = SQLConnection.getTokenFromIduser(req.getId());
		String userId = "";
		String eventId = req.getId(); //TODO: Maybe use eventId here to avoid confusion
		
		// do some checks ...
		if (SQLConnection.getEventFromIdevent(eventId) != null) {
			return "false";
		}
		if (userId != req.getCreatorId()) {
			return "false";
		}
		
		boolean noerrors = true;
		// create the event and add users to it
		noerrors = SQLConnection.createEvent(req.getName(), userId, eventId, req.getDesc());
		for (User thisUser : req.getUsers()) {
			noerrors = noerrors && SQLConnection.addUserToEvent(thisUser.getEmail(), eventId);
		}
		
		return noerrors? "true" : "false";
	}
	
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateEvent(simpleRequest req) {
		String userId = SQLConnection.getTokenFromIduser(req.getId());
		String eventId = req.getId(); //TODO: Maybe use eventId here to avoid confusion
		
		// do some checks ...
		Event originalEvent = SQLConnection.getEventFromIdevent(eventId);
		if (originalEvent == null
				|| originalEvent.getCreatorId() != userId) {
			return "false";
		}
		
		Event event = new Event(req.getName(), req.getId(), req.getUsers(), req.getDesc(), req.getCreatorId(), req.getExpenses());
		
		boolean noerrors = true;
		noerrors = SQLConnection.updateEvent(event);
		
		return noerrors? "true" : "false";
	}

	
//	@Path("delete")
//	@POST
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public void deleteEvent(@FormParam("id") Integer id) {
//		System.out.println(id.toString());
//	}
	
//	@Path("get/{id}")
//	@GET
//	@Produces(MediaType.TEXT_HTML)
//	public String getEvent(@PathParam("id") Integer id) {
//		System.out.println("Requested information about " + id);
//		return id.toString();
//	}
	
	
}
