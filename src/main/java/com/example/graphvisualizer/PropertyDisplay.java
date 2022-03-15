package com.example.graphvisualizer;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PropertyDisplay extends VBox {
    final Graph<String> graph;
    List<Property> properties;

    public PropertyDisplay(Graph<String> graph) {
        this.graph = graph;
        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        properties = new ArrayList<>();
        this.getChildren().add(new Text("----GRAPH PROPERTIES----"));
        addProperty("Vertices:", graph::getVertexCount);
        addProperty("Edges: ", graph::getEdgeCount);
        addProperty("Minimum Degree: ", graph::getMinDegree);
        addProperty("Maximum Degree: ", graph::getMaxDegree);
        addProperty("Bipartite: ", graph::isBipartite);
    }

    public void addProperty(String label, Supplier<?> propertySupplier) {
        Property property = new Property(label, () -> String.valueOf(propertySupplier.get()));
        properties.add(property);
        this.getChildren().add(property);
    }

    public void refresh() {
        properties.forEach(Property::refresh);
    }
}

class Property extends Text {
    String label;
    Supplier<String> propertySupplier;
    public Property(String label, Supplier<String> propertySupplier){
        this.label = label;
        this.propertySupplier = propertySupplier;
        refresh();
    }

    public void refresh() {
        this.setText(label + propertySupplier.get());
    }
}


