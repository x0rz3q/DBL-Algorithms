package algorithms;

import Collections.QuadTree;
import Parser.DataRecord;
import Parser.Parser;
import javafx.util.Pair;
import main.Interpreter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmTester {

    private void runTest(DataRecord record, String fileName, Double optHeight) {

        switch (record.placementModel) {
            case ONE_SLIDER:
                (new GreedySliderAlgorithm()).solve(record);
                break;
            case TWO_POS:
                (new TwoPositionBinarySearcher()).solve(record);
                break;
            case FOUR_POS:
                throw new UnsupportedOperationException("No 4-pos algorithm implemented yet ");
        }

        assertEquals(optHeight * 1.0, (record.height / record.aspectRatio), "the height found is not correct in file: " + fileName);
        assertTrue(Interpreter.isValid(record), "the solution found is not valid in file: " + fileName);

    }


    private void readInFiles(String filePath) throws IOException, NullPointerException {
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        Parser parser = new Parser();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Pair<DataRecord, Double> input = parser.inputTestMode(new FileInputStream(listOfFiles[i]), QuadTree.class);
                runTest(input.getKey(), listOfFiles[i].getName(), input.getValue());
            }
        }
    }


    @Test
    public void TwoPosTest() {
        try {
            readInFiles("tests/algorithms/TestFiles/TwoPosTestFiles");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void FourPosTest() {
        try {
            readInFiles("tests/algorithms/TestFiles/FourPosTestFiles");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void SliderTest() {
        try {
            readInFiles("tests/algorithms/TestFiles/SliderTestFiles");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
