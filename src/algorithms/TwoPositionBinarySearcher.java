package algorithms;

import Parser.DataRecord;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import models.DirectionEnum;
import models.PositionLabel;
import models.Rectangle;

import java.util.*;

public class TwoPositionBinarySearcher extends BinarySearcher {




    // edges of implication graph and its inverse
    private List<Integer>[] adj;
    private List<Integer>[] adjInv;


    ImplicationGraphSolver solver;


    public TwoPositionBinarySearcher() {
        solver = new ImplicationGraphSolver();
    }



    @Override
    double[] getSolutionSpace(DataRecord record) {
        double maxHeight = Math.min(20000, (20000.0 * record.aspectRatio));
        SortedSet<Double> solutions = new TreeSet<>();

        double index = 0;
        while (index < maxHeight) {
            solutions.add(new Double(index));
            index++;
        }

        index = 0;
        while (index * record.aspectRatio < maxHeight) {
            solutions.add(new Double(index * record.aspectRatio));
            index += 0.5;
        }



        double[] solutionSpace = new double[solutions.size()];
        int counter = 0;
        for (Double d : solutions) {
            solutionSpace[counter] = d;
            counter++;
        }


        return solutionSpace;
    }

    @Override
    boolean isSolvable(DataRecord record, double height) {
        int noInputs = record.labels.size();

        initializeGraph(record, height, noInputs);


        return solver.isSolvable(adj, adjInv);

    }

    @Override
    void getSolution(DataRecord record, double height) {
        int noPoints = record.labels.size();
        initializeGraph(record, height, noPoints);

        // set height
        record.height = height;

        boolean[] solution = solver.getSolution(adj, adjInv);


        for (int i = 0; i < solution.length; i++) {
            if (solution[i]) {
                ((PositionLabel) record.labels.get(i)).setDirection(DirectionEnum.NE);
            } else {
                ((PositionLabel) record.labels.get(i)).setDirection(DirectionEnum.NW);
            }
        }

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

        double width = height * record.aspectRatio;

        for (LabelInterface point : record.labels) {
            double x = point.getXMin();
            double y = point.getYMin();


            // label NE of point intersects with NE lables
            Collection<GeometryInterface> collection = record.collection.query2D(new Rectangle(x - width, y - height, x + width, y + height));
            if (collection != null) {
                for (GeometryInterface square : collection) {
                    addEdgeAndInverse(point.getID(), ((LabelInterface) square).getID() + noPoints, noPoints);
                }
            }

            // label NE of point intersects with NW lables
            collection = record.collection.query2D(new Rectangle(x, y - height, x + 2 * width, y + height));
            // System.out.println((x) + " " + (y - height) + " " + (x + 2 * width) + " " + (y + height));
            if (collection != null) {
                for (GeometryInterface square : collection) {
                    addEdgeAndInverse(point.getID(), ((LabelInterface) square).getID(), noPoints);
                }
            }

            // label NW of point intersects with NE lables
            collection = record.collection.query2D(new Rectangle(x - 2 * width, y - height, x, y + height));
            if (collection != null) {
                for (GeometryInterface square : collection) {
                    addEdgeAndInverse(point.getID() + noPoints, ((LabelInterface) square).getID() + noPoints, noPoints);
                }
            }

            // label NW of point intersects with NW lables
            collection = record.collection.query2D(new Rectangle(x - width, y - height, x + width, y + height));
            if (collection != null) {
                for (GeometryInterface square : collection) {
                    addEdgeAndInverse(point.getID() + noPoints, ((LabelInterface) square).getID(), noPoints);
                }
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
}
