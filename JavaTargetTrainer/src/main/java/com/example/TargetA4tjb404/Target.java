/*
Tyler Boechler
 */

package com.example.TargetA4tjb404;

public class Target {
    double x, y;
    double radius;

    int id;

    public Target(double nx, double ny, int id) {
        x = nx;
        y = ny;
        radius = 50.0;

        this.id = id;
    }

    public Target(double x, double y, double r, int id) {
        this.x = x;
        this.y = y;
        this.radius = r;

        this.id = id;
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public void resize(double dr) {
        if (this.radius >= 5) {
            radius += dr;
        }
        else {this.radius = 6;}

    }

    public boolean contains(double cx, double cy) {
        return dist(cx,cy,x,y) <= radius;
    }

    private double dist(double x1,double y1,double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }

    @Override
    public String toString() {
        return "Target{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }
}
