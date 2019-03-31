package algorithms;

import Parser.DataRecord;
import distance.FourPositionDistance;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import interfaces.models.PointInterface;
import models.*;
import Collections.KDTree;
import Collections.QuadTree;

import java.util.*;

public class FourPositionWagnerWolff extends BinarySearcher {

    // phase 2 queue
    private ArrayDeque<FourPositionPoint> pointsQueue;

    // conflict graph
    private ArrayList<FourPositionLabel> labelsWithConflicts;

    // selected candidates
    private ArrayList<FourPositionLabel> selectedLabels;

    // DataRecord containing all labels of the current sigma
    // Only used in preprocessing
    private DataRecord labels;

    private final int bruteForceLabels = 20;

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
    double[] findConflictSizes(DataRecord record) {
        FourPositionDistance distanceFunction = new FourPositionDistance();
        distanceFunction.setAspectRatio(record.aspectRatio);
        int k = 8; // number of nearest neighbours to search for
        if (record.labels.size() < 9) k = record.labels.size() - 1;
        Set<Double> conflictSizes = new HashSet<>();
        for (LabelInterface label : record.labels) {
            PointInterface point = label.getPOI();
            Set<GeometryInterface> nearestNeighbours = ((KDTree) record.collection).nearestNeighbours(distanceFunction, k, point);

            for (GeometryInterface target : nearestNeighbours) {
                double conflictSize = distanceFunction.calculate(point, (PointInterface) target);
                conflictSizes.add(conflictSize);
                conflictSizes.add(conflictSize / 2);
            }
        }
        double[] conflicts = new double[conflictSizes.size()];
        int i = 0;
        for (double size : conflictSizes) {
            conflicts[i++] = size;
        }

        // Optionally sort conflicts before passing them to general binary searcher
        Arrays.sort(conflicts);

        return conflicts;
    }

    /**
     * Eliminates all labels in given record which would contain a point (not of the label)
     * if the labels had a given size.
     *
     * @param record the given DataRecord
     * @param sigma  the size of the labels
     * @modifies pointsQueue
     * @modifies labelsWithConflicts
     * @modifies selectedLabels
     * @modifies labels
     * @post (\ forall p ; p \ in record ; p does not have any labels p_i assigned
     *where that p_i of size sigma contains another point in the record)
     */
    boolean preprocessing(DataRecord record, Double sigma) {
        // initialization
        pointsQueue = new ArrayDeque<>(record.labels.size() * 2);
        labelsWithConflicts = new ArrayList<>(record.labels.size() * 2);
        selectedLabels = new ArrayList<>(record.labels.size() * 2);
        labels = new DataRecord();
        labels.collection = new QuadTree(new Rectangle(0, 0, 10000, 10000));
        labels.aspectRatio = record.aspectRatio;
        double ratio = record.aspectRatio;
        double height = sigma;
        double width = sigma * ratio;

        for (LabelInterface p : record.labels) {
            double pX = p.getXMax();
            double pY = p.getYMax();

            Rectangle[] labelRectangles = FourPositionLabel.getAllDirectionRectangles(pX, pY, width, height);
            DirectionEnum[] directions = {DirectionEnum.NE, DirectionEnum.NW, DirectionEnum.SE, DirectionEnum.SW};
            int amountOfConflictingDirections = 0;

            FourPositionPoint point = new FourPositionPoint((FourPositionLabel) p);
            pointsQueue.add(point);

            for (int i = 0; i < directions.length; i++) {
                if (record.collection.query2D(labelRectangles[i]).size() == 0) {
                    FourPositionLabel dirLabel = new FourPositionLabel(height, ratio, 0, point, directions[i]);
                    point.addCandidate(dirLabel);
                    Collection<GeometryInterface> conflictingLabels = labels.collection.query2D(labelRectangles[i]);
                    preprocessingLabel(dirLabel, conflictingLabels);
                } else {
                    amountOfConflictingDirections ++;
                }
            }
            if (amountOfConflictingDirections == directions.length) {
                return false;
            }
        }
        return true;
    }

    /**
     * Subtask of the preprocessing function. If new label p_i intersects with existing labels, add it to labelsWithConflicts.
     * For all labels q_i that intersect p_i; Add q_i to conflict list of p_i; Add p_i to conflict list of q_i;
     * Add q_i to labelsWithConflicts if it is not in there already.
     * Also add p_i to the datarecord for further preprocessing
     *
     * @param label             to operate on
     * @param conflictingLabels all labels that conflict with the label to operate on
     */
    private void preprocessingLabel(FourPositionLabel label, Collection<GeometryInterface> conflictingLabels) {
        if (conflictingLabels.size() > 0) labelsWithConflicts.add(label);

        for (GeometryInterface square : conflictingLabels) {
            label.addConflict((FourPositionLabel) square);
            ((FourPositionLabel) square).addConflict(label);
            if (!labelsWithConflicts.contains(square)) {
                labelsWithConflicts.add((FourPositionLabel) square);
            }
        }

        labels.collection.insert(label);
    }

    /**
     * Eliminates all impossible candidates from the record for a given label size.
     * This function checks for the following four requirements: 1) if all candidates of
     * a point p have been eliminated, we return false since there is no solution; 2) If point p
     * has candidates free of intersection with other candidates, choose an arbitrary candidate
     * and eliminate all other candidates; 3) If p has only one candidate left, do the same on all other
     * squares it intersects (try to delete them); 4) If p has a candidate p_i which overlaps the last two
     * candidates of another site, then update and eliminate p_i.
     *
     * @return whether all points still have candidates left (if not there is no solution for this size)
     * @modifies pointsQueue
     * @post record does not contain candidates which can't be part of the solution
     */
    boolean eliminateImpossibleCandidates() {
        while (!pointsQueue.isEmpty()) {
            FourPositionPoint point = pointsQueue.pollFirst(); // also removes element from queue
            if (noCandidates(point)) {
                return false;
            } else if (hasCandidateWithoutIntersections(point)) {
            } else if (oneCandidate(point)) {
            } else if (candidateIntersectsAllRemaining(point)) {
            }
        }
        return true;
    }

    /**
     * returns whether a point has no alive candidates left.
     *
     * @param point to check
     * @return whether point has candidates alive
     */
    private boolean noCandidates(FourPositionPoint point) {
        return point.getCandidates().isEmpty();
    }


    /**
     * Checks whether the point has a candidate which is free of intersections with other labels, and selects such a
     * label if found.
     *
     * @param point to check
     * @return whether a candidate was chosen
     * @modifies selectedLabels
     * @modifies labelsWithConflicts
     * @modifies pointsQueue
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
        Collection<FourPositionLabel> toBeRemoved = new ArrayList<>();
        for (FourPositionLabel candidate : point.getCandidates()) {
            if (candidate != selected) {
                for (FourPositionLabel conflict : candidate.getConflicts()) {
                    conflict.removeConflict(candidate);
                    pointsQueue.remove(conflict.getPoI());
                    pointsQueue.addLast(conflict.getPoI());
                    if (conflict.getConflicts().size() == 0) labelsWithConflicts.remove(conflict);
                }
                labelsWithConflicts.remove(candidate);
                toBeRemoved.add(candidate);
            }
        }
        for (FourPositionLabel candidate : toBeRemoved) {
            candidate.getPoI().removeCandidate(candidate);
        }
        // remove selected label from conflict graph
        labelsWithConflicts.remove(selected);
        pointsQueue.remove(point);
        return true;
    }


    /**
     * Checks whether a point has only one candidate left, and if so, selects this candidate
     *
     * @param point the point to be processed
     * @return whether the one candidate was found and selected
     * @modifies selectedLabels
     * @modifies pointsQueue
     * @modifies labelsWithConflicts
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
            for (FourPositionLabel recurseConflict : conflict.getConflicts()) {
                if (recurseConflict == selected) continue;
                recurseConflict.removeConflict(conflict);
                pointsQueue.remove(recurseConflict.getPoI());
                pointsQueue.addLast(recurseConflict.getPoI());
                if (recurseConflict.getConflicts().size() == 0) labelsWithConflicts.remove(recurseConflict);
            }
            conflict.getPoI().removeCandidate(conflict);
        }


        // remove selected label from conflict graph
        labelsWithConflicts.remove(selected);
        pointsQueue.remove(point);
        return true;
    }

    /**
     * Checks whether a candidate p_i of a given point p intersects all remaining candidates of a different point q.
     * If so, p_i is removed.
     * If candidates of p were removed then p is put back in the queue
     *
     * @param point the given point
     * @return whether point had candidates that intersected all remaining candidates of another point
     * @modifies labelsWithConflicts
     * @modifies pointsQueue
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

            fullPointIntersect:
            for (FourPositionPoint intersectPoint : havingLabelsIntersecting) {
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
                for (FourPositionLabel conflict : candidate.getConflicts()) {
                    conflict.removeConflict(candidate);
                    pointsQueue.remove(conflict.getPoI());
                    pointsQueue.addLast(conflict.getPoI());
                    if (conflict.getConflicts().size() == 0) labelsWithConflicts.remove(conflict);
                }
                point.removeCandidate(candidate);
                labelsWithConflicts.remove(candidate);
            }
            pointsQueue.addFirst(point);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Eliminates candidates in conflict graph until point has at most two candidates left.
     *
     * @post PoI in conflictGraph have at most 2 candidates
     */
    boolean applyHeuristic() {
        // select Heuristic to be used

        // return numberOfConflictsHeuristic();
        // return numberOfConflictsHeuristicVariation();
        // return ratioPointsLabelsHeuristic();
        return intermediateEliminateHeuristic();
        // return densityHeuristic();
    }

    /**
     * Concrete Heuristic (I from paper)
     * Runs through all points twice:
     * Run 1 - For every point with 4 candidates, remove candidate with largest number of conflicts
     * Run 2 - For every point with 3 candidates, remove candidate with largest number of conflicts
     *
     * @modifies selectedLabels
     * @modifies labelsWithConflicts
     * @post all points have at most two candidates
     */
    private boolean numberOfConflictsHeuristic() {
        // select points
        Set<FourPositionPoint> conflictPoints = new HashSet<>();
        for (FourPositionLabel candidate : labelsWithConflicts) {
            conflictPoints.add(candidate.getPoI());
        }

        // remove highest conflict candidate for points with 4 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 4) {
                chooseLabelsNumberOfConflictsHeuristic(conflictPoint);
            }
        }

        // remove highest conflict candidate for points with 3 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 3) {
                chooseLabelsNumberOfConflictsHeuristic(conflictPoint);
            }
        }

        return true;
    }

    /**
     * Second Concrete Heuristic (adaptation of I from paper)
     * Runs through all points twice:
     * Run 1 - For every point with 4 candidates, remove 2 candidates with largest number of conflicts
     * Run 2 - For every point with 3 candidates, remove candidate with largest number of conflicts
     *
     * @modifies selectedLabels
     * @modifies labelsWithConflicts
     * @post all points have at most two candidates
     */
    private boolean numberOfConflictsHeuristicVariation() {
        // select points
        Set<FourPositionPoint> conflictPoints = new HashSet<>();
        for (FourPositionLabel candidate : labelsWithConflicts) {
            conflictPoints.add(candidate.getPoI());
        }

        // remove highest conflict candidate for points with 4 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 4) {
                chooseLabelsNumberOfConflictsHeuristic(conflictPoint);
                chooseLabelsNumberOfConflictsHeuristic(conflictPoint);
            }
        }

        // remove highest conflict candidate for points with 3 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 3) {
                chooseLabelsNumberOfConflictsHeuristic(conflictPoint);
            }
        }

        return true;
    }

    /**
     * Concrete Heuristic (adaptation of I from paper)
     * Calculates for every candidate label the ratio (# of intersecting labels / # of associated intersecting points)
     * Runs through all points twice:
     * Run 1 - For every point with 4 candidates, remove candidate with highest ratio
     * Run 2 - For every point with 3 candidates, remove candidate with highest ratio
     *
     * @modifies selectedLabels
     * @modifies labelsWithConflicts
     * @post all points have at most two candidates
     */
    private boolean ratioPointsLabelsHeuristic() {
        // Create HashMap mapping labels to their ratio
        HashMap<FourPositionLabel, Double> ratioMap = new HashMap<>();
        for (FourPositionLabel candidate : labelsWithConflicts) {
            HashSet<FourPositionPoint> intersections = new HashSet<>();
            for (FourPositionLabel conflict : candidate.getConflicts()) {
                intersections.add(conflict.getPoI());
            }
            double ratio = (double) candidate.getConflicts().size() / (double) intersections.size();

            ratioMap.put(candidate, ratio);
        }

        // select points
        Set<FourPositionPoint> conflictPoints = new HashSet<>();
        for (FourPositionLabel candidate : labelsWithConflicts) {
            conflictPoints.add(candidate.getPoI());
        }

        // remove highest conflict candidate for points with 4 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 4) {
                chooseLabelsRatioPointsLabelsHeuristic(conflictPoint, ratioMap);
            }
        }

        // remove highest conflict candidate for points with 3 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 3) {
                chooseLabelsRatioPointsLabelsHeuristic(conflictPoint, ratioMap);
            }
        }

        return true;
    }

    private void chooseLabelsRatioPointsLabelsHeuristic(FourPositionPoint conflictPoint, HashMap<FourPositionLabel, Double> ratioMap) {
        // select conflict candidate with highest ratio
        FourPositionLabel maxConflictCandidate = conflictPoint.getCandidates().get(0);
        for (FourPositionLabel candidate : conflictPoint.getCandidates()) {
            if (ratioMap.get(candidate) > ratioMap.get(maxConflictCandidate)) {
                maxConflictCandidate = candidate;
            }
        }

        // remove conflict candidate with highest ratio
        for (FourPositionLabel conflict : maxConflictCandidate.getConflicts()) {
            conflict.removeConflict(maxConflictCandidate);
        }
        maxConflictCandidate.getPoI().removeCandidate(maxConflictCandidate);
        labelsWithConflicts.remove(maxConflictCandidate);
    }

    private void chooseLabelsNumberOfConflictsHeuristic(FourPositionPoint conflictPoint) {
        // select highest conflict candidate
        FourPositionLabel maxConflictCandidate = conflictPoint.getCandidates().get(0);
        for (FourPositionLabel candidate : conflictPoint.getCandidates()) {
            if (candidate.getConflicts().size() > maxConflictCandidate.getConflicts().size()) {
                maxConflictCandidate = candidate;
            }
        }

        // remove highest conflict candidate
        for (FourPositionLabel conflict : maxConflictCandidate.getConflicts()) {
            conflict.removeConflict(maxConflictCandidate);
        }
        maxConflictCandidate.getPoI().removeCandidate(maxConflictCandidate);
        labelsWithConflicts.remove(maxConflictCandidate);
    }

    private boolean intermediateEliminateHeuristic() {
        // Collect all points that are still alive and put them back in the pointsQueue
        Set<FourPositionPoint> conflictPoints = new LinkedHashSet<>();
        for (FourPositionLabel candidate : labelsWithConflicts) {
            conflictPoints.add(candidate.getPoI());
        }

        // remove highest conflict candidate for points with 4 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 4) {
                chooseLabelsNumberOfConflictsHeuristic(conflictPoint);
            }
        }

        pointsQueue.addAll(conflictPoints);

        if (!eliminateImpossibleCandidates()) return false;

        // remove highest conflict candidate for points with 3 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 3) {
                chooseLabelsNumberOfConflictsHeuristic(conflictPoint);
            }
        }

        return true;
    }

    private boolean densityHeuristic() {
        // select points
        Set<FourPositionPoint> conflictPoints = new HashSet<>();
        for (FourPositionLabel candidate : labelsWithConflicts) {
            conflictPoints.add(candidate.getPoI());
        }

        pointsQueue.addAll(conflictPoints);

        // remove highest conflict candidate for points with 4 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 4) {
                chooseLabelsDensityHeuristic(conflictPoint);
            }
        }

        if (!eliminateImpossibleCandidates()) return false;

        // remove highest conflict candidate for points with 3 candidates
        for (FourPositionPoint conflictPoint : conflictPoints) {
            if (conflictPoint.getCandidates().size() == 3) {
                chooseLabelsDensityHeuristic(conflictPoint);
            }
        }

        return true;
    }

    private void chooseLabelsDensityHeuristic(FourPositionPoint conflictPoint) {
        // select highest conflict candidate
        FourPositionLabel maxConflictCandidate = conflictPoint.getCandidates().get(0);
        int maxDensity = 0;
        int density;
        final int checkRange = 3; // determines the range at which the density is determined

        for (FourPositionLabel candidate : conflictPoint.getCandidates()) {
            PointInterface bottomLeftSearch;
            PointInterface topRightSearch;

            switch (candidate.getDirection()) {
                case NE:
                    bottomLeftSearch = candidate.getBottomLeft();
                    topRightSearch = new Point(candidate.getXMin() + checkRange * (candidate.getXMax() - candidate.getXMin()), candidate.getYMin() + checkRange * (candidate.getYMax() - candidate.getYMin()));
                    break;
                case NW:
                    bottomLeftSearch = new Point(candidate.getXMax() - checkRange * (candidate.getXMax() - candidate.getXMin()), candidate.getYMin());
                    topRightSearch = new Point(candidate.getXMax(), candidate.getYMin() + checkRange * (candidate.getYMax() - candidate.getYMin()));
                    break;
                case SE:
                    bottomLeftSearch = new Point(candidate.getXMin(), candidate.getYMax() - checkRange * (candidate.getYMax() - candidate.getYMin()));
                    topRightSearch = new Point(candidate.getXMin() + checkRange * (candidate.getXMax() - candidate.getXMin()), candidate.getYMax());
                    break;
                case SW:
                    bottomLeftSearch = new Point(candidate.getXMax() - checkRange * (candidate.getXMax() - candidate.getXMin()),  candidate.getYMax() - checkRange * (candidate.getYMax() - candidate.getYMin()));
                    topRightSearch = candidate.getTopRight();
                    break;
                default:
                    bottomLeftSearch = new Point(0, 0);
                    topRightSearch = new Point(0, 0);
                    break;
            }

            density = labels.collection.query2D(new Rectangle(bottomLeftSearch, topRightSearch)).size();
            if (density > maxDensity) {
                maxConflictCandidate = candidate;
                maxDensity = density;
            }
        }

        // remove highest conflict candidate
        for (FourPositionLabel conflict : maxConflictCandidate.getConflicts()) {
            conflict.removeConflict(maxConflictCandidate);
        }
        maxConflictCandidate.getPoI().removeCandidate(maxConflictCandidate);
        labelsWithConflicts.remove(maxConflictCandidate);
    }

    /**
     * Try to solve if record does not have an obvious solution.
     * For those points which still have two or more candidates left, choose exactly two (heuristic),
     * and check, whether this remaining problem is solvable with 2-SAT (like 2 position)
     *
     * @return whether record can be solved with current height
     * @modifies record
     * @post record is solved if returnSolution is true
     */
    boolean doTwoSat(final boolean returnSolution) {
        // Get Points from labels
        Set<FourPositionPoint> intersectingPoints = new LinkedHashSet<>();
        for (FourPositionLabel conflictingLabel : labelsWithConflicts) {
            intersectingPoints.add(conflictingLabel.getPoI());
        }
        int count = 0;
        for (FourPositionPoint point : intersectingPoints) {
            point.setId(count++);
            point.getCandidates().get(0).setID(0);
            point.getCandidates().get(1).setID(1);
        }

        // Create input array from points
        ArrayList<List<Integer>[]> input = createImplicationGraph(intersectingPoints);

        // Get solution from
        // call is boolean solvable (adj of length 2n, invadj of length 2n)
        if (!returnSolution) {
            return (new ImplicationGraphSolver()).isSolvable(input.get(0), input.get(1));
        }
        // call getSolution(adj, inadj) assumes is solvable returns boolean array of length n
        boolean[] labels = (new ImplicationGraphSolver()).getSolution(input.get(0), input.get(1));

        // translate back to labels
        count = 0;
        for (FourPositionPoint point : intersectingPoints) {
            DirectionEnum direction;
            if (labels[count]) {
                direction = point.getCandidates().get(0).getDirection();
            } else {
                direction = point.getCandidates().get(1).getDirection();
            }
            point.getOriginalRecordLabel().setDirection(direction);
            count++;
        }
        for (FourPositionLabel label : selectedLabels) {
            label.getPoI().getOriginalRecordLabel().setDirection(label.getDirection());
        }

        return true;
    }

    /**
     * Creates the implication graph as requested by the {@Link ImplicationGraphSolver} based on the given points
     *
     * @param intersectingPoints
     * @return ArrayList containing the implication graph and the inverse implication graph represented
     * as specified by {@link ImplicationGraphSolver}
     * @pre (\ forall FourPositionPoint p ; intersectingPoints.contains ( p); p.getCandidates().size() == 2)
     * AND ID's of points in intersectingPoints consecutive
     * AND ID's of p.getCandidates() == (1, 2), for all p in intersectingPoints
     */
    private ArrayList<List<Integer>[]> createImplicationGraph(Set<FourPositionPoint> intersectingPoints) {
        int nPoints = intersectingPoints.size();
        ArrayList[] adj = new ArrayList[2 * nPoints];
        ArrayList[] invAdj = new ArrayList[2 * nPoints];

        for (int i = 0; i < 2 * nPoints; ++i) {
            adj[i] = new ArrayList<Integer>();
            invAdj[i] = new ArrayList<Integer>();
        }

        for (FourPositionPoint point : intersectingPoints) {
            for (FourPositionLabel label : point.getCandidates()) {
                for (FourPositionLabel conflictLabel : label.getConflicts()) {
                    adj[point.getId() + nPoints * label.getID()].add(conflictLabel.getPoI().getId() + nPoints * (1 - conflictLabel.getID()));
                    invAdj[conflictLabel.getPoI().getId() + nPoints * (1 - conflictLabel.getID())].add(point.getId() + nPoints * label.getID());
                }
            }
        }

        ArrayList<List<Integer>[]> returnSet = new ArrayList<>();
        returnSet.add(adj);
        returnSet.add(invAdj);

        return returnSet;
    }

    @Override
    double[] getSolutionSpace(DataRecord record) {
        return findConflictSizes(record);
    }

    @Override
    boolean isSolvable(DataRecord record, double height) {
        if (!preprocessing(record, height)) {
            return false;
        }

        boolean solvable = eliminateImpossibleCandidates();

        if (!solvable) return false;

        Set<FourPositionPoint> conflictingPoints = new HashSet<>();
        for (FourPositionLabel conflictingLabel : labelsWithConflicts) {
            conflictingPoints.add(conflictingLabel.getPoI());
        }
        
        if (labelsWithConflicts.size() < bruteForceLabels) {
            solvable = bruteForce(conflictingPoints, false);
        } else {
            if (!applyHeuristic()) return false;
            solvable = doTwoSat(false);
        }
        return solvable;
    }

    private boolean bruteForce(Set<FourPositionPoint> conflictingPoints, final boolean returnSolution) {
        // brute force 'heuristic'
        // Remove labels s.t. maximally 2 labels per point are left in brute force manner and check for 2sat solution each time
        if (twoSatReady(conflictingPoints)) {
            return doTwoSat(returnSolution);
        }

        //loop through all points
        for (FourPositionPoint point : conflictingPoints) {
            if (point.getCandidates().size() == 3) {
                Set<FourPositionPoint> newPoints = new HashSet<>(conflictingPoints);
                newPoints.remove(point);

                // remove 1 label from point and brute force the other points
                for (int i = 0; i < 3; i++) {
                    FourPositionLabel removingLabel = point.getCandidates().get(0);
                    ArrayList<FourPositionLabel> conflicts = removeLabel(removingLabel);
                    boolean option = bruteForce(newPoints, returnSolution);
                    if (option) return true;
                    addLabel(removingLabel, point, conflicts);
                }
            } else if (point.getCandidates().size() == 4) {
                // remove 1 label from point and brute force all points (including this one since it has 3 labels)
                for (int i = 0; i < 4; i++) {
                    FourPositionLabel removingLabel = point.getCandidates().get(0);
                    ArrayList<FourPositionLabel> conflicts = removeLabel(removingLabel);
                    boolean option = bruteForce(conflictingPoints, returnSolution);
                    if (option) return true;
                    addLabel(removingLabel, point, conflicts);
                }
            }
        }
        return false;
    }

    private ArrayList<FourPositionLabel> removeLabel(FourPositionLabel label) {
        ArrayList<FourPositionLabel> conflicts = label.getConflicts();
        label.getPoI().removeCandidate(label);
        for (FourPositionLabel conflict : label.getConflicts()) {
            conflict.removeConflict(label);
        }
        labelsWithConflicts.remove(label);
        return conflicts;
    }

    private void addLabel(FourPositionLabel label, FourPositionPoint point, ArrayList<FourPositionLabel> conflicts) {
        if (conflicts.size() > 0) labelsWithConflicts.add(label);
        for (FourPositionLabel conflict : conflicts) {
            conflict.addConflict(label);
        }
        point.addCandidate(label);
    }

    private boolean twoSatReady(Set<FourPositionPoint> conflictingPoints) {
        for (FourPositionPoint point : conflictingPoints) {
            if (point.getCandidates().size() > 2) return false;
        }
        return true;
    }

    @Override
    void getSolution(DataRecord record, double height) {
        preprocessing(record, height);
        eliminateImpossibleCandidates();

        Set<FourPositionPoint> conflictingPoints = new HashSet<>();
        for (FourPositionLabel conflictingLabel : labelsWithConflicts) {
            conflictingPoints.add(conflictingLabel.getPoI());
        }

        record.height = height;
        if (labelsWithConflicts.size() < bruteForceLabels) {
            bruteForce(conflictingPoints, true);
        } else {
            applyHeuristic();
            doTwoSat(true);
        }
    }


    // USED FOR TESTING!
    ArrayDeque<FourPositionPoint> getPointsQueue() {
        return pointsQueue;
    }

    ArrayList<FourPositionLabel> getLabelsWithConflicts() {
        return labelsWithConflicts;
    }

    ArrayList<FourPositionLabel> getSelectedLabels() {
        return selectedLabels;
    }

    public DataRecord getLabels() {
        return labels;
    }

    public void initializeDataStructures(int numberOfPoints) {
        pointsQueue = new ArrayDeque<>(numberOfPoints * 2);
        labelsWithConflicts = new ArrayList<>(numberOfPoints * 2);
        selectedLabels = new ArrayList<>(numberOfPoints * 2);
    }
}
