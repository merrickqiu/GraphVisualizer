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
    private Integer vertexCount = 0; // The next vertex number
    private Vertex startVertex = null; // The selected start vertex for edge creation
    @Override
    public void start(Stage stage) throws IOException {
        Pane graph = new Pane();
        EventHandler<MouseEvent> vertexClick = event -> {
            // Edge Creation
            if (event.isShiftDown()) {
                // Select Start Node
                if (startVertex == null) {
                    startVertex = (Vertex)((Node)event.getTarget()).getParent();
                    startVertex.setColor(Color.RED);
                    System.out.println("Added Start Node");
                }
                // Select End Node
                else {
                    Vertex endVertex = (Vertex)((Node)event.getTarget()).getParent();

                    Edge edge = new Edge(startVertex, endVertex);
                    graph.getChildren().add(edge);

                    startVertex.addEdge(edge);
                    endVertex.addEdge(edge);

                    startVertex.setColor(Color.WHITE);
                    startVertex = null;
                    System.out.println("Created Edge");
                }
            }
            else {
                // Vertex Creation
                if (event.getButton() == MouseButton.PRIMARY) {
                    Vertex vertex = new Vertex(event.getSceneX(), event.getSceneY(), vertexCount.toString());
                    graph.getChildren().add(vertex);
                    vertexCount++;
                    System.out.printf("Added circle at x:%f y:%f\n", event.getSceneX(), event.getSceneY());
                }
                //Vertex Deletion
                else if (event.getButton() == MouseButton.SECONDARY) {
                    Vertex vertex = (Vertex)((Node)event.getTarget()).getParent();
                    //Edge removal
                    while(!vertex.edges.isEmpty()) {
                        Edge edge = vertex.edges.get(0);
                        graph.getChildren().remove(edge);
                        edge.delete();
                    }
                    graph.getChildren().remove(vertex);
                    System.out.println("Removed Circle: " + event.getTarget());
                }
            }
        };
        // Vertex Movement
        EventHandler<MouseEvent> vertexDrag = event -> {
            Vertex vertex = (Vertex)((Node)event.getTarget()).getParent();
            vertex.setXY(event.getSceneX(), event.getSceneY());
            vertex.refreshEdges();
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