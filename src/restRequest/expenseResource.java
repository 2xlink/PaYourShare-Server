package restRequest;

import entities.*;
import entities.Error;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@Path("/expense")
public class expenseResource {
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
	public String createExpense(
			@FormParam("token") String token, 
			@FormParam("id") String expenseId, 
			@FormParam("id_event") String eventId, 
			@FormParam("amount") String amount, 
			@FormParam("creatorId") String creatorId, 
			@FormParam("name") String name,
			@FormParam("type") String type, 
			@FormParam("version") String version, 
			@FormParam("shares") List<Share> shares) { //TODO: String parsen oder gleich als map (JSON!!)
		List<String> i = Arrays.asList(token.split("\\s*,\\s*"));
		String expenseId = SQLConnection.createExpense(); //TODO: please return id, what parameters??
		return expenseId; // TODO: Conflict with: "return id if possible, else false"
	}
	
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateExpense(
			@FormParam("token") String token, 
			@FormParam("id") String expenseId, 
			@FormParam("id_event") String eventId, 
			@FormParam("amount") String amount, 
			@FormParam("creatorId") String creatorId, 
			@FormParam("name") String name,
			@FormParam("type") String type, 
			@FormParam("version") String version, 
			@FormParam("shares") List<Share> shares) { //TODO: String parsen oder gleich als map (JSON!!) 
		//similar to event create
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