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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createEvent(simpleRequest req) {
		//TODO: Look up id with token and compare against creatorId
		String uid = null;
		//String eventId = SQLConnection.createEvent(name, uid); //TODO: return event id in SQL connector please
		//return eventId;
		return null; //This returns true/false
	}
	
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateEvent(simpleRequest req) {
		//TODO: similar to create
		//returns true/false
		return null;
	}

	
	@Path("delete")
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void deleteEvent(@FormParam("id") Integer id) {
		System.out.println(id.toString());
	}
	
	@Path("get/{id}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getEvent(@PathParam("id") Integer id) {
		System.out.println("Requested information about " + id);
		return id.toString();
	}
	
	
}
