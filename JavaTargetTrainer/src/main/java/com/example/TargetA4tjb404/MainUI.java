/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class MainUI extends StackPane {

    private TargetView editView;
    private TargetController editController;

    private TrainerController trainerController;
    private TrainerView trainerView;

    private InteractionModel iModel;

    public MainUI() {

        TargetModel model = new TargetModel();
        editController = new TargetController(this);
        editView = new TargetView();
        iModel = new InteractionModel();

        trainerView = new TrainerView();
        trainerController = new TrainerController();

        trainerView.setiModel(iModel);
        trainerView.setModel(model);
        trainerController.setiModel(iModel);
        trainerController.setModel(model);

        editController.setModel(model);
        editView.setModel(model);
        editController.setIModel(iModel);
        editView.setIModel(iModel);
        model.addSubscriber(editView);
        iModel.addSubscriber(editView);

        editView.setController(editController);
        trainerView.setController(trainerController);

        this.getChildren().add(editView);
    }

    public void setScene(Scene scene) {
        editView.setControllerScene(editController, scene);
    }

    public void changeToTraining() {
        trainerView.restart();

        if (!iModel.isTrainingMode) {
            iModel.subscribers.remove(editView);
            iModel.addSubscriber(trainerView);


            this.getChildren().clear();
            this.getChildren().add(trainerView);
            iModel.isTrainingMode = true;
        }
    }

    public void changeToEdit() {
        if (iModel.isTrainingMode) {
            iModel.subscribers.remove(trainerView);
            iModel.addSubscriber(editView);

            this.getChildren().clear();
            this.getChildren().add(editView);
            iModel.isTrainingMode = false;

            editView.modelChanged();
        }
    }
}
