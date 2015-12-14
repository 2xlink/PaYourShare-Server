package restRequest;

import entities.*;
import entities.Error;
import Crypto.PasswordHash;

import java.security.CryptoPrimitive;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public LoginResponse loginUser(@FormParam("email") String email, @FormParam("password") String pass)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		//TODO Maybe return a User item?
		String userId = SQLConnection.getIduserFromEmail(email);
		LoginResponse response = new LoginResponse(new Error(), "Invalid password or username.", userId);
		String hash_db = SQLConnection.getHashToEmail(email);
		if (hash_db != null) {
//			Boolean isValid = Crypto.PasswordHash.validatePassword(pass, hash_db);
			System.out.println(email + "," + pass);
			Boolean isValid = pass.equals(hash_db);
			System.out.println(pass + ", " + hash_db + ", " + isValid);
			if (isValid) {
				response.setStatus("Ok.");
			}
		}
		return response;
	}

	@Path("create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public LoginResponse createUser(@FormParam("name") String name, 
			@FormParam("email") String email,
			@FormParam("password") String pass) {
		if (SQLConnection.createUser(name, email, pass)) {
			return new LoginResponse(new Error(), "Ok.", SQLConnection.getIduserFromEmail(email));
		}
		else {
			System.out.println("Failed to create user.");
			return new LoginResponse(new Error(), "Failed to create user.", null);
		}
	}

	@Path("delete")
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void deleteUser(@FormParam("id") Integer id) {
		System.out.println(id.toString());
	}

	@Path("info/{id}")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getUser(@PathParam("id") Integer id) {
		System.out.println("Requested information about " + id);
		try {
			return SQLConnection.getUsernameFromIduser("1").toString();
		} catch (Exception e) {
			return "User does not exist.";
		}
	}

	@Path("exists")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Boolean isUser(@FormParam("id") Integer id, @FormParam("email") String email) {
		if (!id.equals(null)) {
			return !getUser(id).equals(null);
		} else {
			return !SQLConnection.getIduserFromEmail(email).equals(null);
		}
	}

	@Path("getEvents/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getEventsFromUser(@PathParam("id") Integer user_id) {
		// List<Event> a = new ArrayList<Event>();
		// a.add(new Event("Event1Name", 1, null, "Description 1.", 1));
		// a.add(new Event("Event2Name", 2, null, "Description 2.", 1));
		// return a;

		if (isUser(user_id, null)) {
			List<String> eventsListString = SQLConnection.getEventsFromIduser(user_id.toString());
			List<Event> eventsList = new ArrayList<Event>();

			for (String thisEventId : eventsListString) {
				eventsList.add(SQLConnection.getEventFromIdevent(thisEventId.toString()));
			}
			return eventsList;
		} else {
			System.out.println("getEventsFromUser got a not existing User");
			return null;
		}

	}
}
