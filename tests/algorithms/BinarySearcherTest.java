package algorithms;

import Parser.DataRecord;
import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;
import models.Anchor;
import models.DirectionEnum;
import models.PositionLabel;
import models.Square;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.*;


class BinarySearchTest {


    private void runTest(float aspectRatio, float maxHeight) {
        BinarySearcher searcher = new BinarySearcher() {
            @Override
            boolean isSolvable(DataRecord record, double height) {
                // System.out.println(height);
                return height <= maxHeight;
            }

            @Override
            void getSolution(DataRecord record, double height) {
                assertTrue(Math.abs(height - maxHeight) < 0.1, "expected: " + maxHeight + " actual: " + height + " ratio: " + aspectRatio);
            }
        };
        DataRecord record = new DataRecord();
        record.aspectRatio = aspectRatio;
        record.points = new ArrayList<LabelInterface>();
        record.points.add(new PositionLabel(0, 0, 1, DirectionEnum.NE, 0));
        searcher.solve(record);

    }

    @Test
    public void test1() {
        runTest(1, 3);
        runTest(25.234234f, 3);
        runTest(25.234234f, 3.5f);
        runTest(25.234234f, 1000);
        runTest(25.234234f, 1000.5f);
        runTest(25.234234f, 25.234234f);
        runTest(25.234234f, 25.234234f * 5);
        runTest(1.4f, 3);
        runTest(1.4f, 3.5f);
        runTest(1.4f, 1000);
        runTest(1.4f, 1000.5f);
        runTest(1.4f, 1.4f);
        runTest(1.4f, 1.4f * 5);
        runTest(2f/27, 3);
        runTest(2f/27, 3.5f);
        runTest(2f/27, 1000);
        runTest(2f/27, 1000.5f);
        runTest(2f/27, 2f/27);
        runTest(2f/27, 2f/27 * 5);
    }
}