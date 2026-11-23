
package ch14;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CustomizeController {

    @FXML
    private ImageView imagePreview;

    private Image selectedImage;

    @FXML
    public void chooseImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose Album Cover");

        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg"
            )
        );

        Stage stage = (Stage)
            ((Node) event.getSource()).getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImage = new Image(file.toURI().toString());
            imagePreview.setImage(selectedImage);
        }
    }

    @FXML
    public void launchPlayer(ActionEvent event)
            throws IOException {

        if (selectedImage == null) {
            System.out.println("Please choose an image first.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("CustomPlayer.fxml")
        );

        Parent root = loader.load();

        CustomPlayerController controller =
            loader.getController();

        controller.setBackgroundImage(selectedImage);

        Stage stage = (Stage)
            ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Customized jMusic Player");
    }
}
