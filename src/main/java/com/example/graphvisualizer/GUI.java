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
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;

class Vertex extends Group {
    Circle circle, innerCircle;
    Text text;
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
}
public class GUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane graph = new Pane();
        graph.setOnMouseClicked( event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Vertex vertex = new Vertex(event.getSceneX(), event.getSceneY(), "N");
                graph.getChildren().add(vertex);
                System.out.printf("Added circle at x:%f y:%f\n", event.getSceneX(), event.getSceneY());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                graph.getChildren().remove(((Node)event.getTarget()).getParent());
                System.out.println("Removed Circle: " + event.getTarget());
            }
        });

        graph.setOnMouseDragged( event -> {
            Vertex vertex = (Vertex)((Node)event.getTarget()).getParent();
            vertex.setX(event.getSceneX());
            vertex.setY(event.getSceneY());
        });

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