package algorithms;

import interfaces.AbstractAlgorithmInterface;
import interfaces.AbstractCollectionInterface;
import interfaces.models.RectangleInterface;

public abstract class BinarySearcher<T extends RectangleInterface> implements AbstractAlgorithmInterface<T> {
    /**
     *
     * uses binary search to find the optimal height for the rectangles
     *
     * @param nodes {@link AbstractAlgorithmInterface}
     */
    @Override
    public void solve(AbstractCollectionInterface<T> nodes) {
        // TODO: this value can be optimized base on the number of points
        int high = 10000;
        int low = 0;


        // make sure that our upper bound is correct. This may not be required if we have a good estimation
        while (isSolvable(nodes, high)) {
            low = high;
            high *= 5;
        }


        while (low < high - 1) {
            int mid = (high + low) / 2;
            if (isSolvable(nodes, mid)) {
                low = mid;
            } else {
                high = mid;
            }
        }

        float height = low;




        getSolution(nodes, height);
        System.out.println(low + " " + high);
    }

    /**
     * returns if labels can be placed for a given height
     *
     * @modifies none
     * @param nodes {@link AbstractAlgorithmInterface}
     * @param height required height for rectangles
     */
    abstract boolean isSolvable(AbstractCollectionInterface<T> nodes, float height);

    /**
     * Place all labels with the given height
     *
     * @modifies nodes
     * @param nodes {@link AbstractAlgorithmInterface}
     * @param height required height for rectangles
     */
    abstract void getSolution(AbstractCollectionInterface<T> nodes, float height);

}
