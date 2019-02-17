package algorithms;

/*
 * @author = Jeroen Schols
 */

import Parser.DataRecord;
import interfaces.AbstractAlgorithmInterface;
import interfaces.models.LabelInterface;
import models.Point;

import java.util.*;

public class GreedySliderAlgorithm implements AbstractAlgorithmInterface {

    /*
     * Comparator that sorts Labels on their POI, where a Label l1 appears before l2 when for
     * their respective POI's p1 and p2 it holds that:
     * {@code p1.x < p2.x || (p1.x == p2.x && p1.y > p2.y)}
     */
    private static Comparator<LabelInterface> comparator = (Comparator<LabelInterface>) (l1, l2) -> {
        int x1 = l1.getPOI().getXMin().intValue();
        int x2 = l2.getPOI().getXMin().intValue();
        double y1 = l1.getPOI().getYMin();
        double y2 = l2.getPOI().getYMin();
        if (x1 < x2) return -1;
        else if (x1 > x2) return 1;
        else if (y1 < y2) return 1;
        else if (y1 > y2) return -1;
        else throw new IllegalArgumentException("GreedySliderAlgorithm.comparator.compare() compares 2 labels with an overlapping POI");
    };

    @Override
    public void solve(DataRecord record) {
        record.labels.sort(comparator);

        //@TODO write a better binary search algorithm, currently is simple structure as placeholder
        double low = 0;
        double high = Double.MAX_VALUE;
        while (high - low > 0.5) {
            double mid = low + high / 2;
            if (isSolvable(record, mid)) {
                low = mid;
            } else {
                high = mid;
            }
        }
    }

    private boolean isSolvable(DataRecord record, double width) {
        throw new UnsupportedOperationException("GreedySliderAlgorithm.isSolvable() not implemented yet");
    }


    private boolean solve(Point[] points, float width) {
        throw new UnsupportedOperationException("GreedySliderAlgorithm.solve() not implemented yet");
    }

    /**
     * makes a greedy choice for assigning given point a label of given width by placing it with a smalles possible slider value
     *
     * @param point {@link Point}
     * @param width float denoting the width given to this label
     * @modifies record.collection
     * @return whether it is possible to add a label of given width to the collection
     */
    private boolean addLabel(Point point, float width) {
        throw new UnsupportedOperationException("GreedySliderAlgorithm.setLabel() not implemented yet");
    }
}
