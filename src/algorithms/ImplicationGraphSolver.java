package algorithms;

import java.util.*;

public class ImplicationGraphSolver {
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
        int noPoints = adj.length / 2;

        int[] scc = createComponents(adj, adjInv);

        // if node and negation are in same scc, return false
        for (int i = 0; i < noPoints; i++) {
            if (scc[i] == scc[i + noPoints]) {
                return false;
            }
        }

        return true;
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
        int noPoints = adj.length / 2;

        // final orientation for label of each point
        boolean[] solution = new boolean[noPoints];

        List<Integer> componentsInReverseTopOrder = new ArrayList<>();

        int[] scc = createComponents(adj, adjInv, componentsInReverseTopOrder);

        // assign labels to each point in reverse topological order
        boolean[] isSet = new boolean[noPoints];
        for (Integer i : componentsInReverseTopOrder) {
            if (!isSet[i % noPoints]) {
                assignTrue(solution, i, isSet, adj, scc);
            }
        }

        return solution;
    }

    private int[] createComponents(List<Integer>[] adj, List<Integer>[] adjInv) {
        return createComponents(adj, adjInv, new ArrayList<>());
    }

    private int[] createComponents(List<Integer>[] adj, List<Integer>[] adjInv, List<Integer> componentsInReverseTopOrder) {
        // initialize variables
        boolean[] visited = new boolean[adj.length];
        Stack<Integer> s = new Stack<>();

        int[] scc = new int[adj.length];

        // ------------- Kosarajus algorithm --------
        // Step 1: dfs on original graph
        for (int i = 0; i < adj.length; i++) {
            if (!visited[i]) {
                List<Integer> stack = dfsFirst(i, visited, adj);
                for (int j = 0; j < stack.size(); j++) {
                    s.add(stack.get(j));
                }
            }
        }

        int componentCounter = 0;
        visited = new boolean[adj.length];

        // Step 2: traverse inverse graph based on s
        while (!s.empty()) {
            int n = s.pop();
            if (!visited[n]) {
                componentsInReverseTopOrder.add(0, n);
                dfsSecond(n, componentCounter, visited, adjInv, scc);
                componentCounter++;
            }
        }
        return scc;
    }



    private boolean allVisited(int start, boolean[] visited, List<Integer>[] adj) {
        for (Integer i : adj[start]) {
            if (!visited[i])
                return false;
        }

        return true;
    }

    private Integer next(Integer start, boolean[] visited, List<Integer>[] adj) {
        for (Integer i : adj[start]) {
            if (!visited[i])
                return i;
        }

        return null;
    }

    private List<Integer> dfsFirst(Integer start, boolean[] visited, List<Integer>[] adj) {
        Stack<Integer> integers = new Stack<>();
        List<Integer> returnValue = new ArrayList<>();
        integers.add(start);

        while(!integers.isEmpty()) {
            Integer next = integers.peek();
            visited[next] = true;

            if (!allVisited(next, visited, adj)) {
                integers.add(next(next, visited, adj));
            } else {
                next = integers.pop();
                returnValue.add(next);
            }
        }
        return returnValue;
    }

    private void dfsSecond(int start, int componentCounter, boolean[] visited, List<Integer>[] adjInv, int[] scc) {
        Stack<Integer> nodesToVisit = new Stack<>();
        nodesToVisit.push(start);

        while (!nodesToVisit.isEmpty()) {
            int current = nodesToVisit.pop();
            if (visited[current]) {
                continue;
            }
            visited[current] = true;

            for (int i : adjInv[current]) {
                nodesToVisit.push(i);
            }
            scc[current] = componentCounter;
        }
    }

    private void assignTrue(boolean[] solution, int node, boolean[] isSet, List<Integer>[] adj, int[] scc) {
        int noPoints = solution.length;

        Stack<Integer> nodesToVisit = new Stack<>();
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
