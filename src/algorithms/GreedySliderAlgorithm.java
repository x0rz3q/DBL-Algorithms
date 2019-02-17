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

    private DataRecord record;

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
        this.record = record;
        record.labels.sort(comparator);
    }

    /**
     * returns whether it is possible to label each point with a label of given width
     *
     * @param points {An array of points}
     * @param width float denoting the width given to each label
     * @modifies record.collection
     * @return whether there is a valid possible labeling
     */
    private boolean isSolvable(Point[] points, float width) {
        throw new UnsupportedOperationException("GreedySliderAlgorithm.isSolvable() not implemented yet");
    }

    /**
     * labels each point with a label of given width
     *
     * @param points {An array of points}
     * @param width float denoting the width given to each label
     * @modifies record.collection
     * @return whether there is a valid possible labeling
     */
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
