import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;



//json.simple 1.1
// import org.json.simple.JSONObject;
 //import org.json.simple.JSONArray;

// Alternate implementation of JSON modules.
import org.json.JSONObject;
import org.json.JSONArray;

public class GetData{
	
    static String prefix = "project2.";
	
    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;
	
    // You must refer to the following variables for the corresponding 
    // tables in your database

    String cityTableName = null;
    String userTableName = null;
    String friendsTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;
    String programTableName = null;
    String educationTableName = null;
    String eventTableName = null;
    String participantTableName = null;
    String albumTableName = null;
    String photoTableName = null;
    String coverPhotoTableName = null;
    String tagTableName = null;

    // This is the data structure to store all users' information
    // DO NOT change the name
    JSONArray users_info = new JSONArray();		// declare a new JSONArray

	
    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
	super();
	String dataType = u;
	oracleConnection = c;
	// You will use the following tables in your Java code
	cityTableName = prefix+dataType+"_CITIES";
	userTableName = prefix+dataType+"_USERS";
	friendsTableName = prefix+dataType+"_FRIENDS";
	currentCityTableName = prefix+dataType+"_USER_CURRENT_CITIES";
	hometownCityTableName = prefix+dataType+"_USER_HOMETOWN_CITIES";
	programTableName = prefix+dataType+"_PROGRAMS";
	educationTableName = prefix+dataType+"_EDUCATION";
	eventTableName = prefix+dataType+"_USER_EVENTS";
	albumTableName = prefix+dataType+"_ALBUMS";
	photoTableName = prefix+dataType+"_PHOTOS";
	tagTableName = prefix+dataType+"_TAGS";
    }
	
	
	
	
    //implement this function

    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException{

    	JSONArray users_info = new JSONArray();
		
	    // Your implementation goes here....		
        try (Statement stmt = oracleConnection.createStatement()) {
            
            ResultSet rst = stmt.executeQuery(
            "SELECT user_id, first_name, last_name, gender, year_of_birth, month_of_birth, day_of_birth " +         
            "FROM " + userTableName + " "); 
            Long userID;
            String firstName;
            String lastName;
            String gender;
            int yob;
            int mob;
            int dob;

            // iterate over the user_id
            while (rst.next()) {     
                JSONObject user = new JSONObject();
                JSONObject home_city = new JSONObject();
                JSONObject curr_city = new JSONObject();
                userID = rst.getLong(1);
                firstName = rst.getString(2);
                lastName = rst.getString(3);
                gender = rst.getString(4);
                yob = rst.getInt(5);
                mob = rst.getInt(6);
                dob = rst.getInt(7);
                user.put("user_id", userID);
                user.put("first_name", firstName);
                user.put("last_name", lastName);
                user.put("gender", gender);
                user.put("YOB", yob);
                user.put("MOB", mob);
                user.put("DOB", dob);
                Statement stmth = oracleConnection.createStatement();
                Statement stmtc = oracleConnection.createStatement();
                ResultSet rsth = stmth.executeQuery(
                "SELECT c.city_name, c.state_name, c.country_name " +  
                "FROM " + cityTableName + " c, " + hometownCityTableName + " h " +
                "WHERE h.hometown_city_id = c.city_id and h.user_id = " + userID);
                String city = "";
                String state = "";
                String country = "";
                while (rsth.next()) {
                    city = rsth.getString(1);
                    state = rsth.getString(2);
                    country = rsth.getString(3);
                }
                home_city.put("city", city);
                home_city.put("state", state);
                home_city.put("country", country);
                user.put("hometown", home_city);
                ResultSet rstc = stmtc.executeQuery(
                "SELECT c.city_name, c.state_name, c.country_name " +  
                "FROM " + cityTableName + " c, " + currentCityTableName + " h " +
                "WHERE h.current_city_id = c.city_id and h.user_id = " + userID);
                while (rstc.next()) {
                    city = rstc.getString(1);
                    state = rstc.getString(2);
                    country = rstc.getString(3);
                }
                curr_city.put("city", city);
                curr_city.put("state", state);
                curr_city.put("country", country);
                user.put("current", curr_city);

                
                rsth.close();
                stmth.close();
                rstc.close();
                stmtc.close();

                Statement stmtf = oracleConnection.createStatement();
                ResultSet rstf = stmtf.executeQuery(
                    "SELECT user1_id FROM " + friendsTableName + " " +
                    "WHERE user2_id = " + userID +" and user1_id > " + userID + " " +
                    "UNION " +
                    "SELECT user2_id FROM " + friendsTableName + " " +
                    "WHERE user1_id = " + userID +" and user2_id > " + userID);
                JSONArray friends = new JSONArray();
                while (rstf.next()) {
                    friends.put(rstf.getLong(1));
                }
                user.put("friends", friends);
                users_info.put(user);
                rstf.close();
                stmtf.close();
            }
            rst.close();
            stmt.close();
        }

        return users_info;
    }

    // This outputs to a file "output.json"
    public void writeJSON(JSONArray users_info) {
	// DO NOT MODIFY this function
	try {
	    FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
	    file.write(users_info.toString());
	    file.flush();
	    file.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
		
    }
}
