package ch14;

import java.io.File;

import java.net.URL;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class SceneController implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;
    @FXML
    private Button playButton, pauseButton, resetButton, previousButton, nextButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider songProgressBar;
    
    @FXML
    private ImageView imageView;

    private Media media;
    private MediaPlayer mediaPlayer;
    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumber;
    private final int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};
    private Timer timer;
    private TimerTask task;
    private boolean running;
    private boolean isPlay = true;
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        songs = new ArrayList<File>();

        	directory = new File(System.getProperty("user.home"), "ProjectMusic");
        	files = directory.listFiles()
        			;
        	if(files != null) {
        		for(File file : files) {
        			String name = file.getName().toLowerCase();
        			if(file.isFile() && (name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".m4a"))) {
        				songs.add(file);
        				System.out.println("Found: " + file.getName());
        			}
        			
        		}
        	}else {
        		System.out.println("Could not read Music folder!");
        	}
        	if(songs.isEmpty()) {
        		System.out.println("No audio files found in Music folder!");
        		return;
        	}
        	
        	//add the button icons
        	setUpIcons();

        	media = new Media(songs.get(0).toURI().toString());
        	mediaPlayer = new MediaPlayer(media);
        	
        	mediaPlayer.setOnReady(() -> {
        		songProgressBar.setMax(media.getDuration().toSeconds());
        	});

        	songLabel.setText(songs.get(0).getName());

        	directory = new File(System.getProperty("user.home"), "ProjectMusic");

        	if (files != null) {
        	    for (File file : files) {
        	        System.out.println("Found: " + file.getName());
        	    }
        	} else {
        	    System.out.println("Could not read Music folder!");
        	}

        if(songs.isEmpty()) {
        	System.out.println("No MP3 files found!");
        	return;
        }
        
        
        //Print which file JavaFX is loading
        System.out.println("Loading: " + songs.get(songNumber).getName());

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
        	songProgressBar.setMax(media.getDuration().toSeconds());
        });
        songLabel.setText(songs.get(songNumber).getName().substring(0, songs.get(songNumber).getName().length() - 4));

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }
        var iconStream = getClass().getResourceAsStream("icon.png");
        if(iconStream != null) {
        	Image image = new Image(iconStream);
        	imageView.setImage(image);
        }else {
        	System.out.println("Could not find icon.png in package!");
        }
    

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }

        });
        
        //add a slider that can jump to a specific time stamp when dragged and released
        songProgressBar.valueChangingProperty().addListener((obs, wasChagning, isChanging) -> {
        	if(!isChanging && mediaPlayer != null) {
        		mediaPlayer.seek(Duration.seconds(songProgressBar.getValue()));
        	}
        });
        
        //jump to a specific time stamp when the progress bar is clicked directly on the track
        songProgressBar.setOnMouseClicked(event -> {
        	if(mediaPlayer != null) {
        		mediaPlayer.seek(Duration.seconds(songProgressBar.getValue()));
        	}
        });
    }
    
  
    //set up the icons for the music player
    private void setUpIcons() {
    	pauseButton.setText("▶");
        resetButton.setText("↺");
        previousButton.setText("⏮");
        nextButton.setText("⏭");
    }
       
    //create setter for buttons
    private void setButtonIcon(Button button, Image image) {
    	if(button != null && image != null) {
    		ImageView iconView = new ImageView(image);
    		iconView.setFitWidth(20);
    		iconView.setFitHeight(20);
    		iconView.setPreserveRatio(true);
    		button.setText("");
    		button.setGraphic(iconView);
    	}
    }
    
    private void loadSong(int index) {
    	if(mediaPlayer != null) {
    		mediaPlayer.stop();
    		mediaPlayer.dispose();
    	}
    	
    	File currentFile = songs.get(index);
    	media = new Media(currentFile.toURI().toString());
    	mediaPlayer = new MediaPlayer(media);
    	
    	String fileName = currentFile.getName();
    	songLabel.setText(fileName.endsWith(".mp3") ? fileName.substring(0, fileName.length() - 4) : fileName);
    	
    	songProgressBar.setValue(0);
    	mediaPlayer.setOnReady(() -> {
    		songProgressBar.setMax(media.getDuration().toSeconds());
    	});
    	
    	mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
    	changeSpeed(null);
    }
        
  
    public void browseMusic(ActionEvent event) {
         
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("MP3 Files (.mp3)", "*.mp3");
        fc.getExtensionFilters().add(ext);
        File song = fc.showOpenDialog(window);
        event.consume();
        
        if(song != null) {
        	songs.add(song);
        }
    }

    public void handleMedia() {
        if (isPlay) {
            beginTimer();
            changeSpeed(null);
            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            mediaPlayer.play();
            isPlay = false;
            pauseButton.setText("⏸");
        } else {
            cancelTimer();
            mediaPlayer.pause();
            isPlay = true;
            pauseButton.setText("▶");
        }
    }

    public void resetMedia() {
        songProgressBar.setValue(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void previousMedia() {
        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> {
            	songProgressBar.setMax(media.getDuration().toSeconds());
            });
            songLabel.setText(songs.get(songNumber).getName().substring(0, songs.get(songNumber).getName().length() - 4));
        } else {
            songNumber = songs.size() - 1;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> {
            	songProgressBar.setMax(media.getDuration().toSeconds());
            });
            songLabel.setText(songs.get(songNumber).getName().substring(0, songs.get(songNumber).getName().length() - 4));
        }
        handleMedia();
    }

    public void nextMedia() {
        if (songNumber < songs.size() - 1) {
            songNumber++;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            
            mediaPlayer.setOnReady(() -> {
            	songProgressBar.setMax(media.getDuration().toSeconds());
            });
            songLabel.setText(songs.get(songNumber).getName().substring(0, songs.get(songNumber).getName().length() - 4));
        } else {
            songNumber = 0;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            
            mediaPlayer.setOnReady(() -> {
            	songProgressBar.setMax(media.getDuration().toSeconds());
            });
            songLabel.setText(songs.get(songNumber).getName().substring(0, songs.get(songNumber).getName().length() - 4));
        }
        handleMedia();
    }

    public void changeSpeed(ActionEvent event) {
        if (speedBox.getValue() == null) {
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        }
    }

    public void closeApp() {
        Platform.exit();
        System.exit(0);
    }

    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                Platform.runLater(() -> {
                	if(!songProgressBar.isValueChanging()) {
                		songProgressBar.setValue(current);
                	}
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 500);
    }

    public void cancelTimer() {
        running = false;
        if(timer != null) {
        	timer.cancel();
        }
    }
}
