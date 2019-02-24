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
        assertTrue(algo.getPointsQueue().size() == 2);
        assertTrue(algo.getLabelsWithConflicts().size() == 4);
        assertTrue(algo.getSelectedLabels().size() == 0);
        assertTrue(algo.getLabels().collection.size() == 6);
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
        assertTrue(algo.getPointsQueue().size() == 2);
        assertTrue(algo.getLabelsWithConflicts().size() == 2);
        assertTrue(algo.getSelectedLabels().size() == 0);
        assertTrue(algo.getLabels().collection.size() == 8);
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
        assertTrue(algo.getLabels().collection.size() == 1);
        assertTrue(algo.getPointsQueue().size() == 0);
        assertTrue(algo.getLabelsWithConflicts().size() == 0);
        assertTrue(algo.getSelectedLabels().size() == 1);
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