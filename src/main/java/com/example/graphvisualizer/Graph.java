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
        return adjList.values().stream().mapToInt(List::size).min().orElse(0);
    }

    public int getMaxDegree() {
        return adjList.values().stream().mapToInt(List::size).max().orElse(0);
    }

    public String getAverageDegree() {
        try {
            return String.format("%.2f", adjList.values().stream()
                    .mapToInt(List::size).average()
                    .orElseThrow(NoSuchElementException::new));
        } catch (NoSuchElementException e){
            return "NaN";
        }
    }

    public String getGirth() {
        Integer girth = null;
        for (T source : adjList.keySet()) {
            List<T> neighbors = new ArrayList<>(adjList.get(source)); // copy so that foreach works
            for (T destination : neighbors) {
                removeEdge(source, destination);
                int distance = dijkstraDistance(source, destination);
                System.out.println(adjList.get(source).toString() + adjList.get(destination));
                System.out.printf("Distance from %s to %s: %d\n", source, destination, distance);
                if (distance != -1 && (girth == null || distance+1 < girth)) {
                    girth = distance + 1;
                }
                addEdge(source, destination);
            }
        }
        return girth == null ? "INFINITE GIRTH" : girth.toString();
    }

    class Node implements Comparable<Node> {
        public T vertex;
        public Node previous;
        public double cost;
        public Node(T vertex, T previous, Double cost) {
            this.vertex = vertex;
            this.cost = cost;
        }
        @Override
        public int compareTo(Node that) {
            return Double.compare(this.cost, that.cost);
        }
    }
    private int dijkstraDistance(T v1, T v2) {
        List<T> path = dijkstraPath(v1, v2);
        return path == null ? -1 : path.size()-1;
    }

    // https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Using_a_priority_queue
    private List<T> dijkstraPath(T v1, T v2) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Map<T, Node> nodeMap = new HashMap<>();

        Node sourceNode = new Node(v1, null, 0.0);
        queue.add(sourceNode);
        nodeMap.put(v1, sourceNode);

        for (T vertex : adjList.keySet()) {
            if (vertex != v1) {
                Node node = new Node(vertex, null, Double.POSITIVE_INFINITY);
                queue.add(node);
                nodeMap.put(vertex, node);
            }
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.vertex == v2) {
                if (current.cost == Double.POSITIVE_INFINITY) {
                    return null;
                }
                List<T> path = new LinkedList<>();
                Node pathNode = nodeMap.get(v2);
                while(pathNode != null) {
                    path.add(0, pathNode.vertex);
                    pathNode = pathNode.previous;
                }
                return path;
            }
            for(T neighbor : adjList.get(current.vertex)) {
                Node neighborNode = nodeMap.get(neighbor);
                double altDistance = current.cost + 1.0;
                if (altDistance < neighborNode.cost) {
                    neighborNode.cost = altDistance;
                    neighborNode.previous = current;
                }
            }
        }
        return null;
    }
    public boolean isComplete() {
        return getEdgeCount() == (getVertexCount()*(getVertexCount()-1)) / 2;
    }

    public String regularity() {
        if (getMinDegree() == getMaxDegree()) {
            return getMinDegree() + "-regular";
        } else {
            return "Not regular";
        }
    }

    protected class BFS implements Iterable<T> {
        T source;
        public BFS(T source) {
            this.source = source;
        }
        public Iterator<T> iterator() {
            return new iteratorBFS(source);
        }
        class iteratorBFS implements Iterator<T> {
            Set<T> visited = new HashSet<>();
            Queue<T> searchQueue = new LinkedList<>();

            public iteratorBFS(T source) {
                searchQueue.add(source);
            }
            public boolean hasNext() {
                return !searchQueue.isEmpty();
            }

            public T next() {
                if(searchQueue.isEmpty()) {
                    throw new NoSuchElementException();
                }
                T next = searchQueue.remove();
                visited.add(next);
                for(T neighbor : adjList.get(next)) {
                    if (!visited.contains(neighbor)) {
                        searchQueue.add(neighbor);
                    }
                }
                return next;
            }
        }
    }
    protected class DFS implements Iterable<T> {
        T source;
        public DFS(T source) {
            this.source = source;
        }
        public Iterator<T> iterator() {
            return new iteratorDFS(source);
        }
        class iteratorDFS implements Iterator<T> {
            Set<T> visited = new HashSet<>();
            Stack<T> searchStack = new Stack<>();

            public iteratorDFS(T source) {
                searchStack.push(source);
            }
            public boolean hasNext() {
                return !searchStack.isEmpty();
            }

            public T next() {
                if(searchStack.isEmpty()) {
                    throw new NoSuchElementException();
                }
                T next = searchStack.pop();
                visited.add(next);
                for(T neighbor : adjList.get(next)) {
                    if (!visited.contains(neighbor)) {
                        searchStack.push(neighbor);
                    }
                }
                return next;
            }
        }
    }

    private List<T> subgraphSources() {
        List<T> sources = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        for (T vertex : adjList.keySet()) {
            if (visited.contains(vertex)) {
                continue;
            }
            sources.add(vertex);
            (new BFS(vertex)).forEach(visited::add);
        }
        return sources;
    }
    public boolean isBipartite() {
        if (adjList.isEmpty()) {
            return true;
        }
        for (T source : subgraphSources()) {
            if (!isBipartite(source)) {
                return false;
            }
        }
        return true;
    }
    public boolean isBipartite(T source) {
        Set<T> red = new HashSet<>();
        Set<T> blue = new HashSet<>();
        red.add(source);
        for(T vertex : new BFS(source)) {
            boolean isRed = red.contains(vertex);
            Set<T> currentColor = isRed ? red : blue;
            Set<T> otherColor = isRed ? blue : red;
            currentColor.add(vertex);

            for (T neighbor : adjList.get(vertex)) {
                if (currentColor.contains(neighbor)) {
                    return false;
                }
                otherColor.add(neighbor);
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
        //g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 0);
        g.removeEdge(0, 1);
        //System.out.println(g.dijkstraPath(0, 1));
        System.out.println(g.dijkstraPath(1, 0));
    }
}

