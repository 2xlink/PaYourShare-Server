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
	@Consumes(MediaType.APPLICATION_JSON)
	public String createExpense(simpleRequest req) {
		String userId = SQLConnection.getTokenFromIduser(req.getId());
		
		if (userId != req.getCreatorId()) {
			return "false";
		}
		// maybe also check here if the user is allowed to do this
		
		Expense expense = new Expense(req.getCreatorId(), req.getAmount(), req.getName(), req.getId(), req.getType(), req.getEventId());
		expense.setShares(req.getShares());
		
		boolean noerrors;
		noerrors = SQLConnection.createExpense(expense);

		return noerrors? "true" : "false";
	}
	
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateExpense(simpleRequest req) { //TODO: String parsen oder gleich als map (JSON!!) 
		String userId = SQLConnection.getTokenFromIduser(req.getId());
		
		if (userId != req.getCreatorId()) {
			return "false";
		}
		// maybe also check here if the user is allowed to do this
		
		Expense expense = new Expense(req.getCreatorId(), req.getAmount(), req.getName(), req.getId(), req.getType(), req.getEventId());
		expense.setShares(req.getShares());
		
		boolean noerrors;
		noerrors = SQLConnection.createExpense(expense);

		return noerrors? "true" : "false";
	}
//
//	
//	@Path("delete")
//	@POST
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public void deleteEvent(simpleRequest req) {
//		String id = req.getId();
//		System.out.println(id.toString());
//	}
//	
//	@Path("get/{id}")
//	@GET
//	@Produces(MediaType.TEXT_HTML)
//	public String getEvent(@PathParam("id") Integer id) {
//		System.out.println("Requested information about " + id);
//		return id.toString();
//	}
}
