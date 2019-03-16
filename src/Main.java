import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException {


        // ADAPT LOCATION HERE ------------------------------------------------------------------------
        // String testCaseLocation = "/home/juris/Uni/DBL-Algorithms/TestCaseSpecification.txt";
        String testCaseLocation = "/home/juris/Uni/DBL-Algorithms/TestCaseSpecification.txt";
        // ADAPT LOCATION BEFORE HERE -----------------------------------------------------------------

        TestReader reader = new TestReader(testCaseLocation);
        Controller controller = new Controller();

        ArrayList<TestData> tests = reader.getTests();
        for (TestData test : tests) {
            System.out.println(test.toString());
            controller.setData(test);
            controller.generate();
        }

    }
}
