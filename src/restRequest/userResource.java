package restRequest;

import entities.*;
import entities.Error;
import Crypto.PasswordHash;
import Crypto.TokenGenerator;

import java.security.CryptoPrimitive;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;
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
		LoginResponse response = new LoginResponse("false", userId);
		String hash_db = SQLConnection.getHashToEmail(req.getEmail());
		
		if (hash_db != null) {
			String userPassword = req.getPassword();
			Boolean isValid = Crypto.PasswordHash.validatePassword(userPassword, hash_db);
			if (isValid) {
				response.setStatus("true");
			}
		}
		
		String token = TokenGenerator.generate();
		response.setToken(token);
		//TODO: Set token in db
		return response;
	}

	@Path("register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponse registerUser(simpleRequest req) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String id = req.getId();
		String email = req.getEmail();
		String name = req.getName(); //TODO: Name? :D
		String password = req.getPassword();
		LoginResponse response = new LoginResponse("false", null);
		
		// do some checks
		if (!(SQLConnection.getIduserFromEmail(email) == null) // email already exists
				|| !(SQLConnection.getUsernameFromIduser(id) == null)) { // id already exists
			return response;
		}
		
		// hash the new password
		String pass_hashed = Crypto.PasswordHash.createHash(password);
		
		// create the user in the db
		if (!SQLConnection.createUser(name, email, password, id)) {
			System.out.println("Error creating the user with " + name + email + pass_hashed + id);
			return response;
		}
		
		response.setStatus("true");
		String token = TokenGenerator.generate();
		response.setToken(token);
		//TODO: Set token in db
		
		
		return null;
	}

//	@Path("create")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	// @Consumes(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public LoginResponse createUser(@FormParam("name") String name, @FormParam("email") String email,
//			@FormParam("password") String pass) {
//		if (SQLConnection.createUser(name, email, pass)) {   //createUser(name,email,password,id)
//			return new LoginResponse("true", SQLConnection.getIduserFromEmail(email));
//		} else {
//			System.out.println("Failed to create user.");
//			return new LoginResponse("Failed to create user.", null);
//		}
//	}

//	@Path("delete")
//	@POST
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public void deleteUser(@FormParam("id") Integer id) {
//		System.out.println(id.toString());
//	}

	@Path("info/{id}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getUser(@PathParam("id") String id) {
		System.out.println("Requested information about " + id);
		try {
			return SQLConnection.getUsernameFromIduser(id).toString();
		} catch (Exception e) {
			return "User does not exist.";
		}
	}

//	@Path("exists")
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public String isUserFromEmail(simpleRequest req) {
//		return !(SQLConnection.getIduserFromEmail(email) == null) ? 
//				SQLConnection.getIduserFromEmail(email) : "false";
//	}
//
//	private Boolean isUserFromId(String id) {
//		return !getUser(id).equals(null);
//	}

//	@Path("getEvents/{id}") //TODO: id is not needed, can be derived by token
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public List<Event> getEventsFromUser(simpleRequest req) {
//		//TODO: Use the token, Luke!
//		if (isUserFromId(user_id)) {
//			List<String> eventsListString = SQLConnection.getEventsFromIduser(user_id.toString());
//			List<Event> eventsList = new ArrayList<Event>();
//
//			for (String thisEventId : eventsListString) {
//				eventsList.add(SQLConnection.getEventFromIdevent(thisEventId.toString()));
//			}
//			return eventsList;
//		} else {
//			System.out.println("getEventsFromUser got a not existing User");
//			return null;
//		}
//	}
}