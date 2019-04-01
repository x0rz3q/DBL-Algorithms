package algorithms;

import Collections.KDTree;
import Parser.DataRecord;
import interfaces.models.LabelInterface;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FourPositionWagnerWolffTest {

    private FourPositionWagnerWolff algo;

    @BeforeEach
    void setup() {
        algo = new FourPositionWagnerWolff(0);
    }

    @Test
    void findConflictSizes() {
        DataRecord record = new DataRecord();
        record.aspectRatio = 1.3;
        record.collection = new KDTree();
        record.labels = new ArrayList<>();
        FourPositionPoint point1 = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(120, 160));
        record.collection.insert(point1);
        record.collection.insert(point2);
        LabelInterface label1 = new FourPositionLabel(0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        record.labels.add(label1);
        record.labels.add(label2);
        double[] conflictSizes = algo.findConflictSizes(record);
        assertEquals(2, conflictSizes.length);
    }

    @Test
    void preprocessing1() {
        DataRecord record = new DataRecord();
        record.aspectRatio = 1;
        record.collection = new KDTree();
        record.labels = new ArrayList<>();
        FourPositionPoint point1 = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(120, 120));
        record.collection.insert(point1);
        record.collection.insert(point2);
        LabelInterface label1 = new FourPositionLabel(0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        record.labels.add(label1);
        record.labels.add(label2);
        double sigma = 30;
        algo.preprocessing(record, sigma);
        assertEquals(2, algo.getPointsQueue().size());
        assertEquals(4, algo.getLabelsWithConflicts().size());
        assertEquals(0, algo.getSelectedLabels().size());
        assertEquals(6, algo.getLabels().collection.size());
    }

    @Test
    void preprocessing2() {
        DataRecord record = new DataRecord();
        record.aspectRatio = 1;
        record.collection = new KDTree();
        record.labels = new ArrayList<>();
        FourPositionPoint point1 = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(140, 140));
        record.collection.insert(point1);
        record.collection.insert(point2);
        LabelInterface label1 = new FourPositionLabel(0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        record.labels.add(label1);
        record.labels.add(label2);
        double sigma = 30;
        algo.preprocessing(record, sigma);
        assertEquals(2, algo.getPointsQueue().size());
        assertEquals(2, algo.getLabelsWithConflicts().size());
        assertEquals(0, algo.getSelectedLabels().size());
        assertEquals(8, algo.getLabels().collection.size());
    }

    @Test
    void eliminateImpossibleCandidates1() {
        DataRecord record = new DataRecord();
        record.aspectRatio = 1;
        record.collection = new KDTree();
        record.labels = new ArrayList<>();
        FourPositionPoint point1 = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(90, 90));
        FourPositionPoint point3 = new FourPositionPoint(new Point(110, 110));
        FourPositionPoint point4 = new FourPositionPoint(new Point(110, 90));
        FourPositionPoint point5 = new FourPositionPoint(new Point(90, 110));
        LabelInterface label1 = new FourPositionLabel(0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        LabelInterface label3 = new FourPositionLabel(0, record.aspectRatio, 0, point3, DirectionEnum.NE);
        LabelInterface label4 = new FourPositionLabel(0, record.aspectRatio, 0, point4, DirectionEnum.NE);
        LabelInterface label5 = new FourPositionLabel(0, record.aspectRatio, 0, point5, DirectionEnum.NE);
        record.collection.insert(point1);
        record.collection.insert(point2);
        record.collection.insert(point3);
        record.collection.insert(point4);
        record.collection.insert(point5);
        record.labels.add(label1);
        record.labels.add(label2);
        record.labels.add(label3);
        record.labels.add(label4);
        record.labels.add(label5);
        double sigma = 30;
        algo.preprocessing(record, sigma);
        assertFalse(algo.eliminateImpossibleCandidates());
    }

    @Test
    void eliminateImpossibleCandidates2() {
        DataRecord record = new DataRecord();
        record.aspectRatio = 1;
        record.collection = new KDTree();
        record.labels = new ArrayList<>();
        FourPositionPoint point = new FourPositionPoint(new Point(100, 100));
        LabelInterface label = new FourPositionLabel(0, record.aspectRatio, 0, point, DirectionEnum.NE);
        record.collection.insert(point);
        record.labels.add(label);
        double sigma = 30;
        algo.preprocessing(record, sigma);
        assertTrue(algo.eliminateImpossibleCandidates());
        assertEquals(0, algo.getPointsQueue().size());
        assertEquals(0, algo.getLabelsWithConflicts().size());
        assertEquals(1, algo.getSelectedLabels().size());
    }

    @Test
    void eliminateImpossibleCandidates3() {
        FourPositionPoint point = new FourPositionPoint(new Point(100, 100));
        algo.initializeDataStructures(2);
        algo.getPointsQueue().add(point);
        FourPositionLabel label = new FourPositionLabel(30, 1, 0, point, DirectionEnum.NE);
        point.addCandidate(label);

        FourPositionPoint point2 = new FourPositionPoint(new Point(110, 110));
        algo.getPointsQueue().addLast(point2);
        FourPositionLabel label2 = new FourPositionLabel(30, 1, 0, point2, DirectionEnum.NE);
        FourPositionLabel label3 = new FourPositionLabel(30, 1, 0, point2, DirectionEnum.NW);
        point2.addCandidate(label2);
        point2.addCandidate(label3);

        label2.addConflict(label);
        label.addConflict(label2);
        algo.getLabelsWithConflicts().add(label);
        algo.getLabelsWithConflicts().add(label2);

        assertTrue(algo.eliminateImpossibleCandidates());
        assertEquals(0, algo.getPointsQueue().size());
        assertEquals(0, algo.getLabelsWithConflicts().size());
        assertTrue(algo.getSelectedLabels().contains(label));
        assertTrue(algo.getSelectedLabels().contains(label3));
        assertEquals(2, algo.getSelectedLabels().size());
    }

    @Test
    void eliminateImpossibleCandidates4() {
        algo.initializeDataStructures(5);
        FourPositionPoint point = new FourPositionPoint(new Point(100, 100));
        FourPositionLabel label = new FourPositionLabel(30, 1, 0, point, DirectionEnum.NE);
        point.addCandidate(label);

        FourPositionPoint point2 = new FourPositionPoint(new Point(90, 110));
        FourPositionLabel label2 = new FourPositionLabel(30, 1, 0, point2, DirectionEnum.NE);
        FourPositionLabel label3 = new FourPositionLabel(30, 1, 0, point2, DirectionEnum.NW);
        point2.addCandidate(label2);
        point2.addCandidate(label3);

        FourPositionPoint point3 = new FourPositionPoint(new Point(80, 150));
        FourPositionLabel label4 = new FourPositionLabel(30,1, 0, point3, DirectionEnum.SW);
        FourPositionLabel label5 = new FourPositionLabel(30, 1, 0, point3, DirectionEnum.NW);
        point3.addCandidate(label4);
        point3.addCandidate(label5);


        label2.addConflict(label);
        label.addConflict(label2);
        label3.addConflict(label4);
        label4.addConflict(label3);
        algo.getLabelsWithConflicts().add(label);
        algo.getLabelsWithConflicts().add(label2);
        algo.getLabelsWithConflicts().add(label3);
        algo.getLabelsWithConflicts().add(label4);

        algo.getPointsQueue().addFirst(point2);
        algo.getPointsQueue().addLast(point);
        algo.getPointsQueue().addLast(point3);

        assertTrue(algo.eliminateImpossibleCandidates());
        assertEquals(0, algo.getPointsQueue().size());
        assertEquals(3, algo.getSelectedLabels().size());
        assertTrue(algo.getSelectedLabels().contains(label));
        assertTrue(algo.getSelectedLabels().contains(label3));
        assertTrue(algo.getSelectedLabels().contains(label5));
        assertEquals(0, algo.getLabelsWithConflicts().size());
    }

    @Test
    void eliminateImpossibleCandidates() {
        DataRecord record = new DataRecord();
        record.aspectRatio = 1;
        record.collection = new KDTree();
        record.labels = new ArrayList<>();
        FourPositionPoint point = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(115, 115));
        FourPositionPoint point3 = new FourPositionPoint(new Point(95, 120));
        FourPositionPoint point4 = new FourPositionPoint(new Point(110, 95));
        FourPositionLabel label = new FourPositionLabel(0, record.aspectRatio, 0, point, DirectionEnum.NE);
        FourPositionLabel label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        FourPositionLabel label3 = new FourPositionLabel(0, record.aspectRatio, 0, point3, DirectionEnum.NE);
        FourPositionLabel label4 = new FourPositionLabel(0, record.aspectRatio, 0, point4, DirectionEnum.NE);
        record.collection.insert(point);
        record.collection.insert(point2);
        record.collection.insert(point3);
        record.collection.insert(point4);
        record.labels.add(label);
        record.labels.add(label2);
        record.labels.add(label3);
        record.labels.add(label4);
        double sigma = 30;
        algo.preprocessing(record, sigma);
        assertTrue(algo.eliminateImpossibleCandidates());
        assertEquals(4, algo.getSelectedLabels().size());
        assertEquals(0, algo.getPointsQueue().size());
        assertEquals(0, algo.getLabelsWithConflicts().size());
    }

    @Test
    void applyHeuristic1() {
        algo.initializeDataStructures(5);
        FourPositionPoint point = new FourPositionPoint(new Point(100, 100));
        FourPositionLabel label1 = new FourPositionLabel(0, 1, 0, point, DirectionEnum.NE);
        FourPositionLabel label2 = new FourPositionLabel(0, 1, 0, point, DirectionEnum.SE);
        FourPositionLabel label3 = new FourPositionLabel(0, 1, 0, point, DirectionEnum.SW);
        point.addCandidate(label1);
        point.addCandidate(label2);
        point.addCandidate(label3);
        algo.getLabelsWithConflicts().add(label1);
        algo.getLabelsWithConflicts().add(label2);
        algo.getLabelsWithConflicts().add(label3);
        algo.applyHeuristic(0);

        for (FourPositionLabel label : algo.getLabelsWithConflicts()) {
            assertTrue(label.getPoI().getCandidates().size() == 2);
        }
    }

    @Test
    void applyHeuristic2() {
        algo.initializeDataStructures(5);
        FourPositionPoint point = new FourPositionPoint(new Point(100, 100));
        FourPositionLabel label1 = new FourPositionLabel(0, 1, 0, point, DirectionEnum.NE);
        FourPositionLabel label2 = new FourPositionLabel(0, 1, 0, point, DirectionEnum.SE);
        FourPositionLabel label3 = new FourPositionLabel(0, 1, 0, point, DirectionEnum.SW);
        FourPositionLabel label4 = new FourPositionLabel(0, 1, 0, point, DirectionEnum.NW);
        point.addCandidate(label1);
        point.addCandidate(label2);
        point.addCandidate(label3);
        point.addCandidate(label4);
        algo.getLabelsWithConflicts().add(label1);
        algo.getLabelsWithConflicts().add(label2);
        algo.getLabelsWithConflicts().add(label3);
        algo.getLabelsWithConflicts().add(label4);
        algo.applyHeuristic(0);

        for (FourPositionLabel label : algo.getLabelsWithConflicts()) {
            assertTrue(label.getPoI().getCandidates().size() == 2);
        }
    }

    @Test
    void applyHeuristic3() {
        algo.initializeDataStructures(5);
        FourPositionPoint point1 = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(50, 50));

        FourPositionLabel point1label1 = new FourPositionLabel(0, 1, 0, point1, DirectionEnum.NE);
        FourPositionLabel point1label2 = new FourPositionLabel(0, 1, 0, point1, DirectionEnum.SE);
        FourPositionLabel point1label3 = new FourPositionLabel(0, 1, 0, point1, DirectionEnum.SW);
        FourPositionLabel point2label1 = new FourPositionLabel(0, 1, 0, point2, DirectionEnum.NE);
        FourPositionLabel point2label2 = new FourPositionLabel(0, 1, 0, point2, DirectionEnum.SE);
        FourPositionLabel point2label3 = new FourPositionLabel(0, 1, 0, point2, DirectionEnum.SW);
        FourPositionLabel point2label4 = new FourPositionLabel(0, 1, 0, point2, DirectionEnum.NW);

        point1.addCandidate(point1label1);
        point1.addCandidate(point1label2);
        point1.addCandidate(point1label3);
        point2.addCandidate(point2label1);
        point2.addCandidate(point2label2);
        point2.addCandidate(point2label3);
        point2.addCandidate(point2label4);

        algo.getLabelsWithConflicts().add(point1label1);
        algo.getLabelsWithConflicts().add(point1label2);
        algo.getLabelsWithConflicts().add(point1label3);
        algo.getLabelsWithConflicts().add(point2label1);
        algo.getLabelsWithConflicts().add(point2label2);
        algo.getLabelsWithConflicts().add(point2label3);
        algo.getLabelsWithConflicts().add(point2label4);

        algo.applyHeuristic(0);

        for (FourPositionLabel label : algo.getLabelsWithConflicts()) {
            assertTrue(label.getPoI().getCandidates().size() <= 2);
        }
    }

    @Test
    void applyHeuristic4() {
        // Ask Hidde for picture
        // Create points
        algo.initializeDataStructures(5);
        FourPositionPoint pointNW = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint pointNE = new FourPositionPoint(new Point(119, 102));
        FourPositionPoint pointSE = new FourPositionPoint(new Point(121, 83));
        FourPositionPoint pointSW = new FourPositionPoint(new Point(102, 81));

        // Create labels
        FourPositionLabel pointNWlabelNE = new FourPositionLabel(10,1, 0, pointNW, DirectionEnum.NE);
        FourPositionLabel pointNWlabelSE = new FourPositionLabel(10,1, 0, pointNW, DirectionEnum.SE);
        FourPositionLabel pointNWlabelSW = new FourPositionLabel(10,1, 0, pointNW, DirectionEnum.SW);

        FourPositionLabel pointNElabelNW = new FourPositionLabel(10, 1, 0, pointNE, DirectionEnum.NW);
        FourPositionLabel pointNElabelSE = new FourPositionLabel(10, 1, 0, pointNE, DirectionEnum.SE);
        FourPositionLabel pointNElabelSW = new FourPositionLabel(10, 1, 0, pointNE, DirectionEnum.SW);

        FourPositionLabel pointSElabelNW = new FourPositionLabel(10, 1,0, pointSE, DirectionEnum.NW);
        FourPositionLabel pointSElabelNE = new FourPositionLabel(10, 1,0, pointSE, DirectionEnum.NE);
        FourPositionLabel pointSElabelSW = new FourPositionLabel(10, 1,0, pointSE, DirectionEnum.SW);

        FourPositionLabel pointSWlabelNW = new FourPositionLabel(10, 1, 0, pointSW, DirectionEnum.NW);
        FourPositionLabel pointSWlabelNE = new FourPositionLabel(10, 1, 0, pointSW, DirectionEnum.NE);
        FourPositionLabel pointSWlabelSE = new FourPositionLabel(10, 1, 0, pointSW, DirectionEnum.SE);

        // Add conflicts
        pointNWlabelNE.addConflict(pointNElabelNW);
        pointNWlabelNE.addConflict(pointNElabelSW);

        pointNWlabelSW.addConflict(pointSWlabelNW);

        pointNWlabelSE.addConflict(pointSWlabelNW);
        pointNWlabelSE.addConflict(pointSWlabelNE);
        pointNWlabelSE.addConflict(pointNElabelSW);

        pointNElabelNW.addConflict(pointNWlabelNE);

        pointNElabelSW.addConflict(pointNWlabelNE);
        pointNElabelSW.addConflict(pointNWlabelSE);
        pointNElabelSW.addConflict(pointSElabelNW);

        pointNElabelSE.addConflict(pointSElabelNW);
        pointNElabelSE.addConflict(pointSElabelNE);

        pointSElabelNW.addConflict(pointNElabelSW);
        pointSElabelNW.addConflict(pointNElabelSE);
        pointSElabelNW.addConflict(pointSWlabelNE);

        pointSElabelSW.addConflict(pointSWlabelNE);
        pointSElabelSW.addConflict(pointSWlabelSE);

        pointSElabelNE.addConflict(pointNElabelSE);

        pointSWlabelNW.addConflict(pointNWlabelSW);
        pointSWlabelNW.addConflict(pointNWlabelSE);

        pointSWlabelNE.addConflict(pointNWlabelSE);
        pointSWlabelNE.addConflict(pointSElabelNW);
        pointSWlabelNE.addConflict(pointSElabelSW);

        pointSWlabelSE.addConflict(pointSElabelSW);

        // Register candidates to points
        pointNW.addCandidate(pointNWlabelNE);
        pointNW.addCandidate(pointNWlabelSE);
        pointNW.addCandidate(pointNWlabelSW);

        pointNE.addCandidate(pointNElabelNW);
        pointNE.addCandidate(pointNElabelSW);
        pointNE.addCandidate(pointNElabelSE);

        pointSE.addCandidate(pointSElabelNW);
        pointSE.addCandidate(pointSElabelNE);
        pointSE.addCandidate(pointSElabelSW);

        pointSW.addCandidate(pointSWlabelNW);
        pointSW.addCandidate(pointSWlabelNE);
        pointSW.addCandidate(pointSWlabelSE);

        // Register candidates to LabelsWithConflicts
        algo.getLabelsWithConflicts().add(pointNWlabelNE);
        algo.getLabelsWithConflicts().add(pointNWlabelSE);
        algo.getLabelsWithConflicts().add(pointNWlabelSW);

        algo.getLabelsWithConflicts().add(pointNElabelNW);
        algo.getLabelsWithConflicts().add(pointNElabelSW);
        algo.getLabelsWithConflicts().add(pointNElabelSE);

        algo.getLabelsWithConflicts().add(pointSElabelNW);
        algo.getLabelsWithConflicts().add(pointSElabelNE);
        algo.getLabelsWithConflicts().add(pointSElabelSW);

        algo.getLabelsWithConflicts().add(pointSWlabelNW);
        algo.getLabelsWithConflicts().add(pointSWlabelNE);
        algo.getLabelsWithConflicts().add(pointSWlabelSE);

        algo.applyHeuristic(0);

        for (FourPositionLabel label : algo.getLabelsWithConflicts()) {
            assertTrue(label.getPoI().getCandidates().size() == 2);
        }
    }

    @Test
    void doTwoSat() {
        algo.initializeDataStructures(10);
        DataRecord record = new DataRecord();
        record.labels = new ArrayList<>();
        record.collection = new KDTree();
        record.aspectRatio = 1;
        record.height = 15;
        FourPositionPoint point = new FourPositionPoint(new Point(100,100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(120, 100));
        FourPositionLabel label = new FourPositionLabel(0, record.aspectRatio, 0, point, DirectionEnum.NE);
        FourPositionLabel label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        record.collection.insert(point);
        record.collection.insert(point2);
        record.labels.add(label);
        record.labels.add(label2);

        FourPositionPoint pointPoint = new FourPositionPoint(label);
        FourPositionPoint point2Point = new FourPositionPoint(label2);
        FourPositionLabel pointLabel1 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint, DirectionEnum.NE);
        FourPositionLabel pointLabel2 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint, DirectionEnum.NW);
        FourPositionLabel point2Label1 = new FourPositionLabel(record.height, record.aspectRatio, 0, point2Point, DirectionEnum.SE);
        FourPositionLabel point2Label2 = new FourPositionLabel(record.height, record.aspectRatio, 0, point2Point, DirectionEnum.SW);

        pointPoint.addCandidate(pointLabel1);
        pointPoint.addCandidate(pointLabel2);
        point2Point.addCandidate(point2Label1);
        point2Point.addCandidate(point2Label2);

        pointLabel1.addConflict(point2Label1);
        point2Label1.addConflict(pointLabel1);
        pointLabel2.addConflict(point2Label2);
        point2Label2.addConflict(pointLabel2);

        algo.getLabelsWithConflicts().add(pointLabel1);
        algo.getLabelsWithConflicts().add(pointLabel2);
        algo.getLabelsWithConflicts().add(point2Label1);
        algo.getLabelsWithConflicts().add(point2Label2);

        algo.doTwoSat(true);
    }

    @Test
    void doTwoSat2() {
        algo.initializeDataStructures(10);
        DataRecord record = new DataRecord();
        record.labels = new ArrayList<>();
        record.collection = new KDTree();
        record.aspectRatio = 1;
        record.height = 30;
        FourPositionPoint point1 = new FourPositionPoint(new Point(430, 460));
        FourPositionPoint point2 = new FourPositionPoint(new Point(465, 450));
        FourPositionPoint point3 = new FourPositionPoint(new Point(450, 500));
        FourPositionPoint point4 = new FourPositionPoint(new Point(500, 500));
        record.collection.insert(point1);
        record.collection.insert(point2);
        record.collection.insert(point3);
        record.collection.insert(point4);
        FourPositionLabel label1 = new FourPositionLabel(0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        FourPositionLabel label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        FourPositionLabel label3 = new FourPositionLabel(0, record.aspectRatio, 0, point3, DirectionEnum.NE);
        FourPositionLabel label4 = new FourPositionLabel(0, record.aspectRatio, 0, point4, DirectionEnum.NE);
        record.labels.add(label1);
        record.labels.add(label2);
        record.labels.add(label3);
        record.labels.add(label4);

        FourPositionPoint pointPoint1 = new FourPositionPoint(label1);
        FourPositionPoint pointPoint2 = new FourPositionPoint(label2);
        FourPositionPoint pointPoint3 = new FourPositionPoint(label3);
        FourPositionPoint pointPoint4 = new FourPositionPoint(label4);
        FourPositionLabel pointPoint1Label1 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint1, DirectionEnum.NE);
        FourPositionLabel pointPoint1Label2 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint1, DirectionEnum.SE);
        pointPoint1.addCandidate(pointPoint1Label1);
        pointPoint1.addCandidate(pointPoint1Label2);
        FourPositionLabel pointPoint2Label1 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint2, DirectionEnum.NE);
        FourPositionLabel pointPoint2Label2 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint2, DirectionEnum.SW);
        pointPoint2.addCandidate(pointPoint2Label1);
        pointPoint2.addCandidate(pointPoint2Label2);
        FourPositionLabel pointPoint3Label1 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint3, DirectionEnum.NE);
        FourPositionLabel pointPoint3Label2 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint3, DirectionEnum.SE);
        pointPoint3.addCandidate(pointPoint3Label1);
        pointPoint3.addCandidate(pointPoint3Label2);
        FourPositionLabel pointPoint4Label1 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint4, DirectionEnum.NW);
        FourPositionLabel pointPoint4Label2 = new FourPositionLabel(record.height, record.aspectRatio, 0, pointPoint4, DirectionEnum.SW);
        pointPoint4.addCandidate(pointPoint4Label1);
        pointPoint4.addCandidate(pointPoint4Label2);

        pointPoint1Label1.addConflict(pointPoint3Label2);
        pointPoint1Label2.addConflict(pointPoint2Label2);
        pointPoint2Label1.addConflict(pointPoint3Label2);
        pointPoint2Label1.addConflict(pointPoint4Label2);
        pointPoint2Label2.addConflict(pointPoint1Label2);
        pointPoint3Label1.addConflict(pointPoint4Label1);
        pointPoint3Label2.addConflict(pointPoint1Label1);
        pointPoint3Label2.addConflict(pointPoint2Label1);
        pointPoint3Label2.addConflict(pointPoint4Label2);
        pointPoint4Label1.addConflict(pointPoint3Label1);
        pointPoint4Label2.addConflict(pointPoint2Label1);
        pointPoint4Label2.addConflict(pointPoint3Label2);

        algo.getLabelsWithConflicts().add(pointPoint1Label1);
        algo.getLabelsWithConflicts().add(pointPoint1Label2);
        algo.getLabelsWithConflicts().add(pointPoint2Label1);
        algo.getLabelsWithConflicts().add(pointPoint2Label2);
        algo.getLabelsWithConflicts().add(pointPoint3Label1);
        algo.getLabelsWithConflicts().add(pointPoint3Label2);
        algo.getLabelsWithConflicts().add(pointPoint4Label1);
        algo.getLabelsWithConflicts().add(pointPoint4Label2);

        algo.doTwoSat(true);
        assertEquals("NE", record.labels.get(0).getPlacement());
        assertEquals("SW", record.labels.get(1).getPlacement());
        assertEquals("NE", record.labels.get(2).getPlacement());
        assertEquals("SW", record.labels.get(3).getPlacement());
    }

    @Test
    void isSolvable() {
    }

    @Test
    void getSolution() {
        DataRecord record = new DataRecord();
        record.labels = new ArrayList<>();
        record.collection = new KDTree();
        record.aspectRatio = 1;
        FourPositionPoint point1 = new FourPositionPoint(new Point(100, 100));
        FourPositionPoint point2 = new FourPositionPoint(new Point(200, 200));
        FourPositionPoint point3 = new FourPositionPoint(new Point(300, 300));
        FourPositionPoint point4 = new FourPositionPoint(new Point(400, 400));
        FourPositionPoint point5 = new FourPositionPoint(new Point(500, 500));
        LabelInterface label1 = new FourPositionLabel(0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        LabelInterface label3 = new FourPositionLabel(0, record.aspectRatio, 0, point3, DirectionEnum.NE);
        LabelInterface label4 = new FourPositionLabel(0, record.aspectRatio, 0, point4, DirectionEnum.NE);
        LabelInterface label5 = new FourPositionLabel(0, record.aspectRatio, 0, point5, DirectionEnum.NE);
        record.collection.insert(point1);
        record.collection.insert(point2);
        record.collection.insert(point3);
        record.collection.insert(point4);
        record.collection.insert(point5);
        record.labels.add(label1);
        record.labels.add(label2);
        record.labels.add(label3);
        record.labels.add(label4);
        record.labels.add(label5);
        algo.getSolution(record, 200);
    }
}