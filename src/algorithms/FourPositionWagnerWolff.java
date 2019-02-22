package algorithms;

import Parser.DataRecord;
import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;
import models.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import static models.DirectionEnum.*;

public class FourPositionWagnerWolff extends AbstractFourPosition {

    // phase 2 queue
    private ArrayDeque<FourPositionPoint> pointsQueue = new ArrayDeque<>();

    // conflict graph
    private ArrayList<FourPositionLabel> labelsWithConflicts = new ArrayList<>();

    // selected candidates
    private ArrayList<FourPositionLabel> selectedLabels = new ArrayList<>();

    // DataRecord containing all labels of the current sigma
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

            FourPositionPoint point = new FourPositionPoint(p.getAnchor());
            pointsQueue.add(point);

            // adding new labels (All id's are 0 for now)
            // add NE square
            if (record.collection.query2D(new Square(new Anchor(pX, pY), sigma)).size() == 0) {
                FourPositionLabel northEastLabel = new FourPositionLabel(pX, pY, sigma, NE, 0, point);
                Collection<SquareInterface> conflictingLabels = labels.collection.query2D(new BoundingBox(pX, pY, pX+sigma, pY+sigma));
                preprocessingLabel(northEastLabel, conflictingLabels);
            }
            // add NW
            if (record.collection.query2D(new Square(new Anchor(pX-sigma, pY), sigma)).size() == 0) {
                FourPositionLabel northWestLabel = new FourPositionLabel(pX, pY, sigma, NW, 0, point);
                Collection<SquareInterface> conflictingLabels = labels.collection.query2D(new BoundingBox(pX-sigma, pY, pX, pY+sigma));
                preprocessingLabel(northWestLabel, conflictingLabels);
            }
            // add SE
            if (record.collection.query2D(new Square(new Anchor(pX, pY-sigma), sigma)).size() == 0) {
                FourPositionLabel southEastlabel = new FourPositionLabel(pX, pY, sigma, SE, 0, point);
                Collection<SquareInterface> conflictingLabels = labels.collection.query2D(new BoundingBox(pX, pY-sigma, pX+sigma, pY));
                preprocessingLabel(southEastlabel, conflictingLabels);
            }
            // add SW
            if (record.collection.query2D(new Square(new Anchor(pX-sigma, pY-sigma), sigma)).size() == 0) {
                labelsWithConflicts.add(new FourPositionLabel(pX, pY, sigma, SW, 0, point));
                Collection<SquareInterface> conflictingLabels = labels.collection.query2D(new BoundingBox(pX-sigma, pY-sigma, pX, pY));
            }
        }
    }

    /**
     * Subtask from the preprocessing function.
     *
     * @param label
     * @param conflictingLabels
     */
    private void preprocessingLabel(FourPositionLabel label, Collection<SquareInterface> conflictingLabels) {
        // TODO: Discuss wether labelsWithConflicts should only contain labels with conflicts or it should consits all labels at start

        // If new label intersects with existing labels, add it to labelsWithConflicts
        if (conflictingLabels.size() > 0) labelsWithConflicts.add(label);

        // for all squares that intersect the new label:
        //      add square to conflict list of the new label
        //      add new label to the conflicts list of the square
        //      add square to labelsWithConflicts lists if its not in there yet
        for (SquareInterface square : conflictingLabels) {
            label.addConflict((FourPositionLabel) square);
            ((FourPositionLabel) square).addConflict(label);
            if (!labelsWithConflicts.contains(square)) { labelsWithConflicts.add((FourPositionLabel) square); }
        }

        // add new label to DataRecord for future queing
        labels.collection.insert(label);
    }

    @Override
    FourPositionLabel[] createConflictGraph() {
        return new FourPositionLabel[0];
    }


    @Override
    boolean eliminateImpossibleCandidates() {
        while (!pointsQueue.isEmpty()) {
            FourPositionPoint point = pointsQueue.pollFirst(); // also removes element from queue
            if (noCandidates(point)) {
                return false;
            } else if (hasCandidateWithoutIntersections(point)) {

            } else if (oneCandidates(point)) {

            } else if (candidateIntersectsAllRemaining(point)) {

            }
        }
        return false;
    }

    private boolean noCandidates(FourPositionPoint point) {
        return false;
    }


    /**
     * Checks whether the point has a candidate which is free of intersections with other labels, and selects such a
     * label if found.
     * @param point
     * @return whether a candidate was chosen
     */
    private boolean hasCandidateWithoutIntersections(FourPositionPoint point) {
        FourPositionLabel selected = null;

        // check individual candidates
        for (FourPositionLabel candidate : point.getCandidates()) {
            if (candidate.getConflicts().isEmpty()) {
                selected = candidate;
                continue;
            }
        }

        if (selected == null) {
            return false;
        }

        // select conflict free candidate
        selectedLabels.add(selected);

        // remove other labels
        for (FourPositionLabel candidate : point.getCandidates()) {
            if (candidate != selected) {
                for (FourPositionLabel conflict : candidate.getConflicts()) {
                    conflict.removeConflict(candidate);
                }
                labelsWithConflicts.remove(candidate);
            }
        }
        return true;
    }

    private boolean oneCandidates(FourPositionPoint point) {
        return false;
    }

    private boolean candidateIntersectsAllRemaining(FourPositionPoint point) {
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
