package algorithms;

/*
 * @author = Jeroen Schols
 */

import Parser.DataRecord;
import interfaces.AbstractAlgorithmInterface;
import interfaces.models.LabelInterface;
import models.Point;

import java.util.List;

public class GreedySliderAlgorithm implements AbstractAlgorithmInterface {

    private DataRecord record;

    @Override
    public void solve(DataRecord record) {
        this.record = record;
        Point[] sortedPoints = sortPoints(record.labels);
    }

    /**
     * sorts a set of labels into an array such that a point p is before point q if {@code p.x < q.x || (p.x == q.x && p.y > q.y)}
     *
     * @param points {A List of labels}
     * @return An array of all labels sorted on increasing x-coordinates and on decreasing y-coordinates
     */
    private Point[] sortPoints(List<? extends LabelInterface> labels) {
        for (LabelInterface label : labels) {

        }
        throw new UnsupportedOperationException("GreedySliderAlgorithm.sortPoints() not implemented yet");
    }

    /**
     * returns whether it is possible to label each point with a label of given width
     *
     * @param points {A list of labels}
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
     * @param points {A list of labels}
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
