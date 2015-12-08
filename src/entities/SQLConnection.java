package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
	 
public class SQLConnection {
 
  private static Connection conn = null;
 
  private static String dbHost = "localhost";
  private static String dbPort = "3306";
  private static String database = "payourshare";
  private static String dbUser = "payourshare"; 
  private static String dbPassword = "";
  
  private SQLConnection() {
	  try {
      // Datenbanktreiber f�r ODBC Schnittstellen laden.
      // F�r verschiedene ODBC-Datenbanken muss dieser Treiber
      // nur einmal geladen werden.
      Class.forName("com.mysql.jdbc.Driver");
      
      // Verbindung zur ODBC-Datenbank 'sakila' herstellen.
      // Es wird die JDBC-ODBC-Br�cke verwendet.
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
          System.out.println(name);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void createEvent(String name, String idcreator){
	  String idevent="3";
	  
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
	 
	      } catch (SQLException e) {
	        e.printStackTrace();
	      }
	    }
	  
  }

}