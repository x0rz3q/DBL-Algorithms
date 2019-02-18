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

    /**
     * Eliminates all impossible candidates from the record for a given label size.
     * This function checks for the following four requirements: 1) if all candidates of
     * a point p have been eliminated, we return false since there is no solution; 2) If point p
     * has candidates free of intersection with other candidates, choose an arbitrary candidate
     * and eliminate all other candidates; 3) If p has only one candidate left, do the same on all other
     * squares it intersects (try to delete them); 4) If p has a candidate p_i which overlaps the last two
     * candidates of another site, then update and eliminate p_i.
     *
     * @param record the given DataRecord
     * @modifies record
     * @post record does not contain candidates which can't be part of the solution
     * @return wether all points still have candidates left (if not there is no solution for this size)
     */
    abstract boolean eliminateImpossibleCandidates(DataRecord record);

    /**
     * Try to solve if given record does not have an obvious solution.
     * For those points which still have two or more candidates left, choose exactly two (heuristic),
     * and check, whether this remaining problem is solvable with 2-SAT (like 2 position)
     *
     * @param record
     * @modifies record
     * @post record is solved
     */
    abstract void doTwoSat(DataRecord record);
}
