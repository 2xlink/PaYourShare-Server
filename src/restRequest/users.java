package restRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entities.Event;
import entities.Expense;
import entities.SQLConnection;
import entities.Share;
import entities.User;

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
		//SQLConnection.deleteUserFromEvent("1","3");
		//SQLConnection.getUserFromToken("token3");
		//SQLConnection.getUserFromIduser("3");
		Event event = new Event("Zug1","2",null,"platzhalter", "1", null);
		User user = new User("1","test1@test.de");
		User user2 = new User("2","test2@test.de");
		List<User> list = new LinkedList<User>();
		list.add(user);
		list.add(user2);
		event.setUsers(list);
		//SQLConnection.updateEvent(event);
		 
		Expense expense = new Expense("2", "60", "Test2erfolgreich", "002", "platzhalter", "1", null);
		
		Map<User, String> map = new HashMap<>();
		map.put(user, "50");
		map.put(user2, "10");
		Share share = new Share();
		share.setMap(map);
		List<Share> list2 = new LinkedList<Share>();
		list2.add(share);
		expense.setShares(list2);
		
		//SQLConnection.createExpense(expense);
		//SQLConnection.deleteExpense(expense);
		//SQLConnection.addUserToExpense("002", "3", "676");
		//SQLConnection.deleteUserFromExpense("002", "3");
		//SQLConnection.updateExpense(expense);
		SQLConnection.getExpenseFromIdevent("2");
		
		
		//SQLConnection.setToken("1", "Token1erfolg");
		//SQLConnection.deleteToken("token2");
		//SQLConnection.getUserFromToken("token3");
		
		return "Alice, Bob";
	}

}