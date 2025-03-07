/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Stackpane view class for target trainer mode
 */
public class TrainerView extends StackPane implements IModelListener {

    GraphicsContext gc;
    Canvas myCanvas;
    public TargetModel model;
    public InteractionModel iModel;
    final NumberAxis xAxis, yAxis;
    final ScatterChart<Number,Number> sc;
    public boolean graphMode = false;

    /**
     * Constructor, creates graphics and graph components
     */
    public TrainerView() {
        myCanvas = new Canvas(800,800);
        gc = myCanvas.getGraphicsContext2D();
        gc.setFill(Color.ORANGE);

        this.getChildren().add(myCanvas);

        xAxis = new NumberAxis(0, 10, 0.5);
        yAxis = new NumberAxis(0,2000, 200);
        sc = new ScatterChart<Number,Number>(xAxis,yAxis);

        yAxis.setLabel("Time in milliseconds");
        xAxis.setLabel("Difficulty by Fitts' Law");
        sc.setTitle("Difficulty vs. Time");
    }

    public void setController(TrainerController controller) {
        myCanvas.setOnMousePressed(controller::handlePressed);
    }

    public void setiModel(InteractionModel i) {
        iModel = i;
    }

    public void setModel(TargetModel modelT) {
        model = modelT;
    }

    @Override
    public void iModelChanged() {
        draw();

    }

    /**
     * Reset proper variables
     */
    public void restart() {
        iModel.targetsTrainer = model.getTargets();
        iModel.totalTargets = iModel.targetsTrainer.size();
        iModel.currentTarget = 0;
        graphMode = false;

        this.getChildren().clear();
        this.getChildren().add(myCanvas);

        iModel.series.getData().clear();
        sc.getData().clear();

        Target startTarget = iModel.targetsTrainer.get(0);
        iModel.targetsTrainer.forEach(t -> {
            if (!startTarget.equals(t)) {
                Target prev = iModel.targetsTrainer.get(t.id-2);
                //System.out.println(prev.x + "G" +  t.x);
                iModel.difficulty.add(((Math.log(2*(Math.sqrt(Math.pow(prev.x-t.x, 2) + Math.pow(prev.y-t.y, 2)))/(t.radius+prev.radius))) / Math.log(2)));
                //System.out.println(iModel.difficulty.get(iModel.difficulty.size()-1));
            } //Create graph points

        });

        draw();
    }

    /**
     * Draw the scene
     */
    private void draw() {
        if (!graphMode) {
            gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
            gc.setFill(Color.PALETURQUOISE);

            gc.rect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
            gc.setFill(Color.BLACK);
            gc.setStroke(Color.BLACK);

            if (iModel.currentTarget >= iModel.totalTargets) {
                graphMode = true;
            }

            if (graphMode) {
                sc.getData().addAll(iModel.series);
                this.getChildren().clear();
                this.getChildren().add(sc);
                return;

            } else {
                Target current = iModel.targetsTrainer.get(iModel.currentTarget);
                gc.fillOval(current.x - current.radius, current.y - current.radius, current.radius * 2, current.radius * 2);
            }
        }
    }

    private void graph() {
        this.getChildren().clear();
    }
}
