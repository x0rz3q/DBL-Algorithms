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
     * Method for checking input style of TestCaseSpecification. Returns type as boolean
     * @return true if complex style, false if simple style
     * @throws IllegalArgumentException if invalid type is specified
     */
    boolean determineInputStyle() throws IllegalArgumentException {
        String inputStyle = sc.next();
        if (inputStyle.equals("complex")) {
            return true;
        } else if (inputStyle.equals("simple")) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid input style (use simple or complex)");
        }
    }

    /**
     * Method for parsing TestGenerator file in complex style into ArrayList of data for tests
     * @return ArrayList<TestData> containing data for individual tests
     * @throws IllegalArgumentException if invalid coordinate distribution is specified
     */
    ArrayList<TestData> getTestsComplex() throws  IllegalArgumentException {
        ArrayList<TestData> tests = new ArrayList<>();
        while(sc.hasNextLine()) {
            if (!sc.hasNext()) {
                break;
            }
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

    /**
     * Method for parsing TestGenerator file in simple style into ArrayList of data for tests
     * @return ArrayList<TestData> containing data for individual tests
     * @throws IllegalArgumentException if invalid coordinate distribution is specified
     */
    ArrayList<TestData> getTestsSimple() {
        ArrayList<TestData> tests = new ArrayList<>();
        while(sc.hasNextLine()) {
            if (!sc.hasNext()) {
                break;
            }
            TestData currentTest = new TestData();

            currentTest.setModel(sc.next());

            String difficulty = sc.next();
            double areaRatio;
            if (difficulty.equals("easy")) {
                areaRatio = 10.0 / 2.0;
            } else if (difficulty.equals("medium")) {
                areaRatio = 10.0 / 3.0;
            } else if (difficulty.equals("hard")) {
                areaRatio = 10.0 / 4.0;
            } else if (difficulty.equals("custom")) {
                areaRatio = 1 / sc.nextDouble();
            } else {
                throw new IllegalArgumentException("Invalid difficulty (easy/medium/hard)");
            }

            currentTest.setN(sc.nextInt());
            currentTest.setRatio(2);
            currentTest.setResult(10);

            int coverage = currentTest.n * 200;
            double area = coverage * areaRatio;
            int range = (int) Math.ceil(Math.sqrt(area));

            currentTest.setxGenerator(new UniformNumberGenerator(0, range));
            currentTest.setyGenerator(new UniformNumberGenerator(0, range));


            tests.add(currentTest);
        }

        return tests;
    }
}
