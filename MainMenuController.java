
package ch14;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainMenuController {

    @FXML
    private void openDefaultPlayer(ActionEvent event)
            throws IOException {

        Parent root = FXMLLoader.load(
            getClass().getResource("MainMenu.fxml")
        );

        Stage stage = (Stage)
            ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("JMusic Player");
        stage.show();
    }

    @FXML
    private void openCustomPlayer(ActionEvent event)
            throws IOException {

        // We will create the customized player later.
        System.out.println("Custom player selected");
    }
}
