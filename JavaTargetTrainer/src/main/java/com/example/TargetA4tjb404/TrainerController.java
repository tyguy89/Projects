/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.scene.input.MouseEvent;

/**
 * Controller class for trainer
 */
public class TrainerController {

    TargetModel targetModel;
    InteractionModel iModel;

    double prevX, prevY;

    public void setModel(TargetModel model) {
        targetModel = model;
    }

    public void setiModel(InteractionModel i) {
        iModel = i;
    }

    public void handlePressed(MouseEvent event) {
        if (targetModel.hitTarget(event.getX(), event.getY())) {
            if (iModel.currentTarget > 0) {
                Target t = targetModel.whichHit(event.getX(), event.getY());
                iModel.trialTimes.add((int) (System.currentTimeMillis()) - iModel.startTime);

                iModel.nextTrainerTarget(t);
                //Move forwards target
                iModel.addGraphPoint(iModel.difficulty.get(iModel.currentTarget-2), iModel.trialTimes.get(iModel.trialTimes.size()-1));

            } else {
                //Start target
                Target t = targetModel.whichHit(event.getX(), event.getY());
                iModel.nextTrainerTarget(t);
            }
        }
        prevX = event.getX();
        prevY = event.getY();
    }
}
