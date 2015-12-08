package restRequest;

import entities.*;
import entities.Error;
import Crypto.PasswordHash;

import java.security.CryptoPrimitive;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
	public LoginResponse UserLogin(@FormParam("email") String email, 
			@FormParam("password") String pass)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		LoginResponse response = new LoginResponse(new Error(), "", "1");
		String hash_db = SQLConnection.getHashToEmail(email);
		if (hash_db != null) {
//			Boolean isValid = Crypto.PasswordHash.validatePassword(pass, hash_db);
			System.out.println(email+","+pass);
			Boolean isValid = pass.equals(hash_db);
			System.out.println(pass + ", " + hash_db + ", " + isValid);
			if (isValid) {
				response.setStatus("Ok.");
			}
			else {
				response.setStatus("Invalid password or username.");
			}
		} else {
			response.setStatus("Invalid password or username.");
		}
		return response;
	}

	@Path("create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public EventResponse createEventForm(@FormParam("name") String name, @FormParam("users") String users) {
		// look up user

		Event event = new Event(name, 1, null, "Custom description.", 1);
		Error error = new Error();
		return new EventResponse(event, error);
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
