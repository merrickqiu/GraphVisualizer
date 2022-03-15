package com.example.graphvisualizer;

import javafx.scene.shape.Line;

public class Edge extends Line {
    private Vertex v1, v2;

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;

        refresh();
    }

    public void refresh() {
        this.setStartX(v1.getX());
        this.setStartY(v1.getY());
        this.setEndX(v2.getX());
        this.setEndY(v2.getY());
    }
}
