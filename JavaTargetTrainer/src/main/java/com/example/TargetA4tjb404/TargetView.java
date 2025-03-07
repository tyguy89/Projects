/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class TargetView extends StackPane implements TargetModelListener, IModelListener {
    GraphicsContext gc;
    Canvas myCanvas;
    TargetModel model;
    InteractionModel iModel;
    TargetController controller;

    public TargetView() {
        myCanvas = new Canvas(800,800);
        gc = myCanvas.getGraphicsContext2D();
        gc.setFill(Color.ORANGE);

        this.getChildren().add(myCanvas);
    }

    private void draw() {
        gc.clearRect(0,0,myCanvas.getWidth(),myCanvas.getHeight());

        gc.setFill(Color.PALETURQUOISE);

        gc.rect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        for (Point2D point : model.pointsTracked) {
            gc.strokeOval(point.getX(), point.getY(), 2, 2);
        }


        model.getTargets().forEach(t -> {


            if (iModel.getSelected() != null && iModel.getSelected().contains(t)) {
                gc.setFill(Color.TOMATO);
            } else {
                gc.setFill(Color.GAINSBORO);
            }

            gc.fillOval(t.x-t.radius,t.y-t.radius,t.radius *2,t.radius *2);
            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(t.id), t.x-3, t.y);
        });

        if (model.boxX != 0) {
            gc.setFill(Color.BLACK);

            gc.strokeRect(model.boxX, model.boxY, model.boxSizeX, model.boxSizeY);
        }
    }

    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    @Override
    public void modelChanged() {
        draw();
        this.requestFocus();
    }

    @Override
    public void iModelChanged() {
        draw();
    }

    public void setController(TargetController controller) {
        this.controller = controller;
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);

    }
    public void setControllerScene(TargetController controller, Scene scene) {
        scene.setOnKeyPressed(controller::handleKeyPress);
        scene.setOnKeyReleased(controller::handleKeyReleased);
    }
}
