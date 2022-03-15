package com.example.graphvisualizer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;

public class GUI extends Application {
    private Integer nodeCount = 0;
    private Vertex startVertex = null;
    @Override
    public void start(Stage stage) throws IOException {
        Pane graph = new Pane();
        EventHandler<MouseEvent> vertexClick = event -> {
            if (event.isShiftDown()) {
                if (startVertex == null) {
                    startVertex = (Vertex)((Node)event.getTarget()).getParent();
                } else {
                    Vertex endVertex = (Vertex)((Node)event.getTarget()).getParent();
                    startVertex.connect(endVertex);
                    startVertex = null;
                }
            } else {
                if (event.getButton() == MouseButton.PRIMARY) {
                    Vertex vertex = new Vertex(event.getSceneX(), event.getSceneY(), nodeCount.toString());
                    graph.getChildren().add(vertex);
                    nodeCount++;
                    System.out.printf("Added circle at x:%f y:%f\n", event.getSceneX(), event.getSceneY());
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    graph.getChildren().remove(((Node)event.getTarget()).getParent());
                    System.out.println("Removed Circle: " + event.getTarget());
                }
            }
        };
        EventHandler<MouseEvent> vertexDrag = event -> {
            Vertex vertex = (Vertex)((Node)event.getTarget()).getParent();
            vertex.setXY(event.getSceneX(), event.getSceneY());
        };

        MouseHandler vertexMouseHandler = new MouseHandler(vertexClick, vertexDrag);
        graph.addEventHandler(MouseEvent.ANY, vertexMouseHandler);

        StackPane root = new StackPane();
        root.getChildren().addAll(graph);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Graph Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}