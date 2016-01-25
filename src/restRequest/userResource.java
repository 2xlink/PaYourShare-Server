package restRequest;

import entities.*;
import Crypto.TokenGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/user")
public class userResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@Path("login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponse loginUser(simpleRequest req)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		String userId = SQLConnection.getIduserFromEmail(req.getEmail());
		String hash_db = SQLConnection.getHashToEmail(req.getEmail());
		
		if (userId == null || hash_db == null) {
			return new LoginResponse("false", null, null);
		}
		String userPassword = req.getPassword();
		System.out.println(userPassword + hash_db);
		Boolean isValid = Crypto.PasswordHash.validatePassword(userPassword, hash_db);
		
		if (!isValid) {
			return new LoginResponse("false", null, null);
		}
		
		String token = TokenGenerator.generate();
		SQLConnection.setToken(userId, token);
		return new LoginResponse("true", userId, token);
	}

	@Path("register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponse registerUser(simpleRequest req) throws NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("RegisterUser was called");
		String id = req.getId();
		String email = req.getEmail();
		String name = email;
		String pass_plain = req.getPassword();
		
		// do some checks
		if (SQLConnection.getIduserFromEmail(email) != null // email already exists
				|| SQLConnection.getUsernameFromIduser(id) != null) { // id already exists
			return new LoginResponse("false", null, null);
		}
		System.out.println("pw hashing step");
		// hash the new password
		String pass_hashed = Crypto.PasswordHash.createHash(pass_plain);
		
		System.out.println("The password is " + pass_hashed);
		String token = TokenGenerator.generate();
		// create the user in the db
		if (!SQLConnection.createUser(name, email, pass_hashed, id, token)) {
			System.out.println("Error creating the user with " + name + email + pass_hashed + id);
			return new LoginResponse("false", null, null);
		}
		
		
		System.out.println("Registered user " + name + email + pass_hashed + id);
		return new LoginResponse("true", id, token);
	}

	@Path("exists")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public UserExistsResponse isUserFromEmail(simpleRequest req) {
		String email = req.getEmail();
		String userId = SQLConnection.getIduserFromEmail(email);
		
		if (userId != null) {
			return new UserExistsResponse("true", userId);
		}
		else {
			return new UserExistsResponse("false", null);
		}
	}

	@SuppressWarnings("unused")
	private Boolean isUserFromId(String id) {
		return SQLConnection.getUsernameFromIduser(id) != null;
	}

	@Path("getEvents")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public EventListResponse getEventsFromUser(simpleRequest req) {
		System.out.println("GetEvents called");
		String token = req.getToken();
		User user = SQLConnection.getUserFromToken(token);
		if (user == null) {
			return new EventListResponse("false", null);
		}
		String userId = user.getId();
		List<String> eventsListString = SQLConnection.getEventsFromIduser(userId.toString()); // returns a list of EventIDs
		List<Event> eventList = new ArrayList<Event>();
		
		System.out.println("eventsList: " + eventsListString);

		for (String thisEventId : eventsListString) {
			System.out.println(thisEventId);
			Event thisEvent = SQLConnection.getEventFromIdevent(thisEventId.toString());
			// due to inconsistencies we need to add the users and expenses seperately
			thisEvent.setUsers(SQLConnection.getUserFromEvent(thisEvent));
			thisEvent.setExpenses(SQLConnection.getExpenseFromIdevent(thisEventId));
			eventList.add(thisEvent);
		}
		return new EventListResponse("true", eventList);
	}
	
//	@Path("info")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public UserInfoResponse getInfoFromUser(simpleRequest req) {
//		System.out.println("GetInfo called");
//		String token = req.getToken();
//		User user = SQLConnection.getUserFromToken(token);
//		if (user == null) {
//			return new UserInfoResponse("false", null);
//		}
//		
//	}
}