package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.jfr.Event;

import javax.swing.*;
import java.io.IOException;


public class HomeController {

    private Stage stage;

    public void setStage(Stage s) {
        this.stage = s;
    }

    private Stage getStageFromEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    @FXML
    private void onStartGame(ActionEvent event) throws IOException {
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
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/difficultyLayout.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        popup.setTitle("Select Difficulty");
        popup.setScene(new Scene(root, 460, 300)); // adjust size if you like
        popup.setResizable(false);
        popup.initOwner(stage);
        popup.showAndWait();
    }

    @FXML
    public void onChooseCharacter(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/characterSelect.fxml"));
        Parent root = loader.load();

        Stage stage = getStageFromEvent(event);
        Scene scene = new Scene(root, 460, 510);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
