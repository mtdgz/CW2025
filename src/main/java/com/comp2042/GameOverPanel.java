package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameOverPanel extends StackPane {

    public GameOverPanel() {
        // overlay look
        setVisible(false);
        setPickOnBounds(true); // block clicks to underlying UI
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getStyleClass().add("you-died-overlay");

        Label titleLabel = new Label("You Died!");
        titleLabel.getStyleClass().add("you-died-title");

        Label scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("you-died-score");

        Button respawnButton = new Button("Respawn");
        respawnButton.getStyleClass().add("you-died-button");

        Button titleScreenButton = new Button("Title screen");
        titleScreenButton.getStyleClass().add("you-died-button");

        VBox box = new VBox(10, titleLabel, scoreLabel, respawnButton, titleScreenButton);
        box.setAlignment(Pos.CENTER);

        getChildren().add(box);
    }
}
