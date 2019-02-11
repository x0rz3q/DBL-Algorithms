import java.io.*;
import java.lang.*;
import java.util.*;
import org.apache.commons.math3.*;
import org.apache.commons.math3.distribution.*;

import javax.sound.midi.ControllerEventListener;


// Stores point
class Point {
    int x;
    int y;

    Point (int x, int y) {
        this.x = x;
        this.y = y;
    }
}

// Stores rectangle
class Rectangle {
    Point sw;
    Point ne;
    Point associated;

    Rectangle (Point southWest, Point northEast, Point associatedPoint) {
        this.sw = southWest;
        this.ne = northEast;
        this.associated = associatedPoint;
    }
}


// Allows for writing to file
class Printer {
    FileWriter fileWriter;
    PrintWriter printWriter;

    // prints the problem statement: model, aspect ratio and number of points to the specified file
    void printBasics(String model, double ratio, int n) {
        printWriter.println("placement model: " + model);
        printWriter.println("aspect ratio: " + ratio);
        printWriter.println("number of points: " + n);
    }

    // prints the coordinates of the generated points to the specified file
    void printPoints(Point[] points) {
        for (Point p : points) {
            printWriter.println(p.x + " " + p.y);
        }
    }

    // prints the correct result to the specified file
    void printResult(double result) {
        printWriter.println("label height: " + result);
        printWriter.close();
    }

    /**
     * Constructor for printer to file
     * @param fileLocation
     * @throws IOException
     */
    Printer(String fileLocation) throws IOException {
        this.fileWriter = new FileWriter(fileLocation);
        this.printWriter = new PrintWriter(fileWriter);
    }
}


// Abstract number generator
abstract class NumberGenerator {
    AbstractIntegerDistribution dist;
    // TODO write contract
    int sample() {
        return dist.sample();
    }
}

// Concrete number generator based on uniform distribution
class UniformNumberGenerator extends NumberGenerator {
    /**
     * Constructor for UniformNumberGenerator
     * @pre upper > lower
     * @param lower lower bound for Uniform distribution
     * @param upper upper bound for Uniform distribution
     */
    UniformNumberGenerator(int lower, int upper) {
        this.dist = new UniformIntegerDistribution(lower, upper);
    }
}

// Concrete number generator based on Poisson distribution
class PoissonNumberGenerator extends NumberGenerator {
    /**
     * Constructor for PoissonNumberGenerator
     * @pre p > 0
     * @param p mean & variance
     */
    PoissonNumberGenerator(double p) {
        this.dist = new PoissonDistribution(p);
    }
}

// Concrete number generator based on Geometric distribution
class GeometricNumberGenerator extends NumberGenerator {
    /**
     * Constructor for GeometricNumberGenerator
     * @pre 0 <= p <= 1
     * @param p success probability
     */
    GeometricNumberGenerator(double p) {
        this.dist = new GeometricDistribution(p);
    }
}

// Concrete number generator based on Binomial distribution
class BinomialNumberGenerator extends NumberGenerator {
    /**
     * Constructor for BinomialNumberGenerator
     * @pre 0 <= p <= 1 && n > 0
     * @param p success probability per trial
     * @param n number of trials
     */
    BinomialNumberGenerator(double p, int n) {
        this.dist = new BinomialDistribution(n, p);
    }
}



// Abstract generation strategy
abstract class GenerationStrategy {
    TestData data;
    Random rand = new Random();

    // TODO provide contract
    abstract Point[] generate();
    // TODO provide contract
    abstract Rectangle[] generateStart;
    // TODO provide contract

}

// Concrete generation strategy for 2pos
class Strategy2pos extends GenerationStrategy {
    @Override
    Point[] generate() {
        generateStart();
        // TODO implement
    }

    @Override
    Rectangle[] generateStart {
        // TODO implement
    }

    Strategy2pos(TestData data) { this.data = data; }
}

// Concrete generation strategy for 4pos
class Strategy4pos extends GenerationStrategy {
    @Override
    Point[] generate() {
        // TODO implement
    }

    @Override
    Rectangle[] generateStart {
        // TODO implement
    }

    Strategy4pos(TestData data) {
        this.data = data;
    }
}

// Concrete generation strategy for 1slider
class Strategy1slider extends GenerationStrategy {
    @Override
    Point[] generate() {
        // TODO implement
    }

    @Override
    Rectangle[] generateStart {
        // TODO implement
    }

    Strategy1slider(TestData data) {
        this.data = data;
    }
}

// data structure containing all test variables
class TestData {
    // model: 2pos, 4pos or 1slider
    String model;

    // number of points/labels
    int n;

    // aspect ratio label
    double ratio;

    /* correct result (maximum height)
    *   2pos: result == integer || result * alpha * 2 == integer
    *   4pos: result * 2 == integer || result * alpha * 2 == integer
    *   1slider: result == real
    */
    double result;

    // Number Generators in the x- and y-dimensions
    NumberGenerator xGenerator;
    NumberGenerator yGenerator;

    // location for generated file
    String location;

    // Setters
    void setModel(String newModel) {this.model = newModel;}
    void setN(int newN) {this.n = newN;}
    void setRatio(double newRatio) {this.ratio = newRatio;}
    void setResult(double newResult) {this.result = newResult;}
    void setxGenerator(NumberGenerator newxGenerator) {this.xGenerator = newxGenerator;}
    void setyGenerator(NumberGenerator newyGenerator) {this.yGenerator = newyGenerator;}
    void setLocation(String newLocation) {this.location = newLocation;}
}

// controller for generation processs of test
class Controller {
    // object containing all data for single test
    private TestData data;
    // object for to file printing
    private Printer printer;

    private GenerationStrategy strategy;


    /**
     * generates a test in the given file location
     * @pre data != null (including all fields) && printer != null;
     * @post file at specified location contains test for data specified, with correct result at the end
     */
    void generate() {
        printer.printBasics(data.model, data.ratio, data.n);
        chooseStrategy();
        Point[] points = strategy.generate();
        printer.printPoints(points);
        printer.printResult(data.result);
    }

    /**
     * chooses the strategy to use based on the model
     * @pre data != null (including all fields)
     * @post Controller.strategy is set according to the model
     * @throws IllegalArgumentException
     */
    void chooseStrategy() throws IllegalArgumentException {
        if (data.model == "2pos") {
            strategy = new Strategy2pos(data);
        } else if (data.model == "4pos") {
            strategy = new Strategy4pos(data);
        } else if (data.model == "1slider") {
            strategy = new Strategy1slider(data);
        } else {
            throw new IllegalArgumentException("Non-existent model specified");
        }
    }

    Controller(TestData data) throws IOException {
        this.data = data;
        this.printer = new Printer(data.location);
    }


}


public class Main {
    public static void main(String[] args) throws IOException {
        TestData data;
        // TODO set variables (optionally automatically via file)

        Controller controller = new Controller(data);
        controller.generate();

    }
}
