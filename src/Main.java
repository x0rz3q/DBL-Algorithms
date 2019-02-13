import java.io.*;
import java.lang.*;
import java.util.*;
import org.apache.commons.math3.*;
import org.apache.commons.math3.distribution.*;

import javax.sound.midi.ControllerEventListener;


// Stores point
class Point {
    double x;
    double y;

    Point (double x, double y) {
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

    /**
     * Method for checking whether rectangle collides with other rectangle
     * @pre target and object rectangle same size
     * @param target
     * @return true iff target and object rectangle (partially) coincide
     */
    boolean checkCollision(Rectangle target) {
        if (Math.max(this.sw.x, target.sw.x) < Math.min(this.ne.x, target.ne.x)) {
            return true;
        }
        if (Math.max(this.sw.y, target.sw.y) < Math.min(this.ne.y, target.ne.y)) {
            return true;
        }
        return false;
    }

    /**
     * Method for finding all integer coordinate-points inside a rectangle
     * @pre sw & ne defined & sw.x < ne.x & sw.y < ne.y
     * @return array of all points with integer coordinates inside rectangle object
     */
    Point[] getInternal() {
        ArrayList<Point> internalPoints = new ArrayList<>();
        int xLowerBound;
        int xUpperBound;
        int yLowerBound;
        int yUpperBound;
        if (this.sw.x == Math.ceil(this.sw.x)) { xLowerBound = (int) this.sw.x + 1; } else { xLowerBound = (int) Math.ceil(this.sw.x); }
        if (this.ne.x == Math.floor(this.ne.x)) { xUpperBound = (int) this.ne.x - 1; } else { xUpperBound = (int) Math.floor(this.ne.x); }
        if (this.sw.y == Math.ceil(this.sw.y)) { yLowerBound = (int) this.sw.y + 1; } else { yLowerBound = (int) Math.ceil(this.sw.y); }
        if (this.ne.y == Math.floor(this.ne.y)) { yUpperBound = (int) this.ne.y - 1; } else { yUpperBound = (int) Math.floor(this.ne.y); }

        for (int x = xLowerBound; x <= xUpperBound; x++) {
            for (int y = yLowerBound; y <= yUpperBound; y++) {
                internalPoints.add(new Point(x, y));
            }
        }

        Point[] internalPointArray = new Point[internalPoints.size()];
        internalPointArray = internalPoints.toArray(internalPointArray);
        return internalPointArray;
    }

    /**
     * Returns the integer boundary points on the specified sides
     * @param north
     * @param east
     * @param south
     * @param west
     * @return array of points containing the integer points on the specified boundaries
     */
    Point[] getBoundary(boolean north, boolean east, boolean south, boolean west) {
        ArrayList<Point> boundaryPoints = new ArrayList<>();
        int xLowerBound = (int) Math.ceil(this.sw.x);
        int xUpperBound = (int) Math.floor(this.ne.x);
        int yLowerBound = (int) Math.ceil(this.sw.y);
        int yUpperBound = (int) Math.floor(this.ne.y);

        if (north && (this.ne.y) == Math.floor(this.ne.y)) {
            for (int x = xLowerBound; x <= xUpperBound; x++) {
                boundaryPoints.add(new Point(x, this.ne.y));
            }
        }
        if (east && (this.ne.x) == Math.floor(this.ne.x)) {
            for (int y = yLowerBound; y <= yUpperBound; y++) {
                boundaryPoints.add(new Point(this.ne.x, y));
            }
        }
        if (south && (this.sw.y) == Math.floor(this.sw.y)) {
            for (int x = xLowerBound; x <= xUpperBound; x++) {
                boundaryPoints.add(new Point(x, this.sw.y));
            }
        }
        if (west && (this.sw.x) == Math.floor(this.sw.x)) {
            for (int y = yLowerBound; y <= yUpperBound; y++) {
                boundaryPoints.add(new Point(this.sw.x, y));
            }
        }

        Point[] boundaryPointArray = new Point[boundaryPoints.size()];
        boundaryPointArray = boundaryPoints.toArray(boundaryPointArray);
        return boundaryPointArray;
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
        Collections.shuffle(Arrays.asList(points));
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
    abstract Rectangle[] generateStart();
    // TODO provide contract

}

// Concrete generation strategy for 2pos
class Strategy2pos extends GenerationStrategy {
    @Override
    Point[] generate() {
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        // Add starting rectangles
        Rectangle[] startingRectangles = generateStart();
        for (Rectangle r : startingRectangles) {
            rectangles.add(r);
        }
        int counter = 0;
        double width = data.result * data.ratio;
        while (rectangles.size() < data.n && counter < data.n * 10000) {
            counter++;

            Point candidate = new Point(data.xGenerator.sample(), data.yGenerator.sample());
            boolean useLeft = rand.nextBoolean();
            if (useLeft) {
                // Check left
                Rectangle candidateRectangle = new Rectangle(new Point(candidate.x - width, candidate.y), new Point(candidate.x, candidate.y + data.result), candidate);
                boolean collision = false;
                for (Rectangle r : rectangles) {
                    if (candidateRectangle.checkCollision(r)) {
                        collision = true;
                        break;
                    }
                }
                if (!collision) {
                    rectangles.add(candidateRectangle);
                    continue;
                }

                // Then right
                candidateRectangle = new Rectangle(candidate, new Point(candidate.x + width, candidate.y + data.result), candidate);
                collision = false;
                for (Rectangle r : rectangles) {
                    if (candidateRectangle.checkCollision(r)) {
                        collision = true;
                        break;
                    }
                }
                if (!collision) {
                    rectangles.add(candidateRectangle);
                    continue;
                }
            } else {
                // Check right
                Rectangle candidateRectangle = new Rectangle(candidate, new Point(candidate.x + width, candidate.y + data.result), candidate);
                boolean collision = false;
                for (Rectangle r : rectangles) {
                    if (candidateRectangle.checkCollision(r)) {
                        collision = true;
                        break;
                    }
                }
                if (!collision) {
                    rectangles.add(candidateRectangle);
                    continue;
                }

                // Then left
                candidateRectangle = new Rectangle(new Point(candidate.x - width, candidate.y), new Point(candidate.x, candidate.y + data.result), candidate);
                collision = false;
                for (Rectangle r : rectangles) {
                    if (candidateRectangle.checkCollision(r)) {
                        collision = true;
                        break;
                    }
                }
                if (!collision) {
                    rectangles.add(candidateRectangle);
                    continue;
                }
            }
        }

        Point[] associatedPoints = new Point[rectangles.size()];
        for (int i = 0; i < rectangles.size(); i++) {
            associatedPoints[i] = rectangles.get(i).associated;
        }

    }

    @Override
    Rectangle[] generateStart() {
        // ArrayList storing rectangles to be returned
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        // width of rectangle
        double width = data.result * data.ratio;

        // initial point
        Point startPoint = new Point(data.xGenerator.sample(), data.yGenerator.sample());
        // Possible labels for initial point
        Rectangle leftRectangle = new Rectangle(new Point(startPoint.x - width, startPoint.y), new Point(startPoint.x, startPoint.y + data.result), startPoint);
        Rectangle rightRectangle = new Rectangle(new Point(startPoint.x, startPoint.y), new Point(startPoint.x + width, startPoint.y + data.result), startPoint);

        boolean useLeft = rand.nextBoolean();
        if (useLeft) {
            rectangles.add(leftRectangle);

            // Constructing rectangle making right option invalid
            Point[] internalRight = rightRectangle.getInternal();
            int randIndex = rand.nextInt(internalRight.length);
            Point invalidator = internalRight[randIndex];
            rectangles.add(new Rectangle(invalidator, new Point(invalidator.x + width, invalidator.y + data.result), invalidator));

            // Constructing rectangle limiting size of starting rectangle
            Point[] boundaryLeft = leftRectangle.getBoundary(true,false, false, true);
            if (boundaryLeft.length == 0) {
                // must lock in with two rectangles
                Point lockPoint = new Point(startPoint.x - 2 * width, startPoint.y);
                rectangles.add(new Rectangle(lockPoint, new Point(startPoint.x - width, startPoint.y + data.result), lockPoint));

                // Construct final blocker
                Point[] finalBlockerOptions = (new Rectangle(new Point(lockPoint.x - width, lockPoint.y), new Point(lockPoint.x, lockPoint.y + data.result), lockPoint)).getInternal();
                randIndex = rand.nextInt(finalBlockerOptions.length);
                Point finalBlocker = finalBlockerOptions[randIndex];
                rectangles.add(new Rectangle(new Point(finalBlocker.x - width, finalBlocker.y), new Point(finalBlocker.x, finalBlocker.y + data.result), finalBlocker));
            } else {
                randIndex = rand.nextInt(boundaryLeft.length);
                Point blocker = boundaryLeft[randIndex];
                while (blocker.x == startPoint.x) {
                    randIndex = rand.nextInt(boundaryLeft.length);
                    blocker = boundaryLeft[randIndex];
                }

                if (blocker.y < leftRectangle.ne.y) {
                    rectangles.add(new Rectangle(new Point(blocker.x - width, blocker.y), new Point(blocker.x, blocker.y + data.result), blocker));
                } else {
                    useLeft = rand.nextBoolean();
                    if (useLeft) {
                        rectangles.add(new Rectangle(new Point(blocker.x - width, blocker.y), new Point(blocker.x, blocker.y + data.result), blocker));
                    } else {
                        rectangles.add(new Rectangle(new Point(blocker.x, blocker.y), new Point(blocker.x + width, blocker.y + data.result), blocker));
                    }
                }
            }
        } else {
            rectangles.add(rightRectangle);

            // Constructing rectangle making left option invalid
            Point[] internalLeft = leftRectangle.getInternal();
            int randIndex = rand.nextInt(internalLeft.length);
            Point invalidator = internalLeft[randIndex];
            rectangles.add(new Rectangle(new Point(invalidator.x -width, invalidator.y), new Point(invalidator.x, invalidator.y + data.result), invalidator));

            // Constructing rectangle limiting size of starting rectangle
            Point[] boundaryRight = rightRectangle.getBoundary(true,true, false, false);
            if (boundaryRight.length == 0) {
                // must lock in with two rectangles
                Point lockPoint = new Point(startPoint.x + 2 * width, startPoint.y);
                rectangles.add(new Rectangle(new Point(startPoint.x + width, startPoint.y), new Point(startPoint.x + 2 * width, startPoint.y + data.result), lockPoint));

                // Construct final blocker
                Point[] finalBlockerOptions = (new Rectangle(lockPoint, new Point(lockPoint.x + width, lockPoint.y + data.result), lockPoint)).getInternal();
                randIndex = rand.nextInt(finalBlockerOptions.length);
                Point finalBlocker = finalBlockerOptions[randIndex];
                rectangles.add(new Rectangle(finalBlocker, new Point(finalBlocker.x + width, finalBlocker.y + data.result), finalBlocker));
            } else {
                randIndex = rand.nextInt(boundaryRight.length);
                Point blocker = boundaryRight[randIndex];
                while (blocker.x == startPoint.x) {
                    randIndex = rand.nextInt(boundaryRight.length);
                    blocker = boundaryRight[randIndex];
                }

                if (blocker.y < leftRectangle.ne.y) {
                    rectangles.add(new Rectangle(blocker, new Point(blocker.x + width, blocker.y + data.result), blocker));
                } else {
                    useLeft = rand.nextBoolean();
                    if (useLeft) {
                        rectangles.add(new Rectangle(new Point(blocker.x - width, blocker.y), new Point(blocker.x, blocker.y + data.result), blocker));
                    } else {
                        rectangles.add(new Rectangle(new Point(blocker.x, blocker.y), new Point(blocker.x + width, blocker.y + data.result), blocker));
                    }
                }
            }
        }

        Rectangle[] rectangleArray = new Rectangle[rectangles.size()];
        rectangleArray = rectangles.toArray(rectangleArray);
        return rectangleArray;

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
    Rectangle[] generateStart() {
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
    Rectangle[] generateStart() {
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
