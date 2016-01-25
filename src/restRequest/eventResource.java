package restRequest;

import entities.*;

import org.json.*;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	public StatusResponse createEvent(simpleRequest req) throws JSONException {
		System.out.println(req.getName());
//		for (User u : req.getUsers()) {
//			System.out.println(u.getEmail());
//		}
		User user = SQLConnection.getUserFromToken(req.getToken());
		if (user == null) {
			System.out.println("The user does not exist");
			return new StatusResponse("false");
		}
		String creatorId = user.getId();
		String eventId = req.getId(); 
		
		// do some checks ...
		if (SQLConnection.getEventFromIdevent(eventId) != null) {
			System.out.println("EventId already exists!");
			return new StatusResponse("false");
		}
		
		boolean noerrors = true;
		// create the event and add users to it
		noerrors = SQLConnection.createEvent(req.getName(), creatorId, eventId, req.getDesc());
		for (User thisUser : req.getUsers()) {
			// do not try to add the creator to the event, as he is already in it
			if (thisUser.getId().equals(req.getCreatorId())) { continue; }
			noerrors = noerrors && SQLConnection.addUserToEvent(thisUser.getEmail(), eventId);
		}
		
		System.out.println(noerrors);
		
		return noerrors? new StatusResponse("true") :
			new StatusResponse("false");
	}
	
	@Path("update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StatusResponse updateEvent(simpleRequest req) {
		System.out.println("UpdateEvent called.");
		String eventId = req.getId(); 
		User user = SQLConnection.getUserFromToken(req.getToken());
		if (user == null) {
			System.out.println("The user does not exist");
			return new StatusResponse("false");
		}
		String userId = user.getId();
		
		// do some checks ...
		Event originalEvent = SQLConnection.getEventFromIdevent(eventId);
		if (originalEvent == null || userId == null) {
			System.out.println("1 " + originalEvent + userId);
			return new StatusResponse("false");
		}
		if (!userId.equals(originalEvent.getCreatorId())) {
			System.out.println("2 " + userId + originalEvent.getCreatorId());
			return new StatusResponse("false");
		}
		List<User> newUsers = req.getUsers();
		List<User> oldUsers = SQLConnection.getUserFromEvent(originalEvent);
		System.out.println("old");
		for (User user2 : oldUsers) {
			System.out.println(user2.getEmail() + user2.getId() + user2.getName());
		}
		System.out.println("new");
		for (User user2 : newUsers) {
			System.out.println(user2.getEmail() + user2.getId() + user2.getName());
		}
		oldUsers.removeAll(newUsers);
		System.out.println("cleaned");
		for (User user2 : oldUsers) {
			System.out.println(user2.getEmail() + user2.getId() + user2.getName());
		}
//		System.out.println(oldUsers.get(0).equals(newUsers.get(0)));
		boolean modified = false;
		for (User thisUser : oldUsers) { // we check for every user that was deleted ...
			List<Expense> eventExpenses = SQLConnection.getExpenseFromIdevent(eventId);
			for (Expense thisExpense : eventExpenses) { // in every expense of this event
				List<ShareSimple> expenseShares = thisExpense.getShares();
				for (ShareSimple thisShare : expenseShares) { // and every share in every expense
					if (thisShare.getId().equals(thisUser.getId())) { // whether this share is the user's
						modified = modified || thisShare.getShare() != "0"; // and then set modified to true if his share is not zero
					}
				}
			}
		}
		if (modified) {
			System.out.println("Wanted to remove a user who still has shares.");
			return new StatusResponse("false");
		}
		
		Event event = new Event(req.getName(), req.getId(), req.getUsers(), req.getDesc(), req.getCreatorId(), req.getExpenses());
		event.setExpenses(SQLConnection.getExpenseFromIdevent(eventId));
		
		boolean noerrors = true;
		System.out.println("Trying to write event to database.");
		noerrors = SQLConnection.updateEvent(event);
		System.out.println(noerrors);
		
		return noerrors? new StatusResponse("true") :
			new StatusResponse("false");
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
