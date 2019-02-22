package algorithms;

import Parser.DataRecord;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import models.FourPositionLabel;
import models.FourPositionPoint;
import models.Rectangle;

import java.util.*;

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
        double ratio = record.aspectRatio;
        double height = sigma;
        double width = sigma*ratio;

        for (LabelInterface p : record.labels) {
            double pX = p.getXMax();
            double pY = p.getYMax();

            FourPositionPoint point = new FourPositionPoint(p.getBottomLeft());
            pointsQueue.add(point);

            // adding new labels (All id's are 0 for now)
            // add NE square
            if (record.collection.query2D(new Rectangle(pX, pY, pX+width, pY+height)).size() == 0) {
                FourPositionLabel northEastLabel = new FourPositionLabel(pX, pY, height, ratio, 0, point, NE);
                Collection<GeometryInterface> conflictingLabels = labels.collection.query2D(new Rectangle(pX, pY, pX+width, pY+height));
                preprocessingLabel(northEastLabel, conflictingLabels);
            }
            // add NW
            if (record.collection.query2D(new Rectangle(pX-width, pY, pX, pY+height)).size() == 0) {
                FourPositionLabel northWestLabel = new FourPositionLabel(pX, pY, height, ratio, 0, point, NW);
                Collection<GeometryInterface> conflictingLabels = labels.collection.query2D(new Rectangle(pX-width, pY, pX, pY+height));
                preprocessingLabel(northWestLabel, conflictingLabels);
            }
            // add SE
            if (record.collection.query2D(new Rectangle(pX, pY-height, pX+width, pY)).size() == 0) {
                FourPositionLabel southEastLabel = new FourPositionLabel(pX, pY, height, ratio, 0, point, SE);
                Collection<GeometryInterface> conflictingLabels = labels.collection.query2D(new Rectangle(pX, pY-height, pX+width, pY));
                preprocessingLabel(southEastLabel, conflictingLabels);
            }
            // add SW
            if (record.collection.query2D(new Rectangle(pX-width, pY-height, pX, pY)).size() == 0) {
                FourPositionLabel southWestLabel = new FourPositionLabel(pX, pY, height, ratio, 0, point, SW);
                Collection<GeometryInterface> conflictingLabels = labels.collection.query2D(new Rectangle(pX-width, pY-height, pX, pY));
                preprocessingLabel(southWestLabel, conflictingLabels);
            }
        }
    }

    /**
     * Subtask from the preprocessing function.
     *
     * @param label
     * @param conflictingLabels
     */
    private void preprocessingLabel(FourPositionLabel label, Collection<GeometryInterface> conflictingLabels) {
        // If new label intersects with existing labels, add it to labelsWithConflicts
        if (conflictingLabels.size() > 0) labelsWithConflicts.add(label);

        // for all squares that intersect the new label:
        //      add square to conflict list of the new label
        //      add new label to the conflicts list of the square
        //      add square to labelsWithConflicts lists if its not in there yet
        for (GeometryInterface square : conflictingLabels) {
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
                continue;
            } else if (oneCandidate(point)) {
                continue;
            } else if (candidateIntersectsAllRemaining(point)) {
                continue;
            }
        }
        return true;
    }

    /**
     * returns whether a point has no alive candidates left.
     *
     * @param point
     * @return whether point has candidates alive
     */
    private boolean noCandidates(FourPositionPoint point) {
        return point.getCandidates().isEmpty();
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
                break;
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
                candidate.getPoI().removeCandidate(candidate);
            }
        }
        // remove selected label from conflict graph TODO: determine whether required
        labelsWithConflicts.remove(selected);
        pointsQueue.remove(point);
        return true;
    }


    /**
     * Checks whether a point has only one candidate left, and if so, selects this candidate
     * @param point the point to be processed
     * @return whether the one candidate was found and selected
     */
    private boolean oneCandidate(FourPositionPoint point) {
        if (point.getCandidates().size() != 1) {
            return false;
        }

        // only one candidate left
        FourPositionLabel selected = point.getCandidates().get(0);
        selectedLabels.add(selected);

        // remove conflicting labels
        for (FourPositionLabel conflict : selected.getConflicts()) {
            pointsQueue.remove(conflict.getPoI());
            pointsQueue.addFirst(conflict.getPoI());
            labelsWithConflicts.remove(conflict);
            conflict.getPoI().removeCandidate(conflict);
        }
        // remove selected label from conflict graph
        labelsWithConflicts.remove(selected);
        pointsQueue.remove(point);
        return true;
    }

    /**
     * Checks wether a candidate p_i of a given point p intersects all remaining candidates of a different point q.
     * If so, p_i is removed.
     * If candidates of p were removed then p is put back in the queue
     *
     * @param point the given point
     * @return wether point had candidates that intersected all remaining candidates of another point
     */
    private boolean candidateIntersectsAllRemaining(FourPositionPoint point) {
        List<FourPositionLabel> labelsThatCantExist = new ArrayList<>();
        for (FourPositionLabel candidate : point.getCandidates()) {
            Set<FourPositionPoint> havingLabelsIntersecting = new HashSet<>();

            // get points of the labels that conflict with candidate
            for (FourPositionLabel intersection : candidate.getConflicts()) {
                havingLabelsIntersecting.add(intersection.getPoI());
            }

            boolean canExist = true; // whether candidate can exist in solution

            fullPointIntersect: for (FourPositionPoint intersectPoint : havingLabelsIntersecting) {
                for (FourPositionLabel intersectPointLabel : intersectPoint.getCandidates()) {
                    if (!intersectPointLabel.getConflicts().contains(candidate)) {
                        continue fullPointIntersect;
                    }
                }
                canExist = false;
            }

            if (!canExist) {
                labelsThatCantExist.add(candidate);
            }
        }

        if (!labelsThatCantExist.isEmpty()) {
            for (FourPositionLabel candidate : labelsThatCantExist) {
                point.removeCandidate(candidate);
                labelsWithConflicts.remove(candidate);
            }
            pointsQueue.addFirst(point);
            return true;
        } else {
            return false;
        }
    }

    @Override
    void applyHeuristic() {
        // select Heuristic to be used
        numberOfConflictsHeuristic();
    }

    /**
     * Concrete Heuristic (I from paper)
     * Runs through all points twice:
     * Run 1 - For every point with 4 candidates, remove candidate with largest number of conflicts
     * Run 2 - For every point with 3 candidates, remove candidate with largest number of conflicts
     *
     * @modifies selectedLabels
     * @post all points have at most two candidates
     */
    private void numberOfConflictsHeuristic(){
        // select points
        Set<FourPositionPoint> conflictPoints = new HashSet<>();
        for (FourPositionLabel candidate : labelsWithConflicts) {
            conflictPoints.add(candidate.getPoI());
        }

        // remove highest conflict candidate for points with 4 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 4) {
                // select highest conflict candidate
                FourPositionLabel maxConflictCandidate = conflictPoint.getCandidates().get(0);
                for (FourPositionLabel candidate : conflictPoint.getCandidates()) {
                    if (candidate.getConflicts().size() > maxConflictCandidate.getConflicts().size()) {
                        maxConflictCandidate = candidate;
                    }
                }

                // remove highest conflict candidate
                for (FourPositionLabel conflicts : maxConflictCandidate.getConflicts()) {
                    conflicts.removeConflict(maxConflictCandidate);
                }
                maxConflictCandidate.getPoI().removeCandidate(maxConflictCandidate);
                labelsWithConflicts.remove(maxConflictCandidate);
            }
        }

        // remove highest conflict candidate for points with 4 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 3) {
                // select highest conflict candidate
                FourPositionLabel maxConflictCandidate = conflictPoint.getCandidates().get(0);
                for (FourPositionLabel candidate : conflictPoint.getCandidates()) {
                    if (candidate.getConflicts().size() > maxConflictCandidate.getConflicts().size()) {
                        maxConflictCandidate = candidate;
                    }
                }

                // remove highest conflict candidate
                for (FourPositionLabel conflicts : maxConflictCandidate.getConflicts()) {
                    conflicts.removeConflict(maxConflictCandidate);
                }
                maxConflictCandidate.getPoI().removeCandidate(maxConflictCandidate);
                labelsWithConflicts.remove(maxConflictCandidate);
            }
        }
    }



    @Override
    void doTwoSat() { }

    @Override
    double[] getSolutionSpace(DataRecord record) {
        return findConflictSizes(record);
    }

    @Override
    boolean isSolvable(DataRecord record, double height) {
        preprocessing(record, height);
        boolean solvable = eliminateImpossibleCandidates();
        if (!solvable) return false;
        doTwoSat();
        return true;
    }

    @Override
    void getSolution(DataRecord record, double height) {
        // TODO: implement
    }
}