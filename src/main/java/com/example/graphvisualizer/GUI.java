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
        Graph<String> graph = new Graph<>();
        Display display = new Display(graph);

        Pane graphPane = new Pane();
        graphPane.setPrefSize(1600, 1200);
        EventHandler<MouseEvent> vertexClick = event -> {
            // Edge Creation/Deletion
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
                    // Don't allow self connection
                    if (startVertex == endVertex) {
                        System.out.println("Blocked self connection");
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
            else {
                // Vertex Creation
                if (event.getButton() == MouseButton.PRIMARY) {
                    graph.addVertex(vertexCount.toString());
                    display.refresh();

                    Vertex vertex = new Vertex(event.getSceneX(), event.getSceneY(), vertexCount.toString());
                    graphPane.getChildren().add(vertex);
                    vertexCount++;
                    System.out.printf("Added circle at x:%f y:%f\n", event.getSceneX(), event.getSceneY());
                }
                //Vertex Deletion
                else if (event.getButton() == MouseButton.SECONDARY) {
                    Vertex vertex = (Vertex)((Node)event.getTarget()).getParent();
                    graph.removeVertex(vertex.getLabel());
                    display.refresh();

                    //Edge removal
                    while(!vertex.edges.isEmpty()) {
                        Edge edge = vertex.edges.get(0);
                        graphPane.getChildren().remove(edge);
                        edge.delete();
                    }
                    graphPane.getChildren().remove(vertex);
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
        graphPane.addEventHandler(MouseEvent.ANY, vertexMouseHandler);

        GridPane root = new GridPane();

        root.add(graphPane, 0, 0, 1, 1);
        root.add(display, 1, 0, 1, 1);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Graph Visualizer");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

class Display extends VBox {
    private static final String minDegreeFormat = "Minimum Degree: %d";
    private static final String maxDegreeFormat = "Maximum Degree: %d";
    private static final String bipartiteFormat = "Bipartite?: %b";
    final Text title, minDegree, maxDegree, bipartite;
    Graph graph;
    public Display(Graph graph) {
        this.graph = graph;
        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        title = new Text("----Graph properties----");
        minDegree = new Text(String.format(minDegreeFormat, graph.getMinDegree()));
        maxDegree = new Text(String.format(maxDegreeFormat, graph.getMaxDegree()));
        bipartite = new Text(String.format(bipartiteFormat, graph.isBipartite()));
        this.getChildren().addAll(title, minDegree, maxDegree, bipartite);
    }

    public void refresh() {
        minDegree.setText(String.format(minDegreeFormat, graph.getMinDegree()));
        maxDegree.setText(String.format(maxDegreeFormat, graph.getMaxDegree()));
        bipartite.setText(String.format(bipartiteFormat, graph.isBipartite()));
    }
}