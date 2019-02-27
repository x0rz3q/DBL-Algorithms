package algorithms;

import Collections.KDTree;
import Parser.DataRecord;
import interfaces.models.LabelInterface;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FourPositionWagnerWolffTest {

    private FourPositionWagnerWolff algo;

    @BeforeEach
    void setup() {
        algo = new FourPositionWagnerWolff();
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
    void applyHeuristic() {
    }

    @Test
    void doTwoSat() {
        DataRecord record = new DataRecord();
        record.labels = new ArrayList<>();
        record.collection = new KDTree();
        record.aspectRatio = 1;
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
        FourPositionLabel pointLabel1 = new FourPositionLabel(15, record.aspectRatio, 0, pointPoint, DirectionEnum.NE);
        FourPositionLabel pointLabel2 = new FourPositionLabel(15, record.aspectRatio, 0, pointPoint, DirectionEnum.NW);
        FourPositionLabel point2Label1 = new FourPositionLabel(15, record.aspectRatio, 0, point2Point, DirectionEnum.SE);
        FourPositionLabel point2Label2 = new FourPositionLabel(15, record.aspectRatio, 0, point2Point, DirectionEnum.SW);

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

        algo.doTwoSat(record, true);

    }

    @Test
    void isSolvable() {
    }

    @Test
    void getSolution() {
    }
}