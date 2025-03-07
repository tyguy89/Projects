/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TargetController {
    TargetModel model;
    InteractionModel iModel;
    double prevX, prevY;
    double dX, dY;

    ArrayList<Target> ignoreList = new ArrayList<>();

    MainUI main;

    enum State {READY, PREPARE_CREATE, DRAGGING, BOX, LASSOO}
    enum KeyState {CTRLISDOWN, SHIFTISDOWN, NONE}

    State currentState = State.READY;
    KeyState keyState = KeyState.NONE;


    public TargetController(MainUI m) {main = m;}

    public void setModel(TargetModel newModel) {
        model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void handlePressed(MouseEvent event) {
        System.out.println(currentState.toString());
        switch (currentState) {
            case READY -> {
                if (model.hitTarget(event.getX(), event.getY())) {
                    if (keyState == KeyState.CTRLISDOWN) {
                        Target t = model.whichHit(event.getX(), event.getY());
                        iModel.addTargetToSelectionOrRemove(t);

                        prevX = event.getX();
                        prevY = event.getY();
                    }

                    else if (keyState == KeyState.NONE) {
                        ArrayList<Target> tlist = new ArrayList<Target>();
                        Target t = model.whichHit(event.getX(), event.getY());
                        AtomicBoolean flag = new AtomicBoolean(true);
                        if (iModel.selected != null) {
                            iModel.selected.forEach(tar -> {
                                if (tar.id == t.id) {
                                    flag.set(false);

                                }

                            });
                        }
                        if (flag.get()) {
                            tlist.add(t);
                            iModel.setSelected(tlist);
                        }

                        prevX = event.getX();
                        prevY = event.getY();
                        currentState = State.DRAGGING;
                    }

                    else if (keyState == KeyState.SHIFTISDOWN) {
                        ArrayList<Target> tlist = new ArrayList<Target>();
                        Target t = model.whichHit(event.getX(), event.getY());
                        AtomicBoolean flag = new AtomicBoolean(true);

                        iModel.selected.forEach(tar -> {
                            if (tar.id == t.id) {
                                flag.set(false);

                            }

                        });
                        if (flag.get()) {
                            tlist.add(t);
                            iModel.setSelected(tlist);
                        }

                        dX = 0;
                        dY = 0;
                        prevY = event.getY();
                        prevX = event.getX();
                        currentState = State.DRAGGING;
                    }

                }

                else {
                    if (keyState == KeyState.SHIFTISDOWN) {
                        dX = event.getX();
                        dY = event.getY();
                        currentState = State.PREPARE_CREATE;
                        ArrayList<Target> targets = new ArrayList<>();

                        targets.add(model.addTarget(dX, dY));
                        //if (!(model.getTargets().size() == 1)) {
                        iModel.addUndo((ArrayList<Target>) model.getTargets(), false);
                        //}
                        currentState = State.READY;
                    }

                    else if (keyState == KeyState.NONE) {
                        //iModel.unselect();
                        currentState = State.BOX;
                    }
                    else if (keyState == KeyState.CTRLISDOWN) {
                        //iModel.unselect();
                        model.pointsTracked.clear();
                        model.pathComplete = false;
                        Point2D nextPoint = new Point2D(prevX, prevY);
                        model.addPoint(nextPoint);
                        currentState = State.LASSOO;
                    }

                }
            }
        }
    }

    public void handleDragged(MouseEvent event) {
        switch (currentState) {
            case PREPARE_CREATE -> {
                currentState = State.READY;
            }
            case DRAGGING -> {
                if (keyState == KeyState.SHIFTISDOWN) {


                    dX = event.getX() - prevX;
                    dY = event.getY() - prevY;
                    prevX = event.getX();
                    prevY = event.getY();

                    model.resizeTarget(iModel.getSelected(), (dX+dY)/2);


                    /*if (iModel.getSelected() != null) {
                        ArrayList<Target> targetsToResize = new ArrayList<>();
                        iModel.getSelected().forEach(t -> {
                            for (Target target: model.getTargets()) {
                                if (target.id == t.id) {
                                    boolean flag = false;
                                    for (Target tar: targetsToResize) {
                                        if (tar.id == t.id) {
                                            flag = true;
                                        }
                                    }
                                    if (flag) {
                                        continue;
                                    }

                                    targetsToResize.add(t);

                                }
                            }






       *//*                     if (dX > 0 && dY > 0) {
                            }
                            else if (dX < 0 && dY > 0) {
                                model.resizeTarget(targetsToResize, (dX+dY)/2);
                            }
                            else if (dX > 0 && dY < 0) {

                            }
                            else {

                            }*//*

                        });
                    }*/

                }

                else if (keyState == KeyState.NONE) {
                    dX = event.getX() - prevX;
                    dY = event.getY() - prevY;
                    prevX = event.getX();
                    prevY = event.getY();

                    iModel.getSelected().forEach(t -> {

                        /*Target sel = model.whichHit(prevX, prevY);
                        System.out.println("Sel" + sel.toString());
                        AtomicBoolean flag = new AtomicBoolean(true);
                        iModel.selected.forEach(tar -> {
                            if (tar.id == sel.id) {
                                flag.set(false);
                            }

                        });
                        if (!flag.get()) {
                            prevX = sel.x;
                            prevY = sel.y;

                            dX = event.getX() - prevX;
                            dY = event.getY() - prevY;
                        }*/


                        //else {

                        //}

                        for (Target target: model.getTargets()) {
                            if (target.id == t.id) {
                                target = t;
                                model.moveTarget(target, dX, dY);
                            }
                        }
                    });


                }


            }

            case BOX -> {
                dX = event.getX() - prevX;
                dY = event.getY() - prevY;
                prevX = event.getX();
                prevY = event.getY();

                if (model.boxX != 0) {
                    model.updateSelectionBox(dX, dY);



                    model.getTargets().forEach(t -> {
                        if (!ignoreList.contains(t)) {
                            if (model.boxType == 0) {
                                if (t.x - t.radius < model.boxX + model.boxSizeX && t.y - t.radius < model.boxY + model.boxSizeY && model.boxX < t.x+t.radius && model.boxY < t.y+t.radius) {
                                    iModel.addTargetToSelectionOrRemove(t);
                                    ignoreList.add(t);
                                }
                            } else if (model.boxType == 1) {
                                //TODO BREAKS HERE!
                                if (t.x + t.radius > model.boxX && t.y - t.radius < model.boxY + model.boxSizeY && model.boxX < t.x+t.radius && model.boxY < t.y+t.radius) {
                                    iModel.addTargetToSelectionOrRemove(t);
                                    ignoreList.add(t);
                                }
                            } else if (model.boxType == 2) {
                                //TODO BREAKS HERE!
                                if (t.x - t.radius < model.boxX + model.boxSizeX && t.y + t.radius > model.boxY && model.boxX < t.x+t.radius && model.boxY < t.y+t.radius) {
                                    iModel.addTargetToSelectionOrRemove(t);
                                    ignoreList.add(t);
                                }
                            } else if (model.boxType == 3) {
                                //TODO BREAKS HERE!
                                if (t.x + t.radius > model.boxX && t.y + t.radius > model.boxY && model.boxX < t.x+t.radius && model.boxY < t.y+t.radius) {
                                    iModel.addTargetToSelection(t);
                                    ignoreList.add(t);
                                }
                            }
                        }
                    });


                }

                else {
                    model.startSelectionBox(prevX, prevY);
                }


            }

            case LASSOO -> {
                prevX = event.getX();
                prevY = event.getY();

                Point2D nextPoint = new Point2D(prevX, prevY);
                model.addPoint(nextPoint);
            }
        }
    }

    public void handleReleased(MouseEvent event) {
        switch (currentState) {
            case PREPARE_CREATE -> {

            }
            case DRAGGING -> {
                //iModel.unselect();
                iModel.addUndo((ArrayList<Target>) model.getTargets(), false);


                currentState = State.READY;
            }

            case LASSOO -> {
                model.pathComplete = true;
                model.setupOffscreen();


                currentState = State.READY;
                model.clearPoints();
                ArrayList<Target> t = model.lassooTargets();
                for (Target tar: t) {
                    iModel.addTargetToSelectionOrRemove(tar);
                }
            }
            case BOX -> {
                if (iModel.selected != null && !iModel.selected.isEmpty()) {
                    //currentState = State.DRAGGING;
                    dX = 0;
                    dY = 0;


                    //prevX = iModel.getSelected().get(iModel.selected.size()-1).x;
                    //prevY = iModel.getSelected().get(iModel.selected.size()-1).y;
                }
                currentState = State.READY;
                model.clearBox();
                if (ignoreList != null) {
                    ignoreList.clear();
                }

            }
        }
    }


    public void handleKeyPress(KeyEvent event) {
        /*if (iModel.undoStack != null) {
            iModel.undoStack.forEach(t -> {
                System.out.println();
                t.forEach(tar -> {
                    System.out.println(tar.toString());
                });
            });
        }
        System.out.println("*****");
        if (iModel.redoStack != null) {
            iModel.redoStack.forEach(t -> {
                System.out.println();
                t.forEach(tar -> {
                    System.out.println(tar.toString());
                });
            });
        }*/


        if (event.isControlDown()) {
            keyState = KeyState.CTRLISDOWN;

        }


        if (keyState == KeyState.CTRLISDOWN) {
            if (event.getCode() == KeyCode.T) {
                main.changeToTraining();

            }
            else if (event.getCode() == KeyCode.E) {
                main.changeToEdit();

            }
            else if (event.getCode() == KeyCode.X) {
                //Cut mode @TODO

                if (iModel.getSelected() != null) {
                    iModel.clipBoard = iModel.selected;
                    iModel.addUndo((ArrayList<Target>) model.getTargets(), false);
                    iModel.getSelected().forEach(t -> {
                        model.removeSelectedTarget(t);
                    });
                    iModel.unselect();

                }
            } else if (event.getCode() == KeyCode.V) {
                //Paste mode @TODO

                iModel.addUndo((ArrayList<Target>) model.getTargets(), false);
                iModel.clipBoard.forEach(t -> {model.addTarget(t.x, t.y, t.radius);});


            } else if (event.getCode() == KeyCode.C) {
                //Copy mode @TODO
                if (iModel.getSelected() != null) {
                    iModel.clipBoard = iModel.getSelected();

                }


            } else if (event.getCode() == KeyCode.Z) {
                //Undo mode @TODO
                if (!iModel.undoStack.empty()) {

                    ArrayList<Target> update = iModel.undoStack.pop();
                    model.updateTargets(update);

                    iModel.addRedo(update);
                    iModel.unselect();

                }
                else {
                    model.clearTargets();
                }
                iModel.unselect();


            } else if (event.getCode() == KeyCode.R) {
                //Redo mode @TODO
                if (!iModel.redoStack.empty()) {
                    ArrayList<Target> update = iModel.redoStack.pop();
                    model.updateTargets(update);

                    iModel.addUndo(update, true);
                    iModel.unselect();
                }

            }
        }
        else if (event.isShiftDown()) {
            keyState = KeyState.SHIFTISDOWN;
            if (currentState == State.PREPARE_CREATE) {
                model.addTarget(prevX, prevY);
            }
            else if (currentState == State.DRAGGING || currentState == State.READY) {
                model.resizeTarget(prevX, prevY);
            }
        }

        else if (event.getCode() == KeyCode.DELETE && iModel.getSelected() != null) {
            iModel.getSelected().forEach(t -> {
                model.removeSelectedTarget(t);
            });
            iModel.unselect();
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (event.getCode().equals(KeyCode.CONTROL)) {
            //currentState = State.DRAGGING;
        }
        keyState = KeyState.NONE;
    }
}
