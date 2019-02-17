package algorithms;

import Parser.DataRecord;

public abstract class AbstractFourPosition extends BinarySearcher {

    /**
     * Calculates all conflict sizes for given DataRecord. It is sufficient for the
     * 4-position algorithm to only consider the heights corresponding to the important conflict
     * sizes between the points in the data record. A conflict of a site p and one of its eight
     * closest neighbours in one of the four quadrants relative to p is called important.
     *
     * @param record the given DataRecord
     * @return conflictSizes[] = (\forall double i; conflictSizes[].contains(i);
     *                      i is a conflictSize for record)
     */
    abstract double[] findConflictSizes(DataRecord record);

    /**
     * Eliminates all labels in given record which would contain a point (not of the label)
     * if the labels had a given size.
     *
     * @param record the given DataRecord
     * @param sigma the size of the labels
     * @modifies record
     * @post (\forall p; p \in record; p does not have any labels p_i assigned
     *              where that p_i of size sigma contains another point in the record)
     */
    abstract void preprocessing(DataRecord record, Double sigma);

    abstract boolean eliminateImpossibleCandidates(DataRecord record);

    abstract void doTwoSat(DataRecord record);
}
