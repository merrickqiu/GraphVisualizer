package com.example.graphvisualizer;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Vertex extends Group {
    private final Circle circle, innerCircle;
    private final Text text;
    public List<Edge> edges;

    public Vertex(double x, double y, String label) {
        circle = new Circle(30, Color.BLACK);
        innerCircle = new Circle(25, Color.WHITE);
        text = new Text(label);

        circle.setCenterX(x);
        circle.setCenterY(y);
        innerCircle.setCenterX(x);
        innerCircle.setCenterY(y);
        text.setX(x);
        text.setY(y);

        this.getChildren().addAll(circle, innerCircle, text);
        this.getChildren().addAll();
        edges = new LinkedList<>();
    }

    public void setX(double x) {
        circle.setCenterX(x);
        innerCircle.setCenterX(x);
        text.setX(x);
    }

    public void setY(double y) {
        circle.setCenterY(y);
        innerCircle.setCenterY(y);
        text.setY(y);
    }

    public void setXY(double x, double y) {
        setX(x);
        setY(y);
    }

    public double getX() {
        return circle.getCenterX();
    }

    public double getY() {
        return circle.getCenterY();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void refreshEdges() {
        for (Edge e : edges) {
            e.refresh();
        }
    }
}
