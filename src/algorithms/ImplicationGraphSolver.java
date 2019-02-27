package algorithms;

import Parser.DataRecord;
import models.DirectionEnum;
import models.PositionLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ImplicationGraphSolver {

    // variable for dfs to keep track of visited nodes
    private boolean[] visited;
    private boolean[] visitedInv;
    private Stack<Integer> s;

    // stores for each node the component it is in
    private int[] scc;
    private List<Integer> componentsInReverseTopOrder;

    // keep track of current component
    private int counter;


    // edges of implication graph and its inverse
    private List<Integer>[] adj;
    private List<Integer>[] adjInv;



    // keeps track of which rectangles have been set in getSolution()
    private boolean[] isSet;


    
    public boolean isSolvable(List<Integer>[] adj, List<Integer>[] adjInv) {
        int noPoints = adj.length / 2;

        this.adj = adj;
        this.adjInv = adjInv;

        visited = new boolean[noPoints * 2];
        visitedInv = new boolean[noPoints * 2];
        s = new Stack<>();
        scc = new int[noPoints * 2];
        counter = 0;
        componentsInReverseTopOrder = new ArrayList<>();

        // Step 1: dfs on original graph
        for (int i = 0; i < noPoints * 2; i++) {
            if (!visited[i]) {
                dfsFirst(i);
            }
        }

        // Step 2: traverse inverse graph based on s
        while (!s.empty()) {
            int n = s.pop();
            if (!visitedInv[n]) {
                componentsInReverseTopOrder.add(0, n);
                dfsSecond(n);
                counter++;
            }
        }

        for (int i = 0; i < noPoints; i++) {
            if (scc[i] == scc[i + noPoints]) {
                return false;
            }
        }

        return true;
    }


    public boolean[] getSolution(List<Integer>[] adj, List<Integer>[] adjInv) {
        int noPoints = adj.length / 2;

        boolean[] solution = new boolean[noPoints];

        isSolvable(adj, adjInv);


        // assign labels to each point in reverse topological order
        isSet = new boolean[noPoints];
        for (Integer i : componentsInReverseTopOrder) {
            if (!isSet[i % noPoints]) {
                assignTrue(solution, i, adj);
            }
        }
        return solution;
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
        if (visitedInv[start]) {
            return;
        }
        visitedInv[start] = true;

        for (int i : adjInv[start]) {
            dfsSecond(i);
        }

        scc[start] = counter;
    }


    private void assignTrue(boolean[] solution, int node, List<Integer>[] adj) {
        int noPoints = solution.length;

        if (node < noPoints) {
            isSet[node] = true;
            solution[node % noPoints] = true;
        } else {
            isSet[node - noPoints] = true;
            solution[node % noPoints] = false;
        }
        for (int i : adj[node]) {
            if (!isSet[i % noPoints] && scc[i] == scc[node]) {
                assignTrue(solution, i, adj);
            }
        }
    }
}
