package algorithms;

import Parser.DataRecord;
import interfaces.models.LabelInterface;
import models.Anchor;
import models.FourPositionLabel;
import models.PositionLabel;
import models.Square;
import models.*;

import java.util.ArrayList;

import static models.DirectionEnum.*;

public class FourPositionWagnerWolff extends AbstractFourPosition {

    // phase 2 queue
    private ArrayList<FourPositionPoint> pointsQueue = new ArrayList<>();

    // conflict graph
    private ArrayList<FourPositionLabel> conflicts = new ArrayList<>();

    private DataRecord labels;

    @Override
    double[] findConflictSizes(DataRecord record) {
        return new double[0];
    }

    @Override
    void preprocessing(DataRecord record, Double sigma) {
        labels = new DataRecord();

        for (LabelInterface p : record.points) {
            double pX = p.getXMax();
            double pY = p.getYMax();

            FourPositionPoint point = new FourPositionPoint(new Anchor(pX, pY));

            // adding new labels (All id's are 0 for now)
            // add NE square
            if (record.collection.query2D(new Square(new Anchor(pX, pY), sigma)).size() == 0) {
                FourPositionLabel label = new FourPositionLabel(pX, pY, sigma, NE, 0, point);
                labels.collection.insert(label);
                conflicts.add(label);
            }
            // add NW
            if (record.collection.query2D(new Square(new Anchor(pX-sigma, pY), sigma)).size() == 0) {
                conflicts.add(new FourPositionLabel(pX, pY, sigma, NW, 0, point));
            }
            // add SE
            if (record.collection.query2D(new Square(new Anchor(pX, pY-sigma), sigma)).size() == 0) {
                conflicts.add(new FourPositionLabel(pX, pY, sigma, SE, 0, point));
            }
            // add SW
            if (record.collection.query2D(new Square(new Anchor(pX-sigma, pY-sigma), sigma)).size() == 0) {
                conflicts.add(new FourPositionLabel(pX, pY, sigma, SW, 0, point));
            }
        }
    }

    @Override
    FourPositionLabel[] createConflictGraph() {
        return new FourPositionLabel[0];
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
