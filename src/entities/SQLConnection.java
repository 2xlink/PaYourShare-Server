package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
	 
public class SQLConnection {
 
  private static Connection conn = null;
 
  private static String dbHost = "localhost";
  private static String dbPort = "3306";
  private static String database = "payourshare";
  private static String dbUser = "root"; 
  private static String dbPassword = "";
  
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
    }
  }
 
  private static Connection getInstance()
  {
    if(conn == null)
      new SQLConnection();
    return conn;
  }
 
  /**
   * Schreibt die Namensliste in die Konsole
   * Nur zum testen
   */
  public static void printNameList()
  {
    conn = getInstance();
 
    if(conn != null)
    {
      // Anfrage-Statement erzeugen.
      Statement query;
      try {
        query = conn.createStatement();
 
        // Ergebnistabelle erzeugen und abholen.
        String sql = "SELECT name FROM user ";
        String sql2 = "Select us.name" +
        			" From user us" +
        			" join ausgabenuser au on au.iduser = us.iduser" +
        			" join ausgaben ag on au.idbetrag = ag.idbetrag" +
        			" Where ag.name = 'Ticket'";
        ResultSet result = query.executeQuery(sql);

        // Ergebniss�tze durchfahren.
        while (result.next()) {
          String name = result.getString("name"); // Alternativ: result.getString(1);
          //System.out.println(name);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
  
  
  
  public static void createEvent(String name, String idcreator){
	  String idevent= UUID.randomUUID().toString();
	  String ideventuser = UUID.randomUUID().toString();
	  
	  conn = getInstance();
	 
	  if(conn != null)
	    {
	      try {
	 
	        String sql = "INSERT INTO event(idevent, name, idmoderator) " +
	                     "VALUES(?, ?, ?)";
	        PreparedStatement preparedStatement = conn.prepareStatement(sql);
	        // Erstes Fragezeichen durch "firstName" Parameter ersetzen
	        preparedStatement.setString(1, idevent);
	        preparedStatement.setString(2, name);
	        preparedStatement.setString(3, idcreator);
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
	    }
	  
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
	  	      
	      //if(password == null) System.out.println("DEPP!!");

	  }
	  
	  return password;
  }
  
  public static boolean addUserToEvent(String idevent, String iduser){
	  return true;
  }
  
  public static boolean deleteUserFromEvent(String idevent, String iduser){
	  return true;	  
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
						+ "WHERE us.iduser = " + iduser;
			ResultSet result = query.executeQuery(sql);
			
			while(result.next()){
				  liste.add(result.getString("idevent"));
			  }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		  
	  }
	  
	  System.out.println(liste.get(0));
	  
	  return liste;
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
			
		}
	  }
	  
	 /*
	  for(int i = 0; i< liste.size(); i++){
		  System.out.println(liste.get(i));
	  }
	*/
	  
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
	  
	  //System.out.println(liste.get(0));  
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
  
  
  public static boolean createUser(String name, String email, String password){
	  boolean check = false;
	  conn = getInstance();
	  String iduser = UUID.randomUUID().toString();
	  
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
			  
			  String sql2 = "INSERT INTO userlogin(iduser, password, email) " +
	                     "VALUES(?, ?, ?)";
			  PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
			  preparedStatement2.setString(1, iduser);
			  preparedStatement2.setString(2, password);
			  preparedStatement2.setString(3, email);
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
	  
	  System.out.println(liste.get(0));  
	  return liste.get(0);
  }
  
  public static Event getEventFromIdevent(String idevent){
	  
	  Event event = new Event();
	  
	  conn = getInstance();	  
	  
	  if(conn != null){
		  Statement query;
		  try{
			  query = conn.createStatement();
			  
			  String sql = "Select idevent, name, idmoderator From event where idevent =" + "'" + idevent + "'";
			  ResultSet result = query.executeQuery(sql);
			  
			  while(result.next()){
				  event.setId(result.getString(1));
				  event.setName(result.getString(2));
				  event.setCreatorId(result.getString(3));
			  }
			  
		  }catch(SQLException e){
			  e.printStackTrace();
		  }
			 
	  }
	  
	  System.out.println(event.getId());
	  System.out.println(event.getName());
	  System.out.println(event.getCreatorId());
	  
	  return event;
  }
}