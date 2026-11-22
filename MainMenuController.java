
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
    public void openCustomPlayer(ActionEvent event) {

        System.out.println("Customized player selected!");
    }
}
