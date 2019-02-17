import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class TestReader {
    File readFile;
    Scanner sc;

    TestReader(String fileLocation) throws FileNotFoundException {
        readFile = new File(fileLocation);
        sc = new Scanner(readFile);
    }

    /**
     * Method for parsing TestGenerator file into ArrayList of data for tests
     * @return ArrayList<TestData> containing data for individual tests
     * @throws IllegalArgumentException if invalid coordinate distribution is specified
     */
    ArrayList<TestData> getTests() throws  IllegalArgumentException {
        ArrayList<TestData> tests = new ArrayList<>();
        while(sc.hasNextLine()) {
            TestData currentTest = new TestData();

            currentTest.setModel(sc.next());
            currentTest.setN(sc.nextInt());
            currentTest.setRatio(sc.nextDouble());
            currentTest.setResult(sc.nextDouble());

            String xGeneratorType = sc.next();
            if (xGeneratorType.equals("Uniform")) {
                currentTest.setxGenerator(new UniformNumberGenerator(sc.nextInt(), sc.nextInt()));
            } else if (xGeneratorType.equals("Poisson")) {
                currentTest.setxGenerator(new PoissonNumberGenerator(sc.nextDouble()));
            } else if (xGeneratorType.equals("Geometric")) {
                currentTest.setxGenerator(new GeometricNumberGenerator(sc.nextDouble()));
            } else if (xGeneratorType.equals("Binomial")) {
                currentTest.setxGenerator(new BinomialNumberGenerator(sc.nextDouble(), sc.nextInt()));
            } else {
                throw new IllegalArgumentException("Invalid numbergenerator");
            }

            String yGeneratorType = sc.next();
            if (yGeneratorType.equals("Uniform")) {
                currentTest.setyGenerator(new UniformNumberGenerator(sc.nextInt(), sc.nextInt()));
            } else if (yGeneratorType.equals("Poisson")) {
                currentTest.setyGenerator(new PoissonNumberGenerator(sc.nextDouble()));
            } else if (yGeneratorType.equals("Geometric")) {
                currentTest.setyGenerator(new GeometricNumberGenerator(sc.nextDouble()));
            } else if (yGeneratorType.equals("Binomial")) {
                currentTest.setyGenerator(new BinomialNumberGenerator(sc.nextDouble(), sc.nextInt()));
            } else {
                throw new IllegalArgumentException("Invalid numbergenerator");
            }

            tests.add(currentTest);
        }

        return tests;
    }
}
