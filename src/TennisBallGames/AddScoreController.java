package TennisBallGames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddScoreController implements Initializable {
    @FXML
    javafx.scene.control.Button cancelBtn;
    @FXML
    javafx.scene.control.Button saveBtn;

    @FXML
    ComboBox matchBox;

    @FXML
    TextField homeTeamScore;
    @FXML
    TextField visitorTeamScore;

    Connection connection;

    // Some local variable declarations
    // The data variable is used to populate the ComboBoxs
    final ObservableList<String> data = FXCollections.observableArrayList();
    // To reference the models inside the controller
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;


    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void save() {
        try {
            String matchText = (String) matchBox.getValue();
            char atZero = matchText.charAt(0);

            int matchNumber = Character.getNumericValue(atZero);
            int hScore = Integer.parseInt(homeTeamScore.getText());
            int vScore = Integer.parseInt(visitorTeamScore.getText());

            String teamNames = (String) matchBox.getValue();
            String substr1 = teamNames.substring(2,17);
            String substr2 = teamNames.substring(18);

            String hName = substr1;
            String vName = substr2;

            matchesAdapter.setTeamsScore(matchNumber, hScore, vScore);
            teamsAdapter.setStatus(hName, vName, hScore, vScore);
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void buildComboBoxData() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
    }

    // Display an alert
    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/TennisBallGames/WesternLogo.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        matchBox.setItems(data);
    }
}
