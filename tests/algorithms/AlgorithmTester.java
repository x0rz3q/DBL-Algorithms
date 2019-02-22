package algorithms;

import Collections.QuadTree;
import Parser.DataRecord;
import Parser.Parser;
import Parser.Pair;
import interfaces.AbstractAlgorithmInterface;
import main.Interpreter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTester {

    private void runTest(DataRecord record, String fileName, Double optHeight, AbstractAlgorithmInterface algorithms) {

        algorithms.solve(record);

        assertTrue(Math.abs((record.height / record.aspectRatio) - optHeight) < 0.1, "the height found is not correct in file: " + fileName);
        assertTrue(Interpreter.isValid(record), "the solution found is not valid in file: " + fileName);

    }


    private void readInFiles(String filePath, AbstractAlgorithmInterface algorithm) throws IOException, NullPointerException {
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        Parser parser = new Parser();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Pair<DataRecord, Double> input = parser.inputTestMode(new FileInputStream(listOfFiles[i]), QuadTree.class);
                runTest(input.getKey(), listOfFiles[i].getName(), input.getValue(), algorithm);
            }
        }
    }


    @Test
    public void TwoPosTest() {
        try {
            readInFiles("tests/algorithms/TestFiles/TwoPosTestFiles", new TwoPositionBinarySearcher());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void FourPosTest() {
        try {
            readInFiles("tests/algorithms/TestFiles/FourPosTestFiles", new TwoPositionBinarySearcher());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void SliderTest() {
        try {
            readInFiles("tests/algorithms/TestFiles/SliderTestFiles", new GreedySliderAlgorithm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
