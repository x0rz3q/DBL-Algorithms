package algorithms;

import Collections.KDTree;
import Collections.QuadTree;
import Parser.DataRecord;
import interfaces.models.LabelInterface;
import models.*;
import models.Point;
import models.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FourPositionWagnerWolffTest {

    FourPositionWagnerWolff algo;

    @BeforeEach
    void setup() {
        algo = new FourPositionWagnerWolff();
    }

    @Test
    void findConflictSizes() {
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
        LabelInterface label1 = new FourPositionLabel(100, 100, 0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(120, 120, 0, record.aspectRatio, 0, point2, DirectionEnum.NE);
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
        LabelInterface label1 = new FourPositionLabel(100, 100, 0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(140, 140, 0, record.aspectRatio, 0, point2, DirectionEnum.NE);
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
    void createConflictGraph() {
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
        LabelInterface label1 = new FourPositionLabel(100, 100, 0, record.aspectRatio, 0, point1, DirectionEnum.NE);
        LabelInterface label2 = new FourPositionLabel(90, 90, 0, record.aspectRatio, 0, point2, DirectionEnum.NE);
        LabelInterface label3 = new FourPositionLabel(110, 110, 0, record.aspectRatio, 0, point3, DirectionEnum.NE);
        LabelInterface label4 = new FourPositionLabel(110, 90, 0, record.aspectRatio, 0, point4, DirectionEnum.NE);
        LabelInterface label5 = new FourPositionLabel(90, 110, 0, record.aspectRatio, 0, point5, DirectionEnum.NE);
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
        LabelInterface label = new FourPositionLabel(100, 100, 0, record.aspectRatio, 0, point, DirectionEnum.NE);
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
        FourPositionLabel label = new FourPositionLabel(100, 100, 30, 1, 0, point, DirectionEnum.NE);
        point.addCandidate(label);

        FourPositionPoint point2 = new FourPositionPoint(new Point(110, 110));
        algo.getPointsQueue().addLast(point2);
        FourPositionLabel label2 = new FourPositionLabel(90, 110, 30, 1, 0, point2, DirectionEnum.NE);
        FourPositionLabel label3 = new FourPositionLabel(90,110, 30, 1, 0, point2, DirectionEnum.NW);
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
    void applyHeuristic() {
    }

    @Test
    void doTwoSat() {
    }

    @Test
    void isSolvable() {
    }

    @Test
    void getSolution() {
    }
}