package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CharacterSelectController {
    private Stage getStageFromEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }


    @FXML
    private void onChooseSteve(ActionEvent event) throws IOException {
        GameConfig.setSelectedCharacter(CharacterType.STEVE);
        goBackToHome(event);
    }

    @FXML
    private void onChooseAlex(ActionEvent event) throws IOException {
        GameConfig.setSelectedCharacter(CharacterType.ALEX);
        goBackToHome(event);
    }

    @FXML
    private void onBackToHome(ActionEvent event) throws IOException{
        goBackToHome(event);
    }

    private void goBackToHome(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeLayout.fxml"));
        Parent root = loader.load();

        Stage stage = getStageFromEvent(event);
        Scene scene = new Scene(root, 460, 510);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}