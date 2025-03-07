/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.*;

public class TargetModel {
    public boolean pathComplete;
    private List<TargetModelListener> subscribers;
    private ArrayList<Target> targets;

    public boolean targetResized = false;

    public double boxX, boxY, boxSizeX, boxSizeY, startBoxX, startBoxY = 0;

    PixelReader readerofPixels;

    ArrayList<Point2D> pointsTracked = new ArrayList<>();

    public int boxType = -1;

    public TargetModel() {
        subscribers = new ArrayList<>();
        targets = new ArrayList<>();
    }

    public Target addTarget(double x, double y) {
        Target t = new Target(x, y, targets.size()+1);
        targets.add(t);
        notifySubscribers();
        return t;
    }

    public Target addTarget(double x, double y, double r) {
        Target t = new Target(x, y, r,targets.size()+1);
        targets.add(t);
        notifySubscribers();
        return t;
    }

    public void moveTarget(Target t, double dx, double dy) {
        t.move(dx,dy);
        notifySubscribers();
    }

    public void addSubscriber(TargetModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.modelChanged());
    }

    public List<Target> getTargets() {
        return targets;
    }

    public boolean hitTarget(double x, double y) {
        for (Target t : targets) {
            if (t.contains(x,y)) return true;
        }
        return false;
    }

    public Target whichHit(double x, double y) {
        for (Target t : targets) {
            if (t.contains(x,y)) return t;
        }
        return null;
    }

    public void removeSelectedTarget(Target selected) {
        targets.remove(selected);
        targets.forEach(t -> {
            t.id = targets.indexOf(t)+1;
        });

        notifySubscribers();
    }

    public void resizeTarget(double prevX, double prevY) {
    }

    public void resizeTarget(ArrayList<Target> selected, double dr) {

            selected.forEach(t -> {
                t.resize(dr);
            });

            targetResized = true;
            notifySubscribers();
    }

    public void startSelectionBox(double dX, double dY) {
        this.boxX = dX;
        this.boxY = dY;

        startBoxX = dX;
        startBoxY = dY;
    }



    public ArrayList<Target> getTargetsLassoo() {

        return null;
    }
    public ArrayList<Target> getTargetsBox() {

        return null;
    }

    public void updateSelectionBox(double dX, double dY) {
        if (boxType == -1) {

            if (dX < 0) {
                boxX += dX;
                boxSizeX -= dX;
            } else {
                boxSizeX += dX;

            }
            if (dY < 0) {
                boxY += dY;
                boxSizeY -= dY;
            } else {
                boxSizeY += dY;
            }

            if (boxX == startBoxX && boxY == startBoxY) {
                boxType = 0;
            } else if (boxX != startBoxX && boxY == startBoxY) {
                boxType = 1;
            } else if (boxX == startBoxX && boxY != startBoxY) {
                boxType = 2;

            } else {
                boxType = 3;
            }
        }
        else if (boxType == 0) {
            boxSizeX += dX;
            boxSizeY += dY;
            if (boxSizeX < 0 && boxSizeY < 0) {
                boxType = 3;
            }
            else if (boxSizeX < 0) {
                boxType = 1;

            }
            else if (boxSizeY < 0) {
                boxType = 2;
            }
        }
        else if (boxType == 1) {
            boxSizeY += dY;

            boxX += dX;
            boxSizeX -= dX;

            if (boxSizeX < 0 && boxSizeY < 0) {
                boxType = 2;
            }
            else if (boxSizeX < 0) {
                boxType = 0;

            }
            else if (boxSizeY < 0) {
                boxType = 3;
            }
        }

        else if (boxType == 2) {

            boxSizeX += dX;

            boxY += dY;
            boxSizeY -= dY;

            if (boxSizeX < 0 && boxSizeY < 0) {
                boxType = 1;
            }
            else if (boxSizeX < 0) {
                boxType = 3;

            }
            else if (boxSizeY < 0) {
                boxType = 0;
            }
        }
        else {
            boxX += dX;
            boxSizeX -= dX;
            boxY += dY;
            boxSizeY -= dY;

            if (boxSizeX < 0 && boxSizeY < 0) {
                boxType = 0;
            }
            else if (boxSizeX < 0) {
                boxType = 2;

            }
            else if (boxSizeY < 0) {
                boxType = 1;
            }
        }



        notifySubscribers();
    }

    public void clearBox() {
        boxSizeY = 0;
        boxSizeX = 0;
        boxX = 0;
        boxY = 0;
        boxType = -1;
        notifySubscribers();
    }

    public void clearTargets() {
        targets = new ArrayList<>();
        notifySubscribers();
    }

    public void updateTargets(ArrayList<Target> update) {
        targets = update;
        notifySubscribers();
    }

    public void setupOffscreen() {
        Canvas checkCanvas = new Canvas(800, 800);
        GraphicsContext checkGC = checkCanvas.getGraphicsContext2D();


        checkGC.beginPath();
        if (pointsTracked.size() > 0) {
            checkGC.moveTo(pointsTracked.get(0).getX(), pointsTracked.get(0).getY());
            for (int i = 1; i < pointsTracked.size(); i++) {
                checkGC.lineTo(pointsTracked.get(i).getX(), pointsTracked.get(i).getY());
            }
        }
        checkGC.closePath();
        checkGC.setFill(Color.BLUE);
        checkGC.fill();

        if (pointsTracked.size() > 1) {
            double xStart = pointsTracked.get(0).getX();
            double yStart = pointsTracked.get(0).getY();

            double xEnd = pointsTracked.get(pointsTracked.size() - 1).getX();
            double yEnd = pointsTracked.get(pointsTracked.size() - 1).getY();

            if (xStart < xEnd && yStart < yEnd) {
                checkGC.fillRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
            }
            else if (xStart > xEnd && yStart < yEnd) {
                checkGC.fillRect(xEnd, yStart, xStart - xEnd, yEnd - yStart);
            }
            else if (xStart < xEnd && yStart > yEnd) {
                checkGC.fillRect(xStart, yEnd, xEnd - xStart, yStart - yEnd);
            }
            else {
                checkGC.fillRect(xEnd, yEnd, xStart - xEnd, yStart - yEnd);
            }
        }

        WritableImage buffer = checkCanvas.snapshot(null, null);
        readerofPixels = buffer.getPixelReader();

    }

    public ArrayList<Target> lassooTargets() {
        ArrayList<Target> returnList = new ArrayList<>();
        targets.forEach(t -> {
            if (readerofPixels.getColor((int) t.x,(int) t.y).equals(Color.BLUE)) {
                returnList.add(t);
            }
        });
        return returnList;

    }

    public void addPoint(Point2D nextPoint) {
        if (!pointsTracked.contains(nextPoint)) {
            pointsTracked.add(nextPoint);
        }
        notifySubscribers();
    }

    public void clearPoints() {
        pointsTracked.clear();
    }
}
