package ch14;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomPlayerController {

    @FXML
    private ImageView backgroundImage;

    public void setBackgroundImage(Image image) {
        backgroundImage.setImage(image);
    }
}
