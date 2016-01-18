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
	public String createEvent(
			@FormParam("token") String token, 
			@FormParam("id") String eventId, 
			@FormParam("name") String name, 
			@FormParam("desc") String description,
			@FormParam("users") List<User> users, 
			@FormParam("creatorId") String creatorId) {
		//TODO: Look up id with token and compare against creatorId
		String uid = null;
		//TODO: If successful send true, else false
		//String eventId = SQLConnection.createEvent(name, uid); //TODO: return event id in SQL connector please
		//return eventId;
		return null; //TODO: Does this need to return a id or is true/false enough?
	}
	
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateEvent(
			@FormParam("token") String token,
			@FormParam("eventId") String eventId, 
			@FormParam("name") String name, 
			@FormParam("desc") String description, 
			@FormParam("users") List<User> users, 
			@FormParam("creatorId") String creatorId,
			@FormParam("version") String version) {
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
