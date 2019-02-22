package algorithms;

import Collections.QuadTree;
import Parser.DataRecord;
import Parser.Parser;
import Parser.Pair;
import interfaces.AbstractAlgorithmInterface;
import main.Interpreter;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AlgorithmTester {

    private void runTest(DataRecord record, String fileName, double optHeight, AbstractAlgorithmInterface algorithms) {

        algorithms.solve(record);

        assertEquals(optHeight, record.height, "the height found is not correct in file: " + fileName);
        assertTrue(Interpreter.isValid(record), "the solution found is not valid in file: " + fileName);

    }


    private Collection<DynamicTest> readInFiles(String filePath, AbstractAlgorithmInterface algorithm) {
        try {
            File folder = new File(filePath);
            File[] listOfFiles = folder.listFiles();

            Parser parser = new Parser();
            Collection<DynamicTest> tests = new ArrayList<>();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    Pair<DataRecord, Double> input = parser.inputTestMode(new FileInputStream(listOfFiles[i]), QuadTree.class);
                    final int index = i;
                    tests.add(dynamicTest("test of " + algorithm.getClass() + " on file: " + listOfFiles[i].getName(),
                            () -> runTest(input.getKey(), listOfFiles[index].getName(), input.getValue(), algorithm)));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @TestFactory
    public Collection<DynamicTest> TwoPosTest() {
        return readInFiles("tests/algorithms/TestFiles/TwoPosTestFiles", new TwoPositionBinarySearcher());
    }

    @TestFactory
    public Collection<DynamicTest> FourPosTest() {
        return readInFiles("tests/algorithms/TestFiles/FourPosTestFiles", new TwoPositionBinarySearcher());
    }

    @TestFactory
    public Collection<DynamicTest> SliderTest() {
        return readInFiles("tests/algorithms/TestFiles/SliderTestFiles", new GreedySliderAlgorithm());
    }
}
