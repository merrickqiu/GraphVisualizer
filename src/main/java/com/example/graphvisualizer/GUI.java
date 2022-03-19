package com.example.graphvisualizer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        Graph<String> graph = new Graph<>();
        PropertyDisplay display = new PropertyDisplay(graph);

        Pane graphPane = new Pane();
        graphPane.setPrefSize(1600, 1200);
        EventHandler<MouseEvent> normalClick = event -> {
            // Vertex Creation
            if (event.getButton() == MouseButton.PRIMARY) {
                graph.addVertex(vertexCount.toString());
                display.refresh();

                Vertex vertex = new Vertex(event.getSceneX(), event.getSceneY(), vertexCount.toString());
                graphPane.getChildren().add(vertex);
                vertexCount++;
                System.out.printf("Added circle at x:%f y:%f\n", event.getSceneX(), event.getSceneY());
            }
            //Edge Handling
            else if (event.getButton() == MouseButton.SECONDARY) {
                // Select Start Node
                if (startVertex == null) {
                    startVertex = (Vertex)((Node)event.getTarget()).getParent();
                    startVertex.setColor(Color.RED);
                    System.out.println("Added Start Node");
                }
                // Select End Node
                else {
                    Vertex endVertex = (Vertex)((Node)event.getTarget()).getParent();
                    // Undo Select
                    if (startVertex == endVertex) {
                        System.out.println("Undo Select");
                        startVertex.setColor(Color.WHITE);
                        startVertex = null;
                        return;
                    }
                    //Delete edge
                    for (Edge edge : startVertex.edges) { // jank way to test if edge already exists
                        if (edge.v1 == startVertex && edge.v2 == endVertex ||
                                edge.v1 == endVertex && edge.v2 == startVertex) {
                            System.out.println("Deleting edge");
                            graph.removeEdge(startVertex.getLabel(), endVertex.getLabel());
                            display.refresh();

                            graphPane.getChildren().remove(edge);
                            startVertex.edges.remove(edge);
                            endVertex.edges.remove(edge);
                            startVertex.setColor(Color.WHITE);
                            startVertex = null;
                            return;
                        }
                    }
                    //Create edge
                    graph.addEdge(startVertex.getLabel(), endVertex.getLabel());
                    display.refresh();

                    Edge edge = new Edge(startVertex, endVertex);
                    graphPane.getChildren().add(edge);

                    startVertex.addEdge(edge);
                    endVertex.addEdge(edge);

                    startVertex.setColor(Color.WHITE);
                    startVertex = null;
                    System.out.println("Created Edge");
                }
            }
        };
        EventHandler<MouseEvent> shiftClick = event -> {
            Vertex vertex = (Vertex)((Node)event.getTarget()).getParent();
            graph.removeVertex(vertex.getLabel());
            display.refresh();

            if (vertex == startVertex) {
                startVertex = null;
            }
            //Edge removal
            while(!vertex.edges.isEmpty()) {
                Edge edge = vertex.edges.get(0);
                graphPane.getChildren().remove(edge);
                edge.delete();
            }
            graphPane.getChildren().remove(vertex);
            System.out.println("Removed Circle: " + event.getTarget());
        };
        EventHandler<MouseEvent> handleClick = event -> {
            if (event.isShiftDown()) {
                shiftClick.handle(event);
            }
            else {
                normalClick.handle(event);
            }
        };

        // Vertex Movement
        EventHandler<MouseEvent> vertexDrag = event -> {
            Vertex vertex = (Vertex)((Node)event.getTarget()).getParent();
            vertex.setXY(event.getSceneX(), event.getSceneY());
            vertex.refreshEdges();
        };

        MouseHandler vertexMouseHandler = new MouseHandler(handleClick, vertexDrag);
        graphPane.addEventHandler(MouseEvent.ANY, vertexMouseHandler);

        GridPane root = new GridPane();

        root.add(graphPane, 0, 0, 1, 1);
        root.add(display, 1, 0, 1, 1);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Graph Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
