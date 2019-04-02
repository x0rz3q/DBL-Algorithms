package algorithms;

import Parser.Parser;
import Parser.TestDataRecord;
import interfaces.AbstractAlgorithmInterface;
import interfaces.models.LabelInterface;
import models.FourPositionLabel;
import models.PositionLabel;
import models.SliderLabel;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import visualizer.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AlgorithmTester {

    private void runTest(TestDataRecord record, String fileName, AbstractAlgorithmInterface algorithms) {
        algorithms.solve(record);

        if (algorithms instanceof FourPositionWagnerWolff) {
            for (LabelInterface l : record.labels) {
                ((FourPositionLabel) l).setHeight(record.height);
            }
        } else if (algorithms instanceof TwoPositionBinarySearcher){
            for (LabelInterface l : record.labels) {
                ((PositionLabel) l).setHeight(record.height);
            }
        } else if (algorithms instanceof GreedySliderAlgorithm){
            for (LabelInterface l : record.labels) {
                ((SliderLabel) l).setHeight(record.height);
            }
        }
        assertTrue(!Interpreter.overlap(record.labels), "the solution found is not valid in file: " + fileName);
        assertTrue(record.height >= record.reqHeight, "the height found is too small in file: " + fileName + ", expected min: " + record.reqHeight + " actual value: " + record.height);
        assertTrue(record.height <= record.optHeight, "the height found is too large in file: " + fileName + ", max value: " + record.optHeight + " actual value: " + record.height);

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
        return readInFiles("tests/algorithms/TestFiles/FourPosTestFiles/BruteForceTests", new FourPositionWagnerWolff());
    }

    @TestFactory
    public Collection<DynamicTest> SliderTest() {
        return readInFiles("tests/algorithms/TestFiles/SliderTestFiles", new GreedySliderAlgorithm());
    }
}
