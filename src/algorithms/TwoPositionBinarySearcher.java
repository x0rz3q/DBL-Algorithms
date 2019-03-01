package algorithms;

import Parser.DataRecord;
import distance.FourPositionDistance;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import interfaces.models.PointInterface;
import models.DirectionEnum;
import models.PositionLabel;
import models.Rectangle;
import Collections.KDTree;

import java.util.*;

public class TwoPositionBinarySearcher extends BinarySearcher {
    // edges of implication graph and its inverse
    private List<Integer>[] adj;
    private List<Integer>[] adjInv;

    private ImplicationGraphSolver solver;

    public TwoPositionBinarySearcher() {
        solver = new ImplicationGraphSolver();
    }

    @Override
    double[] getSolutionSpace(DataRecord record) {
        FourPositionDistance distanceFunction = new FourPositionDistance();
        distanceFunction.setAspectRatio(record.aspectRatio);
        int k = 8; // number of nearest neighbours to search for
        if (record.labels.size() < 9) k = record.labels.size() - 1;
        Set<Double> conflictSizes = new HashSet<>();
        for (LabelInterface label : record.labels) {
            PointInterface point = label.getPOI();
            Set<GeometryInterface> nearestNeighbours = ((KDTree) record.collection).nearestNeighbours(distanceFunction, k, point);

            for (GeometryInterface target : nearestNeighbours) {
                double conflictSize = distanceFunction.calculate(point, ((LabelInterface) target).getPOI());
                conflictSizes.add(conflictSize);
                conflictSizes.add(conflictSize/2.0);
            }
        }

        double[] conflicts = new double[conflictSizes.size()];
        int i = 0;
        for (double size : conflictSizes) {
            conflicts[i++] = size;
        }

        // Optionally sort conflicts before passing them to general binary searcher
        Arrays.sort(conflicts);

        return conflicts;
    }

    @Override
    boolean isSolvable(DataRecord record, double height) {
        initializeGraph(record, height);

        return solver.isSolvable(adj, adjInv);
    }

    @Override
    void getSolution(DataRecord record, double height) {
        initializeGraph(record, height);

        boolean[] solution = solver.getSolution(adj, adjInv);

        // set solution
        record.height = height;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i]) {
                ((PositionLabel) record.labels.get(i)).setDirection(DirectionEnum.NE);
            } else {
                ((PositionLabel) record.labels.get(i)).setDirection(DirectionEnum.NW);
            }
        }
    }

    private void initializeGraph(DataRecord record, double height) {
        int noPoints = record.labels.size();

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
                for (GeometryInterface rectangle : collection) {
                    addEdgeAndInverse(point.getID(), ((LabelInterface) rectangle).getID() + noPoints, noPoints);
                }
            }

            // label NE of point intersects with NW lables
            collection = record.collection.query2D(new Rectangle(x, y - height, x + 2 * width, y + height));
            if (collection != null) {
                for (GeometryInterface rectangle : collection) {
                    addEdgeAndInverse(point.getID(), ((LabelInterface) rectangle).getID(), noPoints);
                }
            }

            // label NW of point intersects with NE lables
            collection = record.collection.query2D(new Rectangle(x - 2 * width, y - height, x, y + height));
            if (collection != null) {
                for (GeometryInterface rectangle : collection) {
                    addEdgeAndInverse(point.getID() + noPoints, ((LabelInterface) rectangle).getID() + noPoints, noPoints);
                }
            }

            // label NW of point intersects with NW lables
            collection = record.collection.query2D(new Rectangle(x - width, y - height, x + width, y + height));
            if (collection != null) {
                for (GeometryInterface rectangle : collection) {
                    addEdgeAndInverse(point.getID() + noPoints, ((LabelInterface) rectangle).getID(), noPoints);
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
