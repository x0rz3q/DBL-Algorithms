import java.io.*;
import java.lang.*;
import java.util.*;

import org.apache.commons.math3.distribution.*;


public class Main {
    public static void main(String[] args) throws IOException {


        // ADAPT LOCATION HERE ------------------------------------------------------------------------
        String testCaseLocation = "TestCaseSpecification.txt";
        // ADAPT LOCATION BEFORE HERE -----------------------------------------------------------------

        TestReader reader = new TestReader(testCaseLocation);
        Controller controller = new Controller();

        ArrayList<TestData> tests = reader.getTests();
        for (TestData test : tests) {
            controller.setData(test);
            controller.generate();
        }

    }
}
