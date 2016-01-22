package entities;

import entities.SQLNames;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;
	 
public class SQLConnection {
 
  private static Connection conn = null;
 
  private static String dbHost = SQLNames.dbHost;
  private static String dbPort = SQLNames.dbPort;
  private static String database = SQLNames.database;
  private static String dbUser = SQLNames.dbUser;
  private static String dbPassword = SQLNames.dbPassword;
  
  private SQLConnection() {
	  try {
      // Datenbanktreiber f�r ODBC Schnittstellen laden.
      // F�r verschiedene ODBC-Datenbanken muss dieser Treiber
      // nur einmal geladen werden.
      Class.forName("com.mysql.jdbc.Driver");
      // Verbindung zur Datenbank herstellen
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
 
 
  public static boolean createEvent(String name, String idcreator, String idevent, String description){
	  String ideventuser = UUID.randomUUID().toString();
	  boolean check = false;
	  //String idevent = UUID.randomUUID().toString();
	  conn = getInstance();
	 
	  if(conn != null)
	    {
	      try {
	 
	        String sql = "INSERT INTO event(idevent, name, description,idmoderator) " +
	                     "VALUES(?, ?, ?, ?)";
	        PreparedStatement preparedStatement = conn.prepareStatement(sql);
	        // Erstes Fragezeichen durch "firstName" Parameter ersetzen
	        preparedStatement.setString(1, idevent);
	        preparedStatement.setString(2, name);
	        preparedStatement.setString(3, description);
	        preparedStatement.setString(4, idcreator);
	        // SQL ausf�hren.
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
	            //System.out.println(password);
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
	  System.out.println(check);
	  return check;
  }
  
  public static boolean deleteUserFromEvent(String idevent, String iduser){
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null && existUser(iduser)){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "DELETE FROM eventuser " +
	                    "WHERE iduser = '" + iduser + "'" +
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
			  
			  String sql = "Select iduser, name, email From user u "
			  		 	+ "Join eventuser eu on u.iduser = eu.iduser"
					  	+ "where idevent =" + "'" + event.getId() + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  user.setId(result.getString(1));
				  user.setEmail(result.getString(2));
				  list.add(user);
				  System.out.println(user.getId());
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
	  				" WHERE idevent = " +  "'" + event.getId() + "'";
			System.out.println(sql);
			query.executeUpdate(sql);  
			  for (User user : event.getUsers()){
		           if(existUserEvent(user.getId(), event.getId()) == false){
		        	   addUserToEvent(user.getEmail(), event.getId());
		           }
		           for(User user2 : getUserFromEvent(event)){
		        	   if(event.getUsers().contains(user2) == false){
		        		   deleteUserFromEvent(event.getId(), user2.getId());
		        	   }
		           }		                      
		        }
			
			
			check = false;
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
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
		  
	  //System.out.println(liste.get(0));  
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
			  
			  String sql = "Select idevent, name, description,idmoderator From event where idevent =" + "'" + idevent + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  event.setId(result.getString(1));
				  event.setName(result.getString(2));
				  event.setDescription(result.getString(3));
				  event.setCreatorId(result.getString(4));	  
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }		 
	  }
	  if(event.getId() == null) event = null;
	  return event;
  }
  
  public static boolean existUserEvent(String iduser, String idevent){
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
  
  public static boolean existUser(String iduser){
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
  
  public static boolean existExpense(String idexpense){
	  boolean check = true;
	  ArrayList<String>	liste = new ArrayList<String>();
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select idexpense From ausgaben where idexpense = " + "'" + idexpense + "'";
			  ResultSet result = query.executeQuery(sql);
			  System.out.println(sql);
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
  
  public static boolean existExpenseUser(String iduser, String idexpense){
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
				  liste.add(result.getString("ideventuser"));
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
	  
	  conn = getInstance();
	 
	  if(conn != null)
	    {
	      try {
	 
	        String sql = "INSERT INTO ausgaben(idexpense, idevent, name, betrag, idcreator) " +
	                     "VALUES(?, ?, ?, ?, ?)";
	        PreparedStatement preparedStatement = conn.prepareStatement(sql);
	        // Erstes Fragezeichen durch "firstName" Parameter ersetzen
	        preparedStatement.setString(1, expense.getExpenseId());
	        preparedStatement.setString(2, expense.getEventId());
	        preparedStatement.setString(3, expense.getName());
	        preparedStatement.setString(4, expense.getAmount());
	        preparedStatement.setString(5, expense.getCreatorId());
	        // SQL ausf�hren.
	        preparedStatement.executeUpdate();
	        
	        String sql2 = "INSERT INTO ausgabenuser(idexpenseuser, iduser, idexpense, betrag) " +
                    "VALUES(?, ?, ?, ?)";
	        PreparedStatement prepStatment2= conn.prepareStatement(sql2);
	        prepStatment2.setString(1, idexpenseuser);
	        prepStatment2.setString(2, expense.getCreatorId());
	        prepStatment2.setString(3, expense.getExpenseId());
	        prepStatment2.setString(4, expense.getAmount());
	        prepStatment2.executeUpdate();
	        
	        preparedStatement.close();
	        prepStatment2.close();
	        
	 
	      } catch (SQLException e) {
	    	  e.printStackTrace();	        
	      }
	    }
	  return true;
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
				liste.add(new Expense(resultsql.getString("idcreator"),resultsql.getString("betrag"),resultsql.getString("name"),resultsql.getString("idexpense"),"0",idevent));
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	  }
	  
	 // Expense expense = new Expense(creatorId,amount, name, expenseId, "0", idevent);
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
			  				" Where idexpense = " +  "'" + expense.getExpenseId() + "'";
			  int i=0;
			  
			  for (Entry<User, String> entry : expense.getShares().get(i).getMap().entrySet()){
		            i++;
		            
		            String sql2 = "Update ausgabenuser SET betrag = " + "'" + entry.getValue() + "'" +
		            				" WHERE idexpense = " + "'" + expense.getExpenseId() +"'" +
		            				"AND iduser = " + "'" + entry.getKey().getId() + "'";
		            
		            System.out.println(entry.getValue());
		            query.executeUpdate(sql2);
		            
		        }
			  
			  
			  query.executeUpdate(sql);
			  check = true;			  			  
			  
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}		  		  
	  }	  
	  return check; 
  }
  
  public static boolean deleteExpense(Expense expense){
	  conn = getInstance();
	  boolean check = false;
	  if(conn != null && existExpense(expense.getExpenseId())){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "DELETE FROM ausgaben " +
	                    "WHERE idexpense = '" + expense.getExpenseId() + "'";
			  String sql2 = "DELETE FROM ausgabenuser " +
	                    "Where idexpense = " + "'" + expense.getExpenseId() + "'";
			  query.executeUpdate(sql);
		      query.executeUpdate(sql2);
		      check = true; 
		       
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
	  }
	  
	  return check;	  
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
			// TODO: handle exception
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
	  
	  if(conn != null){
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
  
  
  public static User getUserFromToken(String token){
	  User user = new User();	  
	  conn = getInstance();	  
	  
	  if(conn != null){
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
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }		 
	  }
	  if(user.getId() == null) user = null;
	  System.out.println(user.getName());
	  System.out.println(user.getId());
	  System.out.println(user.getEmail());
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
  
}