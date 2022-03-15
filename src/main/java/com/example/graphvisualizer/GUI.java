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

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane graph = new Pane();
        graph.setOnMouseClicked( event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Circle circle = new Circle();
                circle.setCenterX(event.getSceneX());
                circle.setCenterY(event.getSceneY());
                circle.setRadius(25);
                circle.setFill(Color.BLACK);

                Circle innerCircle = new Circle();
                innerCircle.setCenterX(event.getSceneX());
                innerCircle.setCenterY(event.getSceneY());
                innerCircle.setRadius(20);
                innerCircle.setFill(Color.WHITE);

                Text text = new Text("N");
                text.setX(event.getSceneX());
                text.setY(event.getSceneY());
                text.setFill(Color.BLACK);
                Group vertex = new Group();
                vertex.getChildren().addAll(circle, innerCircle, text);

                graph.getChildren().add(vertex);
                System.out.printf("Added circle at x:%f y:%f\n", event.getSceneX(), event.getSceneY());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                graph.getChildren().remove(((Node)event.getTarget()).getParent());
                System.out.println(event.getTarget());
            }
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