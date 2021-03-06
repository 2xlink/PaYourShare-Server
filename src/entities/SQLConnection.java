package entities;

import entities.SQLNames;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.swing.plaf.synth.SynthSpinnerUI;
	 
public class SQLConnection {
 
  private static Connection conn = null;
 
  private static String dbHost = SQLNames.dbHost;
  private static String dbPort = SQLNames.dbPort;
  private static String database = SQLNames.database;
  private static String dbUser = SQLNames.dbUser;
  private static String dbPassword = SQLNames.dbPassword;
  
  private SQLConnection() {
	  try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
          + dbPort + "/" + database + "?" + "user=" + dbUser + "&"
          + "password=" + dbPassword);
    } catch (ClassNotFoundException e) {
      System.out.println("Treiber nicht gefunden");
    } catch (SQLException e) {
      System.out.println("Connect nicht moeglich");
      System.out.println(e);
    }
  }
 
  private static Connection getInstance()
  {
    if(conn == null)
      new SQLConnection();
    return conn;
  }
 
 
  public static boolean createEvent(String name, String idcreator, String idevent, String description, String version){
	  String ideventuser = UUID.randomUUID().toString();
	  boolean check = false;
	  //String idevent = UUID.randomUUID().toString();
	  conn = getInstance();
	 
	  if(conn != null)
	    {
	      try {
	 
	        String sql = "INSERT INTO event(idevent, name, description, idmoderator, version) " +
	                     "VALUES(?, ?, ?, ?, ?)";
	        PreparedStatement preparedStatement = conn.prepareStatement(sql);
	        preparedStatement.setString(1, idevent);
	        preparedStatement.setString(2, name);
	        preparedStatement.setString(3, description);
	        preparedStatement.setString(4, idcreator);
	        preparedStatement.setString(5, version);
	        preparedStatement.executeUpdate();
	        
	        String sql2 = "INSERT INTO eventuser(ideventuser, idevent, iduser) " +
                    "VALUES(?, ?, ?)";
	        PreparedStatement prepStatment2= conn.prepareStatement(sql2);
	        prepStatment2.setString(1, ideventuser);
	        prepStatment2.setString(2, idevent);
	        prepStatment2.setString(3, idcreator);
	        prepStatment2.executeUpdate();
	        
	 
	      } catch (SQLException e) {
	    	  e.printStackTrace();	        
	      }
	      check = true;
	    }
	  return check;
  }
  
  public static String getHashToEmail(String email){
	  String password = null;
	  
	  conn = getInstance();
	  if(conn != null){
				  
		  Statement query;
	      try {
	        query = conn.createStatement();
	        
	        String sql = ("Select password From userlogin where email = " + "'" + email + "'");
	        
	        
	        ResultSet result = query.executeQuery(sql);
	        
	        while (result.next()) {
	            password = result.getString("password"); // Alternativ: result.getString(1);
	          }
	        
	      } catch (SQLException e) {
	        e.printStackTrace();
	      }
	  }
	  
	  return password;
  }
  
  public static boolean addUserToEvent(String email, String idevent){
	  conn = getInstance();
	  boolean check = false;
	  String iduser = getIduserFromEmail(email);
	  String ideventuser = UUID.randomUUID().toString();
	  System.out.println("Iduser: " + iduser);
	  System.out.println("Email: " + email);
	  System.out.println("ExistUser: " + existUserEvent(iduser, ideventuser));
	  if(conn != null && existUserEvent(iduser, idevent)){
		  Statement query;
		  try{
			  query = conn.createStatement();

			  String sql = "INSERT INTO eventuser(ideventuser, idevent, iduser) " +
	                    "VALUES(?, ?, ?)";
		        PreparedStatement prepStatment2= conn.prepareStatement(sql);
		        prepStatment2.setString(1, ideventuser);
		        prepStatment2.setString(2, idevent);
		        prepStatment2.setString(3, iduser);
		        prepStatment2.executeUpdate();

		       check = true; 
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }

	  return check;
  }
  
  public static boolean deleteUserFromEvent(String idevent, String iduser){
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null && (existUser(iduser) == false)){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  String sql = "DELETE FROM eventuser " +
	                    " WHERE iduser = '" + iduser + "'" +
	                    " AND idevent = '" + idevent + "'";
			  query.executeUpdate(sql);
		       
		      check = true; 
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return check;	  
  }
  
  public static ArrayList<String> getEventsFromIduser(String iduser){
	  conn = getInstance();
	  
	  ArrayList <String> liste = new ArrayList<String>();
	  
	  if(conn != null){
		  Statement query;
		  try {
			query = conn.createStatement();
			  
			String sql = "SELECT ev.idevent FROM user us "
						+ "join eventuser eu on us.iduser = eu.iduser "
						+ "join event ev on ev.idevent = eu.idevent "
						+ "WHERE us.iduser = " +  "'" + iduser + "'";
			ResultSet result = query.executeQuery(sql);
			
			while(result.next()){
				  liste.add(result.getString("idevent"));
			  }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}  
	  }
	  
	  return liste;
  }
  
  public static ArrayList<User> getUserFromEvent(Event event){
	  User user = new User();
	  ArrayList<User> list = new ArrayList<User>();
	  conn = getInstance();
	  
	  if(conn != null){
		  Statement query;
		  
		  try{
			  query = conn.createStatement();
			  
			  String sql = "SELECT u.iduser, u.name, u.email From user u "
			  		 	+ " JOIN eventuser eu on u.iduser = eu.iduser"
					  	+ " WHERE eu.idevent = " + "'" + event.getId() + "'";
			  ResultSet result = query.executeQuery(sql);
			  while(result.next()){
				  list.add(new User(result.getString(1), result.getString(3)));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }  
	  return list;
  }
  
  public static boolean updateEvent(Event event){
	  boolean check = false;
	  conn = getInstance();
	    
	  if(conn != null){
		 Statement query;
		 
		 try {
			query = conn.createStatement();
			
			String sql = "UPDATE event SET name = " + "'" + event.getName() + "'" +
	  				" ,description = " + "'" + event.getDescription() + "'" +
					" ,version = " + "'" + event.getVersion() + "'" +
	  				" WHERE idevent = " +  "'" + event.getId() + "'";		
			query.executeUpdate(sql);
			  
			
			  //Add User to Event
			  for (User user : event.getUsers()){
		           if(existUserEvent(user.getId(), event.getId())){
		        	   addUserToEvent(user.getEmail(), event.getId());  
		           }
		      }
			  //Add User to Expense
			  for(Expense expense : event.getExpenses()){
				  for(ShareSimple share : expense.getShares()){	  
					  if(existExpenseUser(share.getId(), expense.getId()))
					  addUserToExpense(expense.getId(), share.getId(), share.getShare());
				  }
		       }
			  //Delete User from Event
			  for(String iduser : getUserFromIdevent(event.getId())){
				  boolean contains = false;
				  for(int i=0; i<event.getUsers().size(); i++){
					  if(iduser.equals(event.getUsers().get(i).getId())){
						  contains = true;
					  }
				  }
				  if(contains == false){
					  deleteUserFromEvent(event.getId(), iduser);
					  for(Expense expense : event.getExpenses()){
						  for(ShareSimple share : expense.getShares()){
							  deleteUserFromExpense(expense.getId(), iduser);
						  }
					  }
				  }
			  }
			  //Delete User from Expense
			  for(Expense expense : getExpenseFromIdevent(event.getId())){
				  for(ShareSimple share : getShareFromIdexpense(expense.getId())){
					  boolean contains = false;
					  for(Expense expenseapp : event.getExpenses()){
						  for(ShareSimple shareapp : expenseapp.getShares()){
							  if(share.getId().equals(shareapp.getId())){
								  contains = true;								  
							  }
						  }
					  }
					  if(contains == false){
						  deleteUserFromShare(share.getId(), expense.getId());
					  }
				  }
			  }
			  //Add Expense to Event
			  for(Expense expense : event.getExpenses()){
				  if(!existExpenseEvent(expense.getId(), expense.getEventId())){
					  createExpense(expense);
				  }
			  }
			  //Delete Expense to Event
			  for(Expense expense : getExpenseFromIdevent(event.getId())){
				  boolean contains = false;
				  for(Expense expenseapp : event.getExpenses()){
					  if(expense.getId().equals(expenseapp.getId())){
						  contains = true;
					  }
				  }
				  if(!contains){
					  deleteExpense(expense);
				  }
			  }
			  
			  
			check = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	  
	  }
	  
	  return check;
  }
  
  private static boolean deleteEvent(Event event){
	  boolean check = false;
	  conn = getInstance();
	  
	  if(conn != null){
		  Statement query;
		  
		  try{
			  query = conn.createStatement();
			  
			  String sql = "DELETE FROM event WHERE idevent= " + "'" + event.getId() + "'";
			  String sql2 = "DELETE FROM eventuser WHERE idevent = " + "'" + event.getId() + "'";
			  
			  query.executeUpdate(sql);
			  query.executeUpdate(sql2);
			  
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
		  
		  for(int i=0; i<event.getExpenses().size(); i++){
		  	deleteExpense(event.getExpenses().get(i));
		  }
	  }
	  
	  
	  return check;
  }
  
  public static ArrayList<String> getUserFromIdevent(String idevent){
	  conn = getInstance();
	  ArrayList<String> liste = new ArrayList<String>();
	  
	  if(conn != null){
		  Statement query;
		  try {
			query = conn.createStatement();
			String sql ="Select iduser From eventuser Where idevent = " + "'" + idevent + "'";
			ResultSet result = query.executeQuery(sql);
			
			while(result.next()){
				liste.add(result.getString("iduser"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }

	  
	  return liste;
  }
  
  
  public static String getUsernameFromIduser(String iduser){
	  conn = getInstance();
	  
	  ArrayList<String> liste = new ArrayList<String>();
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select name From user where iduser =" + "'" + iduser + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  liste.add(result.getString("name"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }	 
	  }
	  
	  if(liste.size() == 0) return null;
	   
	  return liste.get(0);
  }
  
  
  public static String getEventnameFromIdevent(String idevent){
	  conn = getInstance();
	  
	  ArrayList<String> liste = new ArrayList<String>();
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select name From event where idevent =" + "'" + idevent + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  liste.add(result.getString("name"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }	 
	  }
 
	  return liste.get(0);
  }
  
  
  public static boolean createUser(String name, String email, String password, String iduser, String token){
	  boolean check = false;
	  conn = getInstance();
	  //String iduser = UUID.randomUUID().toString();
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "INSERT INTO user(iduser, name, email) " +
	                     "VALUES(?, ?, ?)";
			  PreparedStatement preparedStatement = conn.prepareStatement(sql);
			  preparedStatement.setString(1, iduser);
			  preparedStatement.setString(2, name);
			  preparedStatement.setString(3, email);
			  preparedStatement.executeUpdate();
			  
			  String sql2 = "INSERT INTO userlogin(iduser, password, email, token) " +
	                     "VALUES(?, ?, ?, ?)";
			  PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
			  preparedStatement2.setString(1, iduser);
			  preparedStatement2.setString(2, password);
			  preparedStatement2.setString(3, email);
			  preparedStatement2.setString(4, token);
			  preparedStatement2.executeUpdate();
			  
			  check = true;
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return check;
  }
  
  public static String getIduserFromEmail(String email){
	  
	  conn = getInstance();
	  
	  ArrayList<String> liste = new ArrayList<String>();
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select iduser From userlogin where email =" + "'" + email + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  liste.add(result.getString("iduser"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  } 
	  }
	  
	  if(liste.size() == 0) return null;
	   
	  return liste.get(0);
  }
  
  public static Event getEventFromIdevent(String idevent){
	  
	  Event event = new Event();
	  
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select idevent, name, description,idmoderator, version From event where idevent =" + "'" + idevent + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  event.setId(result.getString(1));
				  event.setName(result.getString(2));
				  event.setDescription(result.getString(3));
				  event.setCreatorId(result.getString(4));
				  event.setVersion(result.getString(5));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }		 
	  }
	  if(event.getId() == null) event = null;
	  return event;
  }
  
  private static boolean existUserEvent(String iduser, String idevent){
	  boolean check = true;
	  ArrayList<String>	liste = new ArrayList<String>();
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  String sql = "Select ideventuser From eventuser WHERE idevent = " + "'" + idevent + "'" + " AND iduser = " + "'" + iduser + "'";
			  ResultSet result = query.executeQuery(sql);
			  while(result.next()){
				  liste.add(result.getString("ideventuser"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }		 
	  }
	  if(liste.size() != 0) check = false;
	  return check;
  }
  
  private static boolean existUser(String iduser){
	  boolean check = true;
	  ArrayList<String>	liste = new ArrayList<String>();
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select iduser From user where iduser = " + "'" + iduser + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  liste.add(result.getString("iduser"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }		 
	  }
	  if(liste.size() != 0) check = false;
	  return check;
  }
  
  private static boolean existExpense(String idexpense){
	  boolean check = true;
	  ArrayList<String>	liste = new ArrayList<String>();
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select idexpense From ausgaben where idexpense = " + "'" + idexpense + "'";
			  ResultSet result = query.executeQuery(sql);
			  while(result.next()){
				  liste.add(result.getString("idexpense"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }			 
	  }
	  if(liste.size() != 1) check = false;
	  return check;
  }
  
  private static boolean existExpenseEvent(String idexpense, String idevent){
	  boolean check = true;
	  ArrayList<String>	liste = new ArrayList<String>();
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select idexpense From ausgaben where idexpense = " + "'" + idexpense + "'"
					  		+ " AND idevent = "	+ "'" + idevent + "'";
			  ResultSet result = query.executeQuery(sql);
			  while(result.next()){
				  liste.add(result.getString("idexpense"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }			 
	  }
	  if(liste.size() != 1) check = false;
	  return check;
  }
  
  private static boolean existExpenseUser(String iduser, String idexpense){
	  boolean check = true;
	  ArrayList<String>	liste = new ArrayList<String>();
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select idexpenseuser From ausgabenuser where idexpense = " + "'" + idexpense + "'" + " and iduser = " + "'" + iduser + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  liste.add(result.getString("idexpenseuser"));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
			 
	  }
	  if(liste.size() != 0) check = false;
	  return check;
  }
  
  public static boolean createExpense(Expense expense){
	  //String idexpense= UUID.randomUUID().toString();
	  String idexpenseuser = UUID.randomUUID().toString();
	  boolean check = false;
	  
	  conn = getInstance();
	 
	  if(conn != null)
	    {
	      try {
	 
	        String sql = "INSERT INTO ausgaben(idexpense, idevent, name, description, betrag, idcreator, version) " +
	                     "VALUES(?, ?, ?, ?, ?, ?, ?)";
	        PreparedStatement preparedStatement = conn.prepareStatement(sql);
	        preparedStatement.setString(1, expense.getId());
	        preparedStatement.setString(2, expense.getEventId());
	        preparedStatement.setString(3, expense.getName());
	        preparedStatement.setString(4, expense.getType());
	        preparedStatement.setString(5, expense.getAmount());
	        preparedStatement.setString(6, expense.getCreatorId());
	        preparedStatement.setString(7, expense.getVersion());
	        preparedStatement.executeUpdate();
	        
	        
	        for(ShareSimple share : expense.getShares()){
	        	addUserToExpense(expense.getId(), share.getId(), share.getShare());
	        }
	        
	        /*
	        String sql2 = "INSERT INTO ausgabenuser(idexpenseuser, iduser, idexpense, betrag) " +
                    "VALUES(?, ?, ?, ?)";
	        PreparedStatement prepStatment2= conn.prepareStatement(sql2);
	        prepStatment2.setString(1, idexpenseuser);
	        prepStatment2.setString(2, expense.getCreatorId());
	        prepStatment2.setString(3, expense.getId());
	        prepStatment2.setString(4, expense.getAmount());
	        prepStatment2.executeUpdate();
	        */
	        preparedStatement.close();
	        //prepStatment2.close();
	        
	        check = true;
	 
	      } catch (SQLException e) {
	    	  e.printStackTrace();	        
	      }
	    }
	  return check;
  }
  
  public static ArrayList<Expense> getExpenseFromIdevent(String idevent){  
	  conn = getInstance();
	  ArrayList <Expense> liste = new ArrayList<Expense>();
	  if(conn != null){
		  Statement query;
		  try {
			query = conn.createStatement();
			
			String sql = "SELECT * FROM ausgaben WHERE idevent = " + "'" + idevent + "'";
			ResultSet resultsql = query.executeQuery(sql);
			while(resultsql.next()){
				liste.add(new Expense(
						resultsql.getString("idcreator"),
						resultsql.getString("betrag"),
						resultsql.getString("name"),
						resultsql.getString("idexpense"),
						resultsql.getString("description"),
						idevent,
						resultsql.getString("version"),
						getShareFromIdexpense(resultsql.getString("idexpense"))));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
  
	  return liste;
  }
  
  public static boolean updateExpense(Expense expense){
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null){
		  Statement query;
		  
		  try {
			  query = conn.createStatement();
			  
			  String sql = "Update ausgaben SET name =" + "'" + expense.getName() + "'" +
			  				" ,betrag = " + "'" + expense.getAmount() + "'" +
			  				" ,description = " + "'" + expense.getType() + "'" +
			  				" ,idevent = " + "'" + expense.getEventId() + "'" + 
			  				" ,version= " + "'" + expense.getVersion() + "'" +
			  				" Where idexpense = " +  "'" + expense.getId() + "'";
			  int i=0;
			  
			  for (ShareSimple share  : expense.getShares()){
		            i++;
		            
		            String sql2 = "Update ausgabenuser SET betrag = " + "'" + share.getShare()+ "'" +
		            				" WHERE idexpense = " + "'" + expense.getId() +"'" +
		            				"AND iduser = " + "'" + share.getId() + "'";
		            query.executeUpdate(sql2);
		            
		        }
			  
			  
			  query.executeUpdate(sql);
			  check = true;			  			  
			  
		} catch (SQLException e) {
			e.printStackTrace();
		}		  		  
	  }	  
	  return check; 
  }
  
  public static boolean deleteExpense(Expense expense){
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null && existExpense(expense.getId())){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "DELETE FROM ausgaben " +
	                    "WHERE idexpense = '" + expense.getId() + "'";
			  String sql2 = "DELETE FROM ausgabenuser " +
	                    "Where idexpense = " + "'" + expense.getId() + "'";
			  query.executeUpdate(sql);
		      query.executeUpdate(sql2);
		      check = true; 
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return check;	  
  }
  
  public static List<ShareSimple> getShareFromIdexpense(String idexpense){
	  //User user;
	  ArrayList<User> list = new ArrayList<User>();
	  List<ShareSimple> list2 = new LinkedList<ShareSimple>();
	  conn = getInstance();
	  
	  if(conn != null){
		  Statement query;
		  
		  try{
			  query = conn.createStatement();
			  String sql = "SELECT u.iduser, u.name, u.email, au.betrag From user u "
			  		 	+ " JOIN ausgabenuser au on u.iduser = au.iduser"
					  	+ " WHERE au.idexpense = " + "'" + idexpense + "'";
			  ResultSet result = query.executeQuery(sql);
			  while(result.next()){
				  //list.add(new User(result.getString(1), result.getString(3)));
				  
				  
				  //Map<User,String>map = new HashMap<User,String>();
				  //map.put(new User(result.getString(1), result.getString(3)), result.getString(4));
				  list2.add(new ShareSimple(result.getString(1), result.getString(4)));
				  //list2.add(new Share(new User(result.getString(1), result.getString(3)), result.getString(4)));
				  
			  }
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return list2;
  }
  
  public static boolean deleteUserFromExpense(String idexpense, String iduser){
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null && existExpenseUser(iduser, idexpense)){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "DELETE FROM ausgabenuser " +
	                    "WHERE iduser = '" + iduser + "'" +
	                    " AND idexpense = '" + idexpense + "'";
			  query.executeUpdate(sql);
		       
		      check = true; 
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return check;	
  }
  
  public static boolean addUserToExpense(String idexpense, String iduser, String amount){
	 String idexpenseuser = UUID.randomUUID().toString();
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null && existExpenseUser(iduser, idexpense)){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "INSERT INTO ausgabenuser(idexpenseuser, idexpense, iduser, betrag) " +
	                    "VALUES(?, ?, ?, ?)";
			  PreparedStatement prepStatement2= conn.prepareStatement(sql);
		      prepStatement2.setString(1, idexpenseuser);
		      prepStatement2.setString(2, idexpense);
		      prepStatement2.setString(3, iduser);
		      prepStatement2.setString(4, amount);
		      prepStatement2.executeUpdate();
		       
		      check = true; 
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return check;	
  }
  
  
  // TOKEN
  
  
  public static boolean setToken(String iduser, String token){
	  boolean check = false;
	  
	  conn = getInstance();
	  
	  if(conn != null){
		  Statement query;
		  
		  try {
			
			  query = conn.createStatement();
			  
			  String sql = "Update userlogin SET token = " + "'" + token + "'" +
					  		" WHERE iduser = " + "'" + iduser + "'";
			  	  
			  query.executeUpdate(sql);
			  check = true;
			  
		} catch (SQLException e) {
			e.printStackTrace();
		}  	  
	  }  
	  return check;
  }
  
  public static String getToken(String iduser){
	  String result = null;
	  conn = getInstance();
	  
	  if(conn != null){
		  Statement query;
		  
		  try {
			query = conn.createStatement();
			
			String sql = "SELECT token FROM userlogin WHERE iduser = " + "'" + iduser + "'";
			ResultSet resultset = query.executeQuery(sql);
			
			while(resultset.next()){
				result = resultset.getString(0);
			}
		  
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }

	return result;  
  }
  
  public static boolean deleteToken(String token){
	  boolean check = false;
	  conn = getInstance();
	  
	  if(conn != null && existTotoken(token)){
		  Statement query;
		  
		  try {
			  query = conn.createStatement();
			  
			  String sql = "UPDATE userlogin SET token = 'null' WHERE token = " + "'" + token + "'";
			  query.executeUpdate(sql);
			  
			  check = true;		  
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
	  return check;
  }
  
  private static boolean existTotoken(String token){
	  boolean check = false;
	  conn = getInstance();
	  String iduser = null;
	  
	  if(conn != null){
		  Statement query;
		  
		  try {
			  query = conn.createStatement();
			  
			  String sql = "SELECT iduser FROM userlogin WHERE token = " + "'" + token + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  iduser = result.getString("iduser");
			  }
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	  }
	  if(iduser != null) check = true;
	  return check;
  }
  
  public static User getUserFromToken(String token){
	  User user = new User();
	  boolean check = false;
	  conn = getInstance();	  
	  if(conn != null && existTotoken(token)){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select iduser From userlogin" 
					  		+ " where token =" + "'" + token + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  user.setId(result.getString(1));
			  }
			  
			  user = getUserFromIduser(user.getId());
			  check = true;
		  }catch(SQLException e){
			  e.printStackTrace();
		  }		 
	  }
	  if(check == false) user = null;
	  return user;
  }
  
  public static User getUserFromIduser(String iduser){
	  User user = new User();	  
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select iduser, name, email From user" 
					  		+ " where iduser =" + "'" + iduser + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  user.setId(result.getString(1));
				  user.setName(result.getString(2));
				  user.setEmail(result.getString(3));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }		 
	  }

	  if(user.getId() == null) user = null;	  
	  return user;
	  
  }
  
  public static boolean deleteUserFromShare(String iduser, String idexpense){
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null && !existExpenseUser(iduser, idexpense)){
		  Statement query;
		  try{
			  query = conn.createStatement();

			  String sql = "DELETE FROM ausgabenuser " +
	                    "WHERE iduser = '" + iduser + "'" +
	                    " AND idexpense = '" + idexpense + "'";
			  query.executeUpdate(sql);
		       
		      check = true; 
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return check;
  }
}