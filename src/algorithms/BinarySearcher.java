package algorithms;

import Parser.DataRecord;
import interfaces.AbstractAlgorithmInterface;

public abstract class BinarySearcher implements AbstractAlgorithmInterface {
    /**
     *
     * uses binary search to find the optimal height for the rectangles
     *
     * @param record {@link DataRecord}
     */
    @Override
    public void solve(DataRecord record) {
        double[] solutionSpace = getSolutionSpace(record);
        int low = 0;
        int high = solutionSpace.length;

        while (low < high - 1) {
            int mid = (high + low) / 2;
            if (isSolvable(record, solutionSpace[mid])) {
                low = mid;
            } else {
                high = mid;
            }
        }
        getSolution(record, solutionSpace[low]);
    }


    /**
     * @param record
     * @return double[] solution space
     * @post \forall(i ; \result.has(i); \result[i] is possible solution for record)
     * @post \forall(i; 0 <= i < \result.length - 1; \result[i] <= \result[i + 1]
     */
    abstract double[] getSolutionSpace(DataRecord record);

    /**
     * returns if labels can be placed for a given height
     *
     * @param record {@link DataRecord}
     * @param height required height for rectangles
     * @modifies none
     */
    abstract boolean isSolvable(DataRecord record, double height);

    /**
     * Place all labels with the given height (this method is not robust to reduce computation time)
     *
     * @param record {@link DataRecord}
     * @param height required height for rectangles
     * @modifies nodes
     * @pre isSolvable(nodes, height)
     */
    abstract void getSolution(DataRecord record, double height);


}
