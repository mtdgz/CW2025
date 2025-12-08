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

    @FXML
    private void choosePeaceful(ActionEvent event) {
        SoundManager.getInstance().playButtonClick();
        GameConfig.setSelectedDifficulty(Difficulty.PEACEFUL);
        closeWindowFrom(event);
    }

    @FXML
    private void chooseNormal(ActionEvent event) {
        SoundManager.getInstance().playButtonClick();
        GameConfig.setSelectedDifficulty(Difficulty.NORMAL);
        closeWindowFrom(event);
    }

    @FXML
    private void chooseHardcore(ActionEvent event) {
        SoundManager.getInstance().playButtonClick();
        GameConfig.setSelectedDifficulty(Difficulty.HARDCORE);
        closeWindowFrom(event);
    }

    @FXML
    private void goBack(ActionEvent event) {
        SoundManager.getInstance().playButtonClick();
        closeWindowFrom(event);
    }

    private void closeWindowFrom(ActionEvent event) {
        SoundManager.getInstance().playButtonClick();
        Object source = event.getSource();
        if (source instanceof Button button && button.getScene() != null) {
            Stage stage = (Stage) button.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }
}
