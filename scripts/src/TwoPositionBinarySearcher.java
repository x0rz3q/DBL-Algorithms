













import java.util.*;

public class TwoPositionBinarySearcher extends BinarySearcher {
    // edges of implication graph and its inverse
    private ArrayList[] adj;
    private ArrayList[] adjInv;

    private ImplicationGraphSolver solver;

    public TwoPositionBinarySearcher() {
        solver = new ImplicationGraphSolver();
    }

    @Override
    double[] getSolutionSpace(DataRecord record) {
        TwoPositionDistance distanceFunction = new TwoPositionDistance();
        distanceFunction.setAspectRatio(record.aspectRatio);
        int k = 8; // number of nearest neighbours to search for
        if (record.labels.size() < 9) k = record.labels.size() - 1;
        Set<Double> conflictSizes = new HashSet<>();
        for (LabelInterface label : record.labels) {
            PointInterface point = label.getPOI();
            Set<GeometryInterface> nearestNeighbours = ((KDTree) record.collection).nearestNeighbours(distanceFunction, k, point);

            for (GeometryInterface target : nearestNeighbours) {
                Pair<Double, Boolean> conflictSize = distanceFunction.calculateAndIsWidth(point, ((LabelInterface) target).getPOI());
                conflictSizes.add(conflictSize.getKey());
                if (conflictSize.getValue()) {
                    conflictSizes.add(conflictSize.getKey() / 2.0);
                }
            }
        }

        double[] conflicts = new double[conflictSizes.size()];
        int i = 0;
        for (double size : conflictSizes) {
            conflicts[i++] = size;
        }

        Arrays.sort(conflicts);

        return conflicts;
    }

    @Override
    boolean isSolvable(DataRecord record, double height) {
        if(!preprocessingCheck(record, height)) return false;
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

    private Boolean preprocessingCheck(DataRecord record, Double height) {
        double width = height*record.aspectRatio;
        for (LabelInterface label: record.labels) {
            Rectangle left = new Rectangle(label.getXMin() - width, label.getYMin(), label.getXMin(), label.getYMin() + height);
            Rectangle right = new Rectangle(label.getXMin(), label.getYMin(), label.getXMin() + width, label.getYMin() + height);
            if (!record.collection.query2D(left).isEmpty() && !record.collection.query2D(right).isEmpty()) return false;
        }
        return true;
    }
}
