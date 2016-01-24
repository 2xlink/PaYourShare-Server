package restRequest;

import entities.*;
import entities.Error;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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
    public StatusResponse createExpense(simpleRequest req) {
        System.out.println("ExpenseCreate called.");
        User user = SQLConnection.getUserFromToken(req.getToken());
        if (user == null) {
            return new StatusResponse("false");
        }
        
        String userId = user.getId();
        String creatorId = userId;
        // maybe also check here if the user is allowed to do this
        
        // create the new expense
        System.out.println(creatorId+ req.getAmount()+ req.getName()+ req.getId()+ req.getType()+ req.getEventId());
        Expense expense = new Expense(creatorId, req.getAmount(), req.getName(), req.getId(), req.getType(), req.getEventId());
        
        System.out.println("Adding shares");
        List<Share> shares = new LinkedList<>();
        for (ShareSimple thisshare : req.getShares()) {
            System.out.println("Share for " + thisshare.getId());
            Map<User, String> thismap = new HashMap<>();
            thismap.put(SQLConnection.getUserFromIduser(thisshare.getId()), 
                    thisshare.getShare());
            shares.add(new Share(thismap));
        }
        expense.setShares(shares);
        
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
        User user = SQLConnection.getUserFromToken(req.getToken());
        if (user == null) {
        	System.out.println("Token is not valid " + req.getToken());
            return new StatusResponse("false"); 
        }
        
        String userId = user.getId();
        String creatorId = userId;
        // maybe also check here if the user is allowed to do this
        
        Expense expense = new Expense(creatorId, req.getAmount(), req.getName(), req.getId(), req.getType(), req.getEventId());
        
        System.out.println("Adding shares");
        List<Share> shares = new LinkedList<>();
        for (ShareSimple thisshare : req.getShares()) {
            System.out.println("Share for " + thisshare.getId());
            Map<User, String> thismap = new HashMap<>();
            thismap.put(SQLConnection.getUserFromIduser(thisshare.getId()), 
                    thisshare.getShare());
            shares.add(new Share(thismap));
        }
        expense.setShares(shares);
        
        boolean noerrors;
        noerrors = SQLConnection.createExpense(expense);

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
