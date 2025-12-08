package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL fontUrl = getClass().getResource("/fonts/Minecraft.ttf");
        if (fontUrl != null){
            Font.loadFont(fontUrl.toExternalForm(), 16);
        } else {
            System.out.println("Minecraft font not found");
        }

        URL fxmlUrl = getClass().getResource("/homeLayout.fxml");
        if (fxmlUrl == null){
            System.out.println("homeLayout.fxml not found");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeLayout.fxml"));
        Parent root = loader.load();

        HomeController homeController = loader.getController();
        homeController.setStage(primaryStage);

        Scene scene = new Scene(root, 460, 510);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tetris Minecraft Edition");
        primaryStage.setResizable(false);
        primaryStage.show();

        SoundManager.getInstance().playBgm();
    }


}
