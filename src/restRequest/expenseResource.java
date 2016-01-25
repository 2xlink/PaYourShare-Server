package restRequest;

import entities.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    private boolean isUserInEvent(User user, Event event) {
    	return SQLConnection.getUserFromEvent(event).contains(user);
    }
    
    @Path("create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StatusResponse createExpense(simpleRequest req) {
        System.out.println("ExpenseCreate called.");
        if 	   (req.getToken() == null || req.getAmount() == null || 
        		req.getName() == null || req.getId() == null || 
        		req.getType() == null || req.getEventId() == null) {
        	System.out.println("CreateExpense called without providing all parameters.");
        	return new StatusResponse("false");
        }
        User user = SQLConnection.getUserFromToken(req.getToken());
        if (user == null) {
            return new StatusResponse("false");
        }
        
        String userId = user.getId();
        String creatorId = userId;
        Event event = SQLConnection.getEventFromIdevent(req.getEventId());
        if (!isUserInEvent(user, event)) {
        	System.out.println("User is not in event.");
        	return new StatusResponse("false");
		}
        
        // create the new expense
        System.out.println(creatorId+ req.getAmount()+ req.getName()+ req.getId()+ req.getType()+ req.getEventId());
        
        System.out.println("Adding shares");
        List<Share> shares = new LinkedList<>();
        for (ShareSimple thisshare : req.getShares()) {
            System.out.println("Share for " + thisshare.getId());
            Map<User, String> thismap = new HashMap<>();
            thismap.put(SQLConnection.getUserFromIduser(thisshare.getId()), 
                    thisshare.getShare());
            shares.add(new Share(thismap));
        }
        Expense expense = new Expense(creatorId, req.getAmount(), req.getName(), req.getId(), req.getType(), req.getEventId(), shares);
        
        boolean noerrors = true;
        try {
        	noerrors = SQLConnection.createExpense(expense);
		} catch (Exception e) {
			System.out.println("Error while accessing database.");
			e.printStackTrace();
		}
        System.out.println("Created Expense? " + noerrors);

        return noerrors? new StatusResponse("true") : new StatusResponse("false");
    }
    
    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public StatusResponse updateExpense(simpleRequest req) { 
    	System.out.println("updateExpense called.");
        if 	   (req.getToken() == null || req.getAmount() == null || 
        		req.getName() == null || req.getId() == null || 
        		req.getType() == null || req.getEventId() == null) {
        	System.out.println("UpdateExpense called without providing all parameters.");
        	return new StatusResponse("false");
        }
        User user = SQLConnection.getUserFromToken(req.getToken());
        if (user == null) {
            return new StatusResponse("false");
        }
        
        String userId = user.getId();
        String creatorId = userId;
        Event event = SQLConnection.getEventFromIdevent(req.getEventId());
        if (!isUserInEvent(user, event)) {
        	System.out.println("User is not in event.");
        	return new StatusResponse("false");
		}
        
        System.out.println("Adding shares");
        List<Share> shares = new LinkedList<>();
        for (ShareSimple thisshare : req.getShares()) {
            System.out.println("Share for " + thisshare.getId());
            Map<User, String> thismap = new HashMap<>();
            thismap.put(SQLConnection.getUserFromIduser(thisshare.getId()), 
                    thisshare.getShare());
            shares.add(new Share(thismap));
        }
        Expense expense = new Expense(creatorId, req.getAmount(), req.getName(), req.getId(), req.getType(), req.getEventId(), shares);
        
        boolean noerrors;
        noerrors = SQLConnection.updateExpense(expense);

        return noerrors? new StatusResponse("true") : new StatusResponse("false");
    }
//
//  
//  @Path("delete")
//  @POST
//  @Produces(MediaType.TEXT_HTML)
//  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//  public void deleteEvent(simpleRequest req) {
//      String id = req.getId();
//      System.out.println(id.toString());
//  }
//  
//  @Path("get/{id}")
//  @GET
//  @Produces(MediaType.TEXT_HTML)
//  public String getEvent(@PathParam("id") Integer id) {
//      System.out.println("Requested information about " + id);
//      return id.toString();
//  }
}
