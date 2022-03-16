package com.example.graphvisualizer;

import java.util.*;

// https://www.geeksforgeeks.org/implementing-generic-graph-in-java/
public class Graph<T> {
    private final Map<T, List<T>> adjList = new HashMap<>();

    public void addVertex(T vertex) {
        adjList.put(vertex, new LinkedList<T>());
    }

    public void removeVertex(T vertex) {
        adjList.values().forEach(e -> e.remove(vertex));
        adjList.remove(vertex);
    }

    public void addEdge(T source, T destination) {
        if (!adjList.containsKey(source)) {
            addVertex(source);
        }
        if (!adjList.containsKey(destination)) {
            addVertex(destination);
        }

        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public void removeEdge(T source, T destination) {
        if (!adjList.containsKey(source)) {
            throw new IllegalArgumentException("Source does not exist");
        }
        if (!adjList.containsKey(destination)) {
            throw new IllegalArgumentException("Destination does not exist");
        }

        adjList.get(source).remove(destination);
        adjList.get(destination).remove(source);
    }

    public boolean hasVertex(T vertex) {
        return adjList.containsKey(vertex);
    }

    public boolean hasEdge(T source, T destination) {
        return adjList.get(source).contains(destination);
    }

    public int getVertexCount() {
        return adjList.size();
    }

    public int getEdgeCount() {
//        int edgeCount = 0;
//        for (List<T> vertexes : adjList.values()) {
//          edgeCount += vertexes.size();
//        }
        int edgeCount = adjList.values().stream().map(List::size).reduce(0, Integer::sum);
        return edgeCount / 2;
    }

    public int getMinDegree() {
        try {
            return adjList.values().stream().mapToInt(List::size).min().orElseThrow(NoSuchElementException::new);
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }

    public int getMaxDegree() {
        try {
            return adjList.values().stream().mapToInt(List::size).max().orElseThrow(NoSuchElementException::new);
        }
        catch (NoSuchElementException e){
            return 0;
        }

    }
    public boolean isBipartite() {
        if (adjList.isEmpty()) {
            return true;
        }
        // Inefficient but works for small graphs
        for (T source : adjList.keySet()) {
            if (!isBipartite(source)) {
                return false;
            }
        }
        return true;
    }
    //Assumes that graph is all connected
    @Deprecated
    public boolean isBipartite(T source) {
        boolean isRed = true;
        Set<T> visited = new HashSet<>();
        Queue<T> searchQueue = new LinkedList<T>();

        Set<T> red = new HashSet<>();
        Set<T> blue = new HashSet<>();

        searchQueue.add(source);
        red.add(source);

        while (!searchQueue.isEmpty()) {
            // Visit next vertex
            T vertex = searchQueue.remove();
            visited.add(vertex);

            Set<T> currentColor = red.contains(vertex) ? red : blue;
            Set<T> otherColor = red.contains(vertex) ? blue : red;

            // Look through all unvisited neighbors
            for(T neighbor : adjList.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    // If neighbor is same color then not bipartite
                    if (currentColor.contains(neighbor)) {
                        return false;
                    }
                    // Otherwise color and add the neighbor
                    otherColor.add(neighbor);
                    searchQueue.add(neighbor);
                }
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (T vertex : adjList.keySet()) {
            builder.append(vertex.toString());
            builder.append(": ");
            builder.append(adjList.get(vertex).toString());
            builder.append("\n");
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addEdge(0, 4);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        System.out.println(g.getVertexCount());
        System.out.println(g.getEdgeCount());
        System.out.println(g.isBipartite());
        System.out.println(g);

        g.removeVertex(1);
        System.out.println(g.getVertexCount());
        System.out.println(g.getEdgeCount());
        System.out.println(g);

        g.removeEdge(0, 4);
        System.out.println(g.getVertexCount());
        System.out.println(g.getEdgeCount());
        System.out.println(g);

        Graph<Integer> bipartite = new Graph<>();
        bipartite.addEdge(0, 1);
        bipartite.addEdge(1, 2);
        bipartite.addEdge(2, 3);
        bipartite.addEdge(3, 4);
        bipartite.addEdge(4, 5);
        bipartite.addEdge(5, 0);
        System.out.println(bipartite.isBipartite());
        System.out.println(bipartite);
    }
}

