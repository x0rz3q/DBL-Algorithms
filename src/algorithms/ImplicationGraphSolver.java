package algorithms;

import java.util.*;

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

    private int noPoints;

    // keeps track of which rectangles have been set in getSolution()
    private boolean[] isSet;

    /**
     * we assume that we have a 2pos problem with n nodes
     *
     * @param adj    List<Integer>[] implication graph: an array of List<Integer> of length 2n where the first
     *               n values correspond to the first label of each node (value i corresponds to first label of node i)
     *               and the next n values correspond to the second label of each node (value. i + n corresponds to second label of node i)
     *               with 0 <= i < n.
     *               Each value at index i contains a List<Integers> where all the values j in that list represent one implication of the
     *               graph where having label i implies also having label j for all j in \adj[i]
     * @param adjInv List<Integer>[] inverse of implication graph:
     *               \forall(i; adj.has(i); \forall(j; adj[i].has(j); adjInv[j].contains(i)))
     *               \forall(i; adjInv.has(i); \forall(j; adjInv[i].has(j); adj[j].contains(i)))
     * @return \return == true iff the implication graph has a valid solution
     */
    public boolean isSolvable(List<Integer>[] adj, List<Integer>[] adjInv) {
        noPoints = adj.length / 2;

        createComponents(adj, adjInv);

        for (int i = 0; i < noPoints; i++) {
            if (scc[i] == scc[i + noPoints]) {
                return false;
            }
        }

        return true;
    }

    private void createComponents(List<Integer>[] adj, List<Integer>[] adjInv) {
        //get parameters
        this.adj = adj;
        this.adjInv = adjInv;

        // initialize variables
        noPoints = adj.length / 2;
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
    }

    /**
     * we assume that we have a 2pos problem with n nodes
     *
     * @param adj    List<Integer>[] implication graph: an array of List<Integer> of length 2n where the first
     *               n values correspond to the first label of each node (value i corresponds to first label of node i)
     *               and the next n values correspond to the second label of each node (value. i + n corresponds to second label of node i)
     *               with 0 <= i < n.
     *               Each value at index i contains a List<Integers> where all the values j in that list represent one implication of the
     *               graph where having label i implies also having label j for all j in \adj[i]
     * @param adjInv List<Integer>[] inverse of implication graph:
     *               \forall(i; adj.has(i); \forall(j; adj[i].has(j); adjInv[j].contains(i)))
     *               \forall(i; adjInv.has(i); \forall(j; adjInv[i].has(j); adj[j].contains(i)))
     * @return \return represents a possible solution to adj. Where we have \result.length = n and \result[i] represents the label of node i
     * where node i has the first label, if \result[i] is true and otherwise node i needs to have the second label
     * @pre isSolvable(adj, adjInv)
     */
    public boolean[] getSolution(List<Integer>[] adj, List<Integer>[] adjInv) {
        noPoints = adj.length / 2;
        boolean[] solution = new boolean[noPoints];

        createComponents(adj, adjInv);

        // assign labels to each point in reverse topological order
        isSet = new boolean[noPoints];
        for (Integer i : componentsInReverseTopOrder) {
            if (!isSet[i % noPoints]) {
                assignTrue(solution, i, adj);
            }
        }

        return solution;
    }
//
//    private void dfsFirst(int start) {
//        if (visited[start])
//            return;
//
//        visited[start] = true;
//
//        for (Integer i : adj[start]) {
//            dfsFirst(i);
//        }
//
//        s.push(start);
//    }

    private boolean allVisited(int start) {
        for (Integer i : adj[start]) {
            if (!visited[i])
                return false;
        }

        return true;
    }

    private Integer next(Integer start) {
        for (Integer i : adj[start]) {
            if (!visited[i])
                return i;
        }

        return null;
    }

    private void dfsFirst(Integer start) {
        Stack<Integer> integers = new Stack<>();
        integers.add(start);

        do {
            Integer next = integers.peek();
            visited[next] = true;

            if (!allVisited(next)) {
                integers.add(next(next));
            } else {
                next = integers.pop();
                s.add(next);
            }
        } while(!integers.isEmpty());
    }

    private void dfsSecond(int start) {
        Deque<Integer> nodesToVisit = new ArrayDeque<>();
        nodesToVisit.push(start);

        while (!nodesToVisit.isEmpty()) {
            int current = nodesToVisit.pop();
            if (visitedInv[current]) {
                continue;
            }
            visitedInv[current] = true;

            for (int i : adjInv[current]) {
                nodesToVisit.push(i);
            }
            scc[current] = counter;
        }
    }

    private void assignTrue(boolean[] solution, int node, List<Integer>[] adj) {
        int noPoints = solution.length;

        Deque<Integer> nodesToVisit = new ArrayDeque<Integer>();
        nodesToVisit.push(node);

        while (!nodesToVisit.isEmpty()) {
            int current = nodesToVisit.pop();
            if (current < noPoints) {
                isSet[current] = true;
                solution[current % noPoints] = true;
            } else {
                isSet[current - noPoints] = true;
                solution[current % noPoints] = false;
            }

            for (int i : adj[current]) {
                if (!isSet[i % noPoints] && scc[i] == scc[current]) {
                    nodesToVisit.push(i);
                }
            }
        }
    }
}
