/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) {
        MainUI uiRoot = new MainUI();
        scene = new Scene(uiRoot);
        scene.setFill(Color.PALETURQUOISE);

        stage.setTitle("Target Trainer");

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.T)) {
                    uiRoot.changeToTraining();
                }
                else if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.E)) {
                    uiRoot.changeToEdit();
                }
            }
        });

        stage.setScene(scene);
        uiRoot.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}