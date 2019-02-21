package algorithms;

import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;
import Parser.DataRecord;
import models.BoundingBox;
import models.DirectionEnum;
import models.PositionLabel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class TwoPositionBinarySearcher extends BinarySearcher {


    // variable for dfs to keep track of visited nodes
    private boolean[] visited;
    private boolean[] visitedInv;
    private Stack<Integer> s;

    // edges of implication graph and its inverse
    private List<Integer>[] adj;
    private List<Integer>[] adjInv;


    // stores for each node the component it is in
    private int[] scc;
    private List<Integer> componentsInReverseTopOrder;

    // keep track of current component
    private int counter;


    // keeps track of the rect height of last created graph


    // keeps track of which rectangles have been set in getSolution()
    private boolean[] isSet;


    @Override
    boolean isSolvable(DataRecord record, double height) {
        int noInputs = record.labels.size();

        createGraph(record, height, noInputs);

        // check if label and its inverse are in the same component
        for (int i = 0; i < noInputs; i++) {
            if (scc[i] == scc[i + noInputs]) {
                return false;
            }
        }
        return true;

    }

    @Override
    void getSolution(DataRecord record, double height) {
        int noPoints = record.labels.size();
        createGraph(record, height, noPoints);

        // set height
        record.height = height;

//        for (int i = 0; i < adj.length; i++) {
//            for(int j : adj[i]) {
//                System.out.println(i + " " + j);
//            }
//        }

        // assign labels to each point in reverse topological order
        isSet = new boolean[noPoints];
        for (Integer i : componentsInReverseTopOrder) {
            if (!isSet[i % noPoints]) {
                assignTrue(record, i, noPoints);
            }
        }
    }

    private void createGraph(DataRecord record, double height, int noPoints) {
        initializeGraph(record, height, noPoints);
        createComponents(noPoints);
    }


    private void initializeGraph(DataRecord record, double height, int noPoints) {
        // ------------ initialize variables ------------
        adj = new ArrayList[noPoints * 2];
        adjInv = new ArrayList[noPoints * 2];


        for (int i = 0; i < noPoints * 2; i++) {
            adj[i] = new ArrayList<>();
            adjInv[i] = new ArrayList<>();
        }

        // ------------ adding edges to graph ------------
        // loop over every point and for both rectangles check overlaps

        for (LabelInterface point : record.labels) {
            double x = point.getXMin();
            double y = point.getYMin();


            // label NE of point intersects with NE lables
            Collection<SquareInterface> collection = record.collection.query2D(new BoundingBox(x - height, y - height, x + height, y + height));

            if (collection != null) {
                for (SquareInterface square : collection) {
                    addEdgeAndInverse(point.getID(), ((LabelInterface) square).getID() + noPoints, noPoints);
                }
            }

            // label NE of point intersects with NW lables
            collection = record.collection.query2D(new BoundingBox(x, y - height, x + 2 * height, y + height));
            if (collection != null) {
                for (SquareInterface square : collection) {
                    addEdgeAndInverse(point.getID(), ((LabelInterface) square).getID(), noPoints);
                }
            }

            // label NW of point intersects with NE lables
            collection = record.collection.query2D(new BoundingBox(x - 2 * height, y - height, x, y + height));
            if (collection != null) {
                for (SquareInterface square : collection) {
                    addEdgeAndInverse(point.getID() + noPoints, ((LabelInterface) square).getID() + noPoints, noPoints);
                }
            }

            // label NW of point intersects with NW lables
            collection = record.collection.query2D(new BoundingBox(x - height, y - height, x + height, y + height));
            if (collection != null) {
                for (SquareInterface square : collection) {
                    addEdgeAndInverse(point.getID() + noPoints, ((LabelInterface) square).getID(), noPoints);
                }
            }
        }

    }



    private void createComponents(int noPoints) {
        // initialize variables
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


    private void addEdgeAndInverse(int a, int b, int noNodes) {
        if (a % noNodes == b % noNodes) {
            return;
        }
        adj[a].add(b);
        adjInv[b].add(a);
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

    private void assignTrue(DataRecord record, int node, int noPoints) {
        if (node < noPoints) {
            isSet[node] = true;
            ((PositionLabel) record.labels.get(node)).setDirection(DirectionEnum.NE);
        } else {
            isSet[node - noPoints] = true;
            ((PositionLabel) record.labels.get(node % noPoints)).setDirection(DirectionEnum.NW);
        }
        for (int i : adj[node]) {
            if (!isSet[i % noPoints] && scc[i] == scc[node]) {
                assignTrue(record, i, noPoints);
            }
        }
    }
}
