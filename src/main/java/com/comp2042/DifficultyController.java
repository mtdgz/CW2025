package com.comp2042;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DifficultyController {

    @FXML
    private Button peacefulButton;

    @FXML
    private Button normalButton;

    @FXML
    private Button hardcoreButton;

    @FXML
    private Button backButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void choosePeaceful(ActionEvent event) {
        GameConfig.setSelectedDifficulty(Difficulty.PEACEFUL);
        closeWindowFrom(event);
    }

    @FXML
    private void chooseNormal(ActionEvent event) {
        GameConfig.setSelectedDifficulty(Difficulty.NORMAL);
        closeWindowFrom(event);
    }

    @FXML
    private void chooseHardcore(ActionEvent event) {
        GameConfig.setSelectedDifficulty(Difficulty.HARDCORE);
        closeWindowFrom(event);
    }

    @FXML
    private void goBack(ActionEvent event) {
        closeWindowFrom(event);
    }

    private void closeWindowFrom(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button button && button.getScene() != null) {
            Stage stage = (Stage) button.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }
}
