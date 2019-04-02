/*
 * @author = Jeroen Schols
 */

package algorithms;

import Parser.DataRecord;
import interfaces.AbstractAlgorithmInterface;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import models.Rectangle;
import models.SliderLabel;
import java.util.*;

public class GreedySliderAlgorithm implements AbstractAlgorithmInterface {

    /*
     * Comparator that sorts Labels on their POI, where a Label l1 appears before l2 when for
     * their respective POI's p1 and p2 it holds that:
     * {@code p1.x < p2.x || (p1.x == p2.x && p1.y > p2.y)}
     */
    private static Comparator<SliderLabel> comparator = (Comparator<SliderLabel>) (l1, l2) -> {
        double x1 = l1.getPOI().getX();
        double x2 = l2.getPOI().getX();
        double y1 = l1.getPOI().getY();
        double y2 = l2.getPOI().getY();
        if (x1 < x2) return -1;
        else if (x1 > x2) return 1;
        else if (y1 < y2) return 1;
        else if (y1 > y2) return -1;
        else throw new IllegalArgumentException("GreedySliderAlgorithm.comparator.compare() compares 2 labels with an overlapping POI");
    };


    @Override
    public void solve(DataRecord record) {

        // casts labels to sliderlabels in a new sorted array
        SliderLabel[] sortedLabels = new SliderLabel[record.labels.size()];
        record.labels.toArray(sortedLabels);
        Arrays.sort(sortedLabels, comparator);

        // binary search over range [low, high]
        double low = 0;
        double high = Double.MAX_VALUE;
        double mid = (low + high) / 2;
        double prev; // value from previous binary search iteration
        do {
            prev = mid;
            if (solve(record, sortedLabels, mid)) {
                low = mid;
            } else {
                high = mid;
            }
            mid = (low + high) / 2;
        } while (mid != prev); // when difference between low-high becomes neglible, mid == prev

        // set the labels to the found optimal width
        for (SliderLabel label : sortedLabels) setLabel(record, label, low);
        record.height = low / record.aspectRatio;
    }

    /**
     * Finds a solution of assigning labels with given width to the DataRecord problem and resets the labels afterwards
     *
     * @param record       {@link DataRecord}
     * @param sortedLabels an array containing all labels sorted by this.comparator
     * @param width        double denoting the width assigned to each label
     * @return whether there exists a solution assigning labels of given width
     * @pre sortedLabels is sorted by using this.comparator
     * @post all points have a label of width and height 0
     */
    private boolean solve(DataRecord record, SliderLabel[] sortedLabels, double width) {
        int i = 0;

        // tries to set the labels in sorted order
        // postcondition all labels that are set have an index strictly smaller than i
        while (i < sortedLabels.length && setLabel(record, sortedLabels[i], width)) i++;

        // removes all labels that were set
        for (int j = 0; j < i; j++) {
            record.collection.remove(sortedLabels[j]);
            sortedLabels[j].reset();
        }

        // returns whether are labels were set
        return i == sortedLabels.length;
    }

    /**
     * Makes a greedy choice for setting label to given width with a minimum slidervalue
     *
     * @param record {@link DataRecord}
     * @param label  {@link SliderLabel} the label to be set
     * @param w      double denoting the width assigned to label
     * @return whether it is possible to have label be placeable in collection with given width
     * @pre all previous set labels are labels with a smaller index when sorted with this.comparator
     * @post
     */
    private boolean setLabel(DataRecord record, SliderLabel label, double w) {

        int x = (int) label.getPOI().getX();
        int y = (int) label.getPOI().getY();
        double h = w / record.aspectRatio;

        // queries the area covered when this label is placed completely to the left
        Collection<GeometryInterface> queryResult = record.collection.query2D(new Rectangle(x - w, y, x,y + h));

        // sets maxLabel to be the label most to the right of the queried area
        SliderLabel maxLabel = null;
        double xMax = x - w;
        for (GeometryInterface entry : queryResult) {
            if (entry.getXMax() > xMax) {
                maxLabel = (SliderLabel) entry;
                xMax = entry.getXMax();
            }
        }

        // when maxLabel covers the POI, the label can not be placed
        if (xMax > x) return false;

        // when the label should be placed completely to the right, check whether this is possible
        if (xMax == x && !record.collection.query2D(new Rectangle(x,y,x+w,y+h)).isEmpty()) return false;

        // else insert the label aligned to the right of xMax
        if (maxLabel == null) {
            label.setLabel(x, 0, w);
        } else {
            label.setLabel(maxLabel.getSeqStartX(), maxLabel.getSeqIndex() + 1, w);
        }
        record.collection.insert(label);

        return true;
    }
}
