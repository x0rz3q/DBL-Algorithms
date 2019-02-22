package algorithms;

/*
 * @author = Jeroen Schols
 */

import Parser.DataRecord;
import interfaces.AbstractAlgorithmInterface;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import models.Point;
import models.Rectangle;
import models.SliderLabel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

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
        else
            throw new IllegalArgumentException("GreedySliderAlgorithm.comparator.compare() compares 2 labels with an overlapping POI");
    };

    @Override
    public void solve(DataRecord record) {

        List<SliderLabel> sortedLabels = new ArrayList<>();
        for (LabelInterface label : record.labels) {
            if (label.getClass() != SliderLabel.class)
                throw new IllegalArgumentException("GreedySliderAlgorithm.solve() input record does not provide SliderLabels");
            else sortedLabels.add((SliderLabel) label);
        }
        sortedLabels.sort(comparator);

        //@TODO write a better binary search algorithm, currently is simple structure as placeholder
        double epsilon = 0.0001;
        double low = 0;
        double high = Integer.MAX_VALUE;
        while (true) {
            double mid = (low + high) / 2;
            System.out.println("\nmid = " + mid);
            if (solve(record, sortedLabels, mid)) {
                if (!main.Interpreter.isValid(record)) {
                    System.err.println("invalid datarecord found valid");
                    return;
                }
                low = mid;
            } else {
                high = mid;
            }

            if (high - low < epsilon) {
                if (low != mid) solve(record, sortedLabels, low);
                record.height = low;
                break;
            }
        }
    }

    /**
     * Finds a solution to the DataRecord
     *
     * @param record       {@link DataRecord}
     * @param sortedLabels a List containing all labels sorted by this.comparator
     * @param height        double denoting the height assigned to each label
     * @return whether there exists a solution
     * @pre sortedLabels is sorted by using this.comparator
     * @modifies record
     * @post if there is a solution record.collection contains it, else record.collection holds an invalid solution
     */
    private boolean solve(DataRecord record, List<SliderLabel> sortedLabels, double height) {
        for (SliderLabel label : sortedLabels) {
            if (!setLabel(record, label, height)) {
                for (SliderLabel l : sortedLabels) {
                    l.setWidth(0);
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Makes a greedy choice for setting label to given width with a minimum slidervalue
     *
     * @param record {@link DataRecord}
     * @param label  {@link SliderLabel}
     * @param height  double denoting the height assigned to label
     * @return whether it is possible to have label be placeable in collection with given width
     */
    private boolean setLabel(DataRecord record, SliderLabel label, double height) {
        Rectangle queryArea = new Rectangle(new Point(label.getPOI().getX()-height, label.getPOI().getY()), new Point(label.getPOI().getX(), label.getPOI().getY() + height));
        Collection<GeometryInterface> queryResult = record.collection.query2D(queryArea);

        double xMax = label.getPOI().getX() - height;
        for (GeometryInterface entry : queryResult) {
            if (entry != label) xMax = Math.max(xMax, entry.getXMax());
        }

        if (xMax > label.getPOI().getX()) return false;

        double shift = (xMax - label.getPOI().getX()) / height + 1;
        label.setShift(shift);
        label.setHeight(height);


        //TODO: Jeroen please check how to adjust this to the new interface
//        label.setEdgeLength(width, Math.min(Math.max(0, shift), 1));

        return true;
    }
}
