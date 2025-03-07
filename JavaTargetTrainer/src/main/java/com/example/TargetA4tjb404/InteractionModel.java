/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class InteractionModel {
    List<IModelListener> subscribers;
    ArrayList<Target> selected = new ArrayList<>();
    ArrayList<Target> clipBoard = new ArrayList<>();

    public Stack<ArrayList<Target>> undoStack = new Stack<>();
    public Stack<ArrayList<Target>> redoStack = new Stack<>();

    public int currentTarget;
    public int totalTargets;

    public int startTime;

    public List<Integer> trialTimes = new ArrayList<>();
    public List<Double> difficulty = new ArrayList<>();

    public List<Target> targetsTrainer;

    XYChart.Series series = new XYChart.Series();


    public boolean isTrainingMode = false;

    public InteractionModel() {
        subscribers = new ArrayList<>();
    }

    public void addSubscriber(IModelListener sub) {
        subscribers.add(sub);
    }

/*    public void swapSubscriber(IModelListener sub) {
        if (sub instanceof TrainerView) {
            if (subscribers.contains(sub)) {
                subscribers.remove(sub);
            }
            else {
                subscribers.add(sub);
            }

            subscribers.forEach(s -> {
                if (s instanceof TargetView) {
                    subscribers.remove(s);
                }
            });
        }
        else if (sub instanceof  TargetView) {
            if (subscribers.contains(sub)) {
                subscribers.remove(sub);
            }
            else {
                subscribers.add(sub);
            }

            subscribers.forEach(s -> {
                if (s instanceof TrainerView) {
                    subscribers.remove(s);
                }
            });

        }
    }*/

    private void notifySubscribers() {
        subscribers.forEach(s -> s.iModelChanged());
    }

    public void setSelected(ArrayList<Target> t) {
        selected = t;
        notifySubscribers();
    }

    public void addTargetToSelection(Target t) {
        if (selected == null) {
            selected = new ArrayList<>();
        }
        if (!selected.contains(t)) {
            selected.add(t);
            notifySubscribers();

        }
    }


    public void unselect() {
        selected = null;
        notifySubscribers();
    }

    public ArrayList<Target> getSelected() {
        return selected;
    }


    public void addUndo(ArrayList<Target> t, boolean permission) {
        if (!permission) {
            redoStack.clear();
        }
        boolean flag = true;
        for (ArrayList<Target> tar: undoStack) {
            if (tar.equals(t)) {
                flag = false;
                break;
            }
        }
        if (flag) {
            ArrayList<Target> tars = new ArrayList<Target>();
            t.forEach(target -> {
                tars.add(new Target(target.x, target.y, target.radius, target.id));
            });

            undoStack.add(tars);
        }
    }


    public void addRedo(ArrayList<Target> t) {
        redoStack.add(t);
        notifySubscribers();
    }

    public void addTargetToSelectionOrRemove(Target t) {
        if (selected == null) {
            selected = new ArrayList<>();
        }
        if (!selected.contains(t)) {
            selected.add(t);

        }
        else {
            selected.remove(t);
        }
        notifySubscribers();
    }

    public void nextTrainerTarget(Target t) {
        for (Target tar: targetsTrainer) {
            if (t.id == tar.id) {
                currentTarget++;
            }
        }

        startTime = (int) System.currentTimeMillis();


        notifySubscribers();
    }

    public void addGraphPoint(Double integer, Integer integer1) {
        series.getData().add(new XYChart.Data(integer, integer1));

    }
}

