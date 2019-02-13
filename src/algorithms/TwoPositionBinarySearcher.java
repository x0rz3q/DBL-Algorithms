package algorithms;

import models.DataRecord;

import java.util.ArrayList;
import java.util.Stack;

public class TwoPositionBinarySearcher extends BinarySearcher {


    // variable for dfs to keep track of visited nodes
    private boolean[] visited;
    private Stack<Integer> s;

    // edges of implication graph and its inverse
    private ArrayList<Integer>[] adj;
    private ArrayList<Integer>[] adjInv;


    // stores for each node the component it is in
    private int[] scc;

    // keep track of current component
    private int counter;


    // keeps track of the rect height of last created graph
    private float heightLastGraph;


    // keeps track of which rectangles have been set in getSolution()
    private boolean[] isSet;


    @Override
    boolean isSolvable(DataRecord record, float height) {
        if (heightLastGraph != height) {
            createGraph(record, height);
        }
        return Kosaraju(record.points.size());

    }

    @Override
    void getSolution(DataRecord record, float height) {
        if (heightLastGraph != height) {
            createGraph(record, height);
        }

        int noPoints = record.points.size();

        isSet = new boolean[noPoints];

        for (int i = 0; i < noPoints; i++) {
            if (!isSet[i]) {
                assignTrue(i, noPoints);
            }
        }
    }


    private void createGraph(DataRecord record, float height) {

        heightLastGraph = height;

        int noPoints = record.points.size();

        // ------------ initialize variables ------------
        visited = new boolean[noPoints * 2];
        s = new Stack<>();
        scc = new int[noPoints * 2];
        counter = 0;

        adj = new ArrayList[noPoints * 2];
        adjInv = new ArrayList[noPoints * 2];



        for (int i = 0; i < noPoints * 2; i++) {
            adj[i] = new ArrayList<>();
            adjInv[i] = new ArrayList<>();
        }


        // ------------ adding edges to graph ------------
        // TODO: I need the right data structure for that
        // loop over every point and for both rectangles check overlaps

    }

    private boolean Kosaraju(int noInputs) {
        // Step 1: dfs on original graph
        for (int i = 0; i < noInputs * 2; i++) {
            if (!visited[i]) {
                dfsFirst(i);
            }
        }

        // Step 2: traverse inverse graph based on s
        while (!s.empty()) {
            int n = s.pop();

            if (visited[n]) {
                dfsSecond(n);
                counter++;

            }
        }

        for (int i = 0; i < noInputs; i++) {
            if (scc[i] == scc[i + noInputs]) {
                return false;
            }
        }
        return true;
    }


    private void addEdgeAndInverse(int a, int b) {
        adj[a].add(b);
        adj[b].add(a);
        adjInv[b].add(a);
        adjInv[a].add(b);
    }


    private void dfsFirst(int start) {
        if (visited[start]) {
            return;
        }

        visited[start] = true;

        for (int i : adj[start]) {
            dfsFirst(i);
        }

        s.push(start);
    }

    private void dfsSecond(int start) {
        if (!visited[start]) {
            return;
        }
        visited[start] = false;

        for (int i : adjInv[start]) {
            dfsSecond(i);
        }

        scc[start] = counter;
    }

    private void assignTrue(int start, int noNodes) {


        if (start < noNodes) {
            isSet[start] = true;
            // TODO: set start to true (needs data structures)
        } else {
            isSet[start - noNodes] = true;
            // TODO: set (start % noNodes) to false (needs data structures)
        }

        for (int i : adj[start]) {
            assignTrue(i, noNodes);
        }
    }
}
