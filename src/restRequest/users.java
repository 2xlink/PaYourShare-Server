package restRequest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entities.Expense;
import entities.SQLConnection;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/users")
public class users {

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return getUsers();
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return getUsers();
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return getUsers();
	}

	private String getUsers() {
		// return JSON object here
		//SQLConnection.printNameList();
		//SQLConnection.createEvent("Zug", "3");
		//SQLConnection.getHashToEmail("test2@test.de");
		//SQLConnection.getUsernameFromIduser("2");
		//SQLConnection.getEventnameFromIdevent("2");
		//SQLConnection.createUser("Gustaf", "test4@test.de", "pw4");
		//SQLConnection.getEventsFromIduser("5668cc25-8862-49c3-afa3-37ec048c7b61");
		//SQLConnection.getIduserFromEmail("test2@test.de");
		//SQLConnection.getEventFromIdevent("5cc8597f-fce9-40e1-9935-c5836d1aa6f0");
		//SQLConnection.getUserFromIdevent("1");
		//SQLConnection.addUserToEvent("test3@test.de", "1");
		Expense expense = new Expense("1", "50", "Test2", "002", "platzhalter", "1");
		//SQLConnection.createExpense(expense);
		SQLConnection.deleteExpense(expense);
		//SQLConnection.addUserToExpense("002", "2", "10");
		//SQLConnection.deleteUserFromEvent("1","3");
		
		
		return "Alice, Bob";
	}

}