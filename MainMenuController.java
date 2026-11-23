
package ch14;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    public void openDefaultPlayer(ActionEvent event) throws IOException {
    	System.out.println("Default player selected!");
        // Load the original music player FXML
        Parent root = FXMLLoader.load(
            getClass().getResource("Scene.fxml")
        );

        
        Stage stage = (Stage)
            ((Node) event.getSource()).getScene().getWindow();

        // Create the music player scene
        Scene musicScene = new Scene(root);

        stage.setScene(musicScene);
        stage.setTitle("jMusic Player");
        stage.setWidth(600);
        stage.setHeight(500);
        
        stage.show();
    }



@FXML
public void openCustomPlayer(ActionEvent event)
        throws IOException {

    Parent root = FXMLLoader.load(
        getClass().getResource("/ch14/CustomizePlayer.fxml")
    );

    Stage stage = (Stage)
        ((Node) event.getSource()).getScene().getWindow();

    stage.setScene(new Scene(root));
    stage.setTitle("Customize jMusic Player");
        }
@FXML
public void openCredits(ActionEvent event) throws IOException{
    System.out.println("Credits selected!");
    Parent root = FXMLLoader.load(getClass().getResource("Credits.fxml"));
    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    // Create the music player scene
    Scene creditsScene = new Scene(root);
    	
    stage.setScene(creditsScene);
    stage.setTitle("Credits");
    stage.setWidth(600);
    stage.setHeight(500);
    	
    stage.show();

}
}
