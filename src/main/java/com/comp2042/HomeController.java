package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HomeController {

    private Stage stage;

    @FXML
    private StackPane homeRoot;

    @FXML
    private MediaView homeBgView;

    public void setStage(Stage s) {
        this.stage = s;
    }

    private Stage getStageFromEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    @FXML
    public void initialize() {
        try {
            URL videoUrl = getClass().getResource("/video/home_bg.mp4");

            assert videoUrl != null;
            Media media = new Media(videoUrl.toExternalForm());


            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.25);


            homeBgView.setMediaPlayer(player);

            homeBgView.setPreserveRatio(false);
            homeBgView.fitWidthProperty().bind(homeRoot.widthProperty());
            homeBgView.fitHeightProperty().bind(homeRoot.heightProperty());

            homeBgView.toBack();

            player.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onStartGame(ActionEvent event) throws IOException {
        SoundManager.getInstance().playButtonClick();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
        Parent root = loader.load();

        GuiController guiController = loader.getController();
        new GameController(guiController);

        Stage stage = getStageFromEvent(event);
        Scene scene = new Scene(root, 460, 510);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onChooseDifficulty() throws Exception {
        SoundManager.getInstance().playButtonClick();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/difficultyLayout.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        popup.setTitle("Select Difficulty");
        popup.setScene(new Scene(root, 460, 300));
        popup.setResizable(false);
        if (stage != null) {
            popup.initOwner(stage);
        }
        popup.showAndWait();
    }

    @FXML
    public void onChooseCharacter(ActionEvent event) throws IOException {
        SoundManager.getInstance().playButtonClick();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/characterSelect.fxml"));
        Parent root = loader.load();

        Stage stage = getStageFromEvent(event);
        Scene scene = new Scene(root, 460, 510);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
