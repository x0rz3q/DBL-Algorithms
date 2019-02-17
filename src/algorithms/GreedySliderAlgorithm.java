package algorithms;

/*
 * @author = Jeroen Schols
 */

import Parser.DataRecord;
import interfaces.AbstractAlgorithmInterface;
import interfaces.models.LabelInterface;
import models.Point;
import models.SliderLabel;

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

        List<SliderLabel> sortedLabels = new ArrayList<>();
        for (LabelInterface label : record.labels) {
            if (label.getClass() != SliderLabel.class) throw new IllegalArgumentException("GreedySliderAlgorithm.solve() input record does not provide SliderLabels");
            else sortedLabels.add((SliderLabel) label);
        }
        sortedLabels.sort(comparator);

        //@TODO write a better binary search algorithm, currently is simple structure as placeholder
        double low = 0;
        double high = Double.MAX_VALUE;
        while (true) {
            double mid = low + high / 2;
            if (solve(record, sortedLabels, mid)) {
                low = mid;
            } else {
                high = mid;
            }

            if (high - low > 0.5) {
                if (low != mid) solve(record, sortedLabels, low);
                break;
            }
        }
    }

    /**
     * Finds a solution to the DataRecord
     *
     * @param record {@link DataRecord}
     * @param sortedLabels a List containing all labels sorted by this.comparator
     * @param width double denoting the width assigned to each label
     * @pre sortedLabels is sorted by using this.comparator
     * @modifies record
     * @return whether there exists a solution
     * @post if there is a solution record.collection contains it, else record.collection holds an invalid solution
     */
    private boolean solve(DataRecord record, List<SliderLabel> sortedLabels, double width) {
        for (SliderLabel label : sortedLabels) {
            if (!setLabel(record, label, width)) return false;
        }
        return true;
    }

    /**
     * Makes a greedy choice for setting label to given width with a minimum slidervalue
     *
     * @param record
     * @param label
     * @param width
     * @return
     */
    private boolean setLabel(DataRecord record, SliderLabel label, double width) {
        throw new UnsupportedOperationException("GreedySliderAlgorithm.setLabel() not implemented yet");
    }
}
