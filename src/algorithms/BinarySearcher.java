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

        // TODO: needs to be grabbed from input
        float alpha = 1.4f;

        // TODO: this value can be optimized base on the number of points
        int high = (int)(10000 * Math.sqrt(alpha));
        int low = 0;



        // make sure that our upper bound is correct. This may not be required if we have a good estimation
        while (isSolvable(nodes, high)) {
            low = high;
            high = (int) (high * 1.5);
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

        int m_low = (int) (low * alpha * 2);
        int m_high = (int) Math.ceil(high * alpha * 2);

        while (m_low < m_high - 1) {
            int mid = (m_high + m_low) / 2;
            if (isSolvable(nodes,  mid / (alpha * 2))) {
                m_low = mid;
            } else {
                m_high = mid;
            }
        }


        float newHeight = m_low / (alpha * 2);
        if (newHeight > low && newHeight < high) {
            if (isSolvable(nodes, newHeight)) {
                height = Math.max(newHeight, height);
            }
        }
        getSolution(nodes, height);
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
     * Place all labels with the given height (this method is not robust to reduce computation time)
     *
     * @modifies nodes
     * @param nodes {@link AbstractAlgorithmInterface}
     * @param height required height for rectangles
     * @pre isSolvable(nodes, height)
     */
    abstract void getSolution(AbstractCollectionInterface<T> nodes, float height);

}
