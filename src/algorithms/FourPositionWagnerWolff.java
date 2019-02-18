package algorithms;

import Parser.DataRecord;

public class FourPositionWagnerWolff extends AbstractFourPosition {

    @Override
    double[] findConflictSizes(DataRecord record) {
        return new double[0];
    }

    @Override
    void preprocessing(DataRecord record, Double sigma) {

    }

    @Override
    boolean eliminateImpossibleCandidates(DataRecord record) {
        return false;
    }

    @Override
    void doTwoSat(DataRecord record) {

    }

    @Override
    public void solve(DataRecord record) {
        super.solve(record);
    }

    @Override
    boolean isSolvable(DataRecord record, double height) {
        return false;
    }

    @Override
    void getSolution(DataRecord record, double height) {

    }
}
