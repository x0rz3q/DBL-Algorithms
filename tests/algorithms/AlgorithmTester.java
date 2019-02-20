package algorithms;

import Collections.QuadTree;
import Parser.DataRecord;
import Parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

        assertEquals(record.height, optHeight, "the height found is not correct in file: " + fileName
                + " expected: " + optHeight + " returned: " + record.height);

    }


    private void readInFiles(String filePath) throws IOException, NullPointerException {
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        Parser parser = new Parser();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                DataRecord record = parser.input(new FileInputStream(listOfFiles[i]), QuadTree.class);
                runTest(record, listOfFiles[i].getName(), new Double(10));
            }
        }
    }


    @Test
    public void TwoPosTest() {

    }
}
