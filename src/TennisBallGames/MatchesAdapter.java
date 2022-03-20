package TennisBallGames;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Abdelkader
 */
public class MatchesAdapter {

    Connection connection;

    public MatchesAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
            // This will throw an exception if the tables do not exist
            stmt.execute("DROP TABLE Matches");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                // Create the table of Matches
                stmt.execute("CREATE TABLE Matches ("
                        + "MatchNumber INT NOT NULL PRIMARY KEY, "
                        + "HomeTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "VisitorTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "HomeTeamScore INT, "
                        + "VisitorTeamScore INT "
                        + ")");
                populateSamples();
            }
        }
    }
    
    private void populateSamples() throws SQLException{
            // Create a listing of the matches to be played
            this.insertMatch(1, "Astros", "Brewers");
            this.insertMatch(2, "Brewers", "Cubs");
            this.insertMatch(3, "Cubs", "Astros");
    }
        
    
    public int getMax() throws SQLException {
        int num = 0;

        Statement stmt = connection.createStatement();
        String max = "SELECT * FROM Matches";
        ResultSet result = stmt.executeQuery(max);

        while(result.next()){
            num = result.getInt(1);
        }
        
        return num;
    }

    public int getMatchNumber() throws SQLException {
        Statement stmt = connection.createStatement();
        String match = "SELECT MatchNumber FROM Matches";
        ResultSet result = stmt.executeQuery(match);

        int matchNumber = 0;
        while(result.next()){
            matchNumber = result.getInt(1);
        }
        return matchNumber;
    }
    
    public void insertMatch(int num, String home, String visitor) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO Matches (MatchNumber, HomeTeam, VisitorTeam, HomeTeamScore, VisitorTeamScore) "
                + "VALUES (" + num + " , '" + home + "' , '" + visitor + "', 0, 0)");
    }
    
    // Get all Matches
    public ObservableList<Matches> getMatchesList() throws SQLException {
        ObservableList<Matches> matchesList = FXCollections.observableArrayList();

        Statement stmt = connection.createStatement();
        String selectColumn = "SELECT * FROM Matches";
        ResultSet result = stmt.executeQuery(selectColumn);

        while (result.next()) {
            matchesList.add(new Matches(result.getInt("MatchNumber"),
                    result.getString("HomeTeam"),
                    result.getString("VisitorTeam"),
                    result.getInt("HomeTeamScore"),
                    result.getInt("VisitorTeamScore")));
        }

        return matchesList;
    }

    // Get a String list of matches to populate the ComboBox used in Task #4.
    public ObservableList<String> getMatchesNamesList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        ResultSet rs;

        // Create a Statement object
       Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
         String getInfo = "SELECT * FROM Matches";


        // Execute the statement and return the result
        rs = stmt.executeQuery(getInfo);
        
        // Loop the entire rows of rs and set the string values of list
       while(rs.next()) {
           int matchNumber = rs.getInt(1);
           String home = rs.getString(2);
           String visitor = rs.getString(3);

           String listInput = matchNumber + "-" + home + "-" + visitor;

           list.add(listInput);
       }
        
        return list;
    }
    
    public void setTeamsScore(int matchNumber, int hScore, int vScore) throws SQLException
    {
        Statement statement = connection.createStatement();

        statement.executeUpdate("UPDATE Matches " + " SET MatchNumber =" + matchNumber + ", " + "HomeTeamScore =" + hScore + ", "
                + "VisitorTeamScore =" + vScore + " WHERE MatchNumber =" + matchNumber);
   }  
}
