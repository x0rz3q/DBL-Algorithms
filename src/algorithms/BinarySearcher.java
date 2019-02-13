package algorithms;

import interfaces.AbstractAlgorithmInterface;
import models.DataRecord;

public abstract class BinarySearcher implements AbstractAlgorithmInterface {
    /**
     *
     * uses binary search to find the optimal height for the rectangles
     *
     * @param record {@link DataRecord}
     */
    @Override
    public void solve(DataRecord record) {

        // TODO: change back
        float alpha = record.aspectRatio;

        int high = (int)(10000 * Math.sqrt(alpha / record.points.size()));
        int low = 0;


        // make sure that our upper bound is correct. This may not be required if we have a good estimation
        while (isSolvable(record, high)) {
            low = high;
            high = (int) (high * 1.5);
        }


        while (low < high - 1) {
            int mid = (high + low) / 2;
            if (isSolvable(record, mid)) {
                low = mid;
            } else {
                high = mid;
            }
        }

        float height = low;
        int i_low, i_high;

        if (isSolvable(record, low + 0.5f)) {
            // System.out.println("so far: " + (low + 0.5) + " " + high);
            i_low =  (int) Math.ceil((low + 0.5) / alpha);
            i_high =  (int) Math.ceil((high) / alpha);
            height = low + 0.5f;
        } else {
            // System.out.println("so far: " + (low) + " " + (low + 0.5));
            i_low =  (int) Math.ceil((low) / alpha);
            i_high =  (int) Math.ceil((low + 0.5) / alpha);
        }

        while (i_low < i_high - 1) {
            int mid = (i_high + i_low) / 2;
            if (isSolvable(record, mid * alpha)) {
                i_low = mid;
            } else {
                i_high = mid;
            }
        }
        if(i_low * alpha < high && i_low * alpha > low) {
            if (isSolvable(record, i_low * alpha)) {
                height = Math.max(low, i_low * alpha);
            }
        }
        getSolution(record, height);
    }

    /**
     * returns if labels can be placed for a given height
     *
     * @modifies none
     * @param record {@link DataRecord}
     * @param height required height for rectangles
     */
    abstract boolean isSolvable(DataRecord record, float height);

    /**
     * Place all labels with the given height (this method is not robust to reduce computation time)
     *
     * @modifies nodes
     * @param record {@link DataRecord}
     * @param height required height for rectangles
     * @pre isSolvable(nodes, height)
     */
    abstract void getSolution(DataRecord record, float height);

}
