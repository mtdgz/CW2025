package com.comp2042;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class HomeController {
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void onStartGame(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
            Parent root = loader.load();

            GuiController gameController = loader.getController();
            new GameController(gameController);

            Scene gameScene = new Scene(root, 460, 510);
            stage.setScene(gameScene);
            stage.setTitle("Tetris Minecraft Edition");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onChooseDifficulty(){

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
