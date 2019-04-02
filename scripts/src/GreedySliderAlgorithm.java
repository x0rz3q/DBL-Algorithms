

/*
 * @author = Jeroen Schols
 */









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

        // casts labels to sliderlabels in an new sorted List
        List<FieldExtendedSliderLabel> sortedLabels = new ArrayList<>();
        for (LabelInterface label : record.labels) {
            if (label.getClass() != FieldExtendedSliderLabel.class)
                throw new IllegalArgumentException("GreedySliderAlgorithm.solve() input record does not provide SliderLabels");
            else sortedLabels.add((FieldExtendedSliderLabel) label);
        }
        sortedLabels.sort(comparator);

        double epsilon = 0;
        double low = 0;
        double high = Double.MAX_VALUE;
        while (true) {
            double mid = (low + high) / 2;
            if (solve(record, sortedLabels, mid)) {
                low = mid;
            } else {
                high = mid;
            }

            if (mid == epsilon) {
                for (FieldExtendedSliderLabel label : sortedLabels) {
                    setLabel(record, label, low);
                }
                record.height = low / record.aspectRatio;
                return;
            } else {
                epsilon = mid;
            }
        }
    }

    /**
     * Finds a solution to the DataRecord and resets the labels afterwards
     *
     * @param record       {@link DataRecord}
     * @param sortedLabels a List containing all labels sorted by this.comparator
     * @param width        double denoting the width assigned to each label
     * @return whether there exists a solution
     * @pre sortedLabels is sorted by using this.comparator
     * @post if there is a solution record.collection contains it, else record.collection holds an invalid solution
     */
    private boolean solve(DataRecord record, List<FieldExtendedSliderLabel> sortedLabels, double width) {
        for (FieldExtendedSliderLabel label : sortedLabels) {
            if (!setLabel(record, label, width)) {
                for (SliderLabel l : sortedLabels) {
                    record.collection.remove(l);
                    l.setHeight(0);
                    record.collection.insert(l);
                    if (l == label) break;
                }
                return false;
            }
        }
        for (FieldExtendedSliderLabel l : sortedLabels) {
            record.collection.remove(l);
            l.setHeight(0);
            record.collection.insert(l);
        }
        return true;
    }

    /**
     * Makes a greedy choice for setting label to given width with a minimum slidervalue
     *
     * @param record {@link DataRecord}
     * @param label  {@link SliderLabel}
     * @param width  double denoting the width assigned to label
     * @return whether it is possible to have label be placeable in collection with given width
     */
    private boolean setLabel(DataRecord record, FieldExtendedSliderLabel label, double width) {
        Rectangle queryArea = new Rectangle(
                label.getPOI().getX() - width,
                label.getPOI().getY(),
                label.getPOI().getX(),
                label.getPOI().getY() + width / record.aspectRatio);
        Collection<GeometryInterface> queryResult = record.collection.query2D(queryArea);

        double xMax = label.getPOI().getX() - width;
        FieldExtendedSliderLabel maxLabel = label;
        for (GeometryInterface entry : queryResult) {
            if (entry != label && entry.getXMax() > xMax) {
                maxLabel = (FieldExtendedSliderLabel) entry;
                xMax = entry.getXMax();
            }
        }

        if (xMax > label.getPOI().getX()) return false;

        // when the label should be placed completely to the right, check whether this is possible
        if (xMax == label.getPOI().getX()) {
            queryArea = new Rectangle(
                    label.getPOI().getX(),
                    label.getPOI().getY(),
                    label.getPOI().getX() + width,
                    label.getPOI().getY() + width / record.aspectRatio
            );
            queryResult = record.collection.query2D(queryArea);
            for (GeometryInterface entry : queryResult) {
                if (entry != label) {
                    if (((FieldExtendedSliderLabel) entry).getShift() >= 1) return false;
                }
            }
        }

        record.collection.remove(label);
        if (xMax == label.getPOI().getX() - width) {
            label.setFieldExtended((int) label.getPOI().getX(), 0, width);
        } else {
            label.setFieldExtended(maxLabel.getSequenceStartX(), maxLabel.getSequenceIndex() + 1, width);
        }
        record.collection.insert(label);

        return true;
    }
}
