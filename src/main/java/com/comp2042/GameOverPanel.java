package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameOverPanel extends StackPane {

    private final Label titleLabel;
    private final Label scoreLabel;
    private final Button respawnButton;
    private final Button titleScreenButton;

    private Runnable onRespawn;
    private Runnable onTitleScreen;

    public GameOverPanel() {
        // overlay look
        setVisible(false);
        setPickOnBounds(true); // block clicks to underlying UI
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getStyleClass().add("you-died-overlay");

        titleLabel = new Label("You Died!");
        titleLabel.getStyleClass().add("you-died-title");

        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("you-died-score");

        respawnButton = new Button("Respawn");
        respawnButton.getStyleClass().add("you-died-button");

        titleScreenButton = new Button("Title screen");
        titleScreenButton.getStyleClass().add("you-died-button");

        respawnButton.setOnAction(e -> {
            if (onRespawn != null) {
                onRespawn.run();
            }
        });

        titleScreenButton.setOnAction(e -> {
            if (onTitleScreen != null) {
                onTitleScreen.run();
            }
        });

        VBox box = new VBox(10, titleLabel, scoreLabel, respawnButton, titleScreenButton);
        box.setAlignment(Pos.CENTER);

        getChildren().add(box);
    }

    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void setOnRespawn(Runnable onRespawn) {
        this.onRespawn = onRespawn;
    }

    public void setOnTitleScreen(Runnable onTitleScreen) {
        this.onTitleScreen = onTitleScreen;
    }
}
