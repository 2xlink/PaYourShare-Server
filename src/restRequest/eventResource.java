package restRequest;

import entities.*;

import java.util.HashMap;
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

import org.glassfish.jersey.server.JSONP;

@Path("/event")
public class eventResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getTodosBrowser() {
		return ("test");
	}

//	@Path("create")
//	@POST
//	@Produces(MediaType.TEXT_HTML)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public String createEvent(@FormParam("email") String email, @FormParam("password") String pw,
//			@Context HttpServletResponse servletResponse) {
//		System.out.println(email + pw);
//		return "{\"email\": " + email + ", \"password\" : " + pw + "}";
//	}

	@Path("create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public UserTemp createEvent1() {
		UserTemp t_user = new UserTemp();
		t_user.setEmail("email");
		t_user.setPassword("pw");
		System.out.println(t_user.getEmail() + t_user.getPassword());
//		return "{\"email\": " + email + ", \"password\" : " + pw + "}";
		return new UserTemp("email", "pass");
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
