package algorithms;

import Parser.DataRecord;
import interfaces.models.LabelInterface;
import models.Anchor;
import models.PositionLabel;
import models.Square;

import java.util.ArrayList;

import static models.DirectionEnum.*;

public class FourPositionWagnerWolff extends AbstractFourPosition {

    private ArrayList<PositionLabel> candidates = new ArrayList<>();

    @Override
    double[] findConflictSizes(DataRecord record) {
        return new double[0];
    }

    @Override
    void preprocessing(DataRecord record, Double sigma) {

        for (LabelInterface p : record.points) {
            double pX = p.getXMax();
            double pY = p.getYMax();

            // adding new labels (All id's are 0 for now)
            // add NE square
            if (record.collection.query2D(new Square(new Anchor(pX, pY), sigma)).size() == 0) {
                candidates.add(new PositionLabel(pX, pY, sigma, NE, 0));
            }
            // add NW
            if (record.collection.query2D(new Square(new Anchor(pX-sigma, pY), sigma)).size() == 0) {
                candidates.add(new PositionLabel(pX, pY, sigma, NW, 0));
            }
            // add SE
            if (record.collection.query2D(new Square(new Anchor(pX, pY-sigma), sigma)).size() == 0) {
                candidates.add(new PositionLabel(pX, pY, sigma, SE, 0));
            }
            // add SW
            if (record.collection.query2D(new Square(new Anchor(pX-sigma, pY-sigma), sigma)).size() == 0) {
                candidates.add(new PositionLabel(pX, pY, sigma, SW, 0));
            }
        }
    }

    @Override
    boolean eliminateImpossibleCandidates() {
        return false;
    }

    @Override
    void doTwoSat() {

    }

    @Override
    public void solve(DataRecord record) {
        double[] conflictSizes = findConflictSizes(record);

        // binary search over conflictSizes
        // for conflict size do:

            // initialize labels for conflictSize
            // preprocessing(record, conflictSizes[i]);
            // boolean solvable = eliminatImpossibleCandidates(record);
            // if (!solvable) continue;
            // doTwoSat(record)
    }
}
