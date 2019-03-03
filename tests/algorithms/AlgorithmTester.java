package algorithms;

import Collections.KDTree;
import Collections.QuadTree;
import Parser.DataRecord;
import Parser.TestDataRecord;
import Parser.Parser;
import interfaces.AbstractAlgorithmInterface;
import interfaces.AbstractCollectionInterface;
import main.Interpreter;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AlgorithmTester {

    private void runTest(TestDataRecord record, String fileName, AbstractAlgorithmInterface algorithms) {
        algorithms.solve(record);
        assertTrue(record.height >= record.reqHeight, "the height found is too small in file: " + fileName + ", expected min: " + record.reqHeight + " actual value: " + record.height);
        assertTrue(record.height <= record.optHeight, "the height found is too large in file: " + fileName + ", max value: " + record.optHeight + " actual value: " + record.height);
        assertTrue(Interpreter.isValid(record), "the solution found is not valid in file: " + fileName);
    }


    private Collection<DynamicTest> readInFiles(String filePath, AbstractAlgorithmInterface algorithm) {
        try {
            File folder = new File(filePath);
            File[] listOfFiles = folder.listFiles();

            Parser parser = new Parser();
            Collection<DynamicTest> tests = new ArrayList<>();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    TestDataRecord input = parser.inputTestMode(new FileInputStream(file));
                    tests.add(dynamicTest("test of " + algorithm.getClass() + " on file: " + file.getName(),
                            () -> runTest(input, file.getName(), algorithm)));
                }
            }
            return tests;
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
