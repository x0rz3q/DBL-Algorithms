import Collections.QuadTree;
import models.Point;
import models.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.ceil;

// Abstract generation strategy
abstract class GenerationStrategy {
    TestData data;
    QuadTree tree = new QuadTree();
    QuadTree pointsTree = new QuadTree();
    ArrayList<Rectangle> rectangles = new ArrayList<>();

    Random rand = new Random();
    double height;
    double width;

    /**
     * Generates testcase fullfiling the requirements as set in the 'data' TestData record
     * @pre data != null && data == valid
     * @return array of test-points
     */
    Point[] generate() {
        int counter = 0;
        double width = data.result * data.ratio;

        // generate rectangles & points determining final size
        generateStart();

        while (rectangles.size() < data.n && counter < data.n * 1e5) {
            counter++;

            // select candidate point
            Point candidate = new Point(data.xGenerator.sample(0, 10000), data.yGenerator.sample(0, 10000));
            while (pointsTree.query2D(new Rectangle(candidate.getX() - 0.5, candidate.getY() - 0.5, candidate.getX() + 0.5, candidate.getY() + 0.5)).size() > 0) {
                candidate = new Point(data.xGenerator.sample((int) ceil(width), (int) (10000 - ceil(width))), data.yGenerator.sample((int) ceil(width), (int) (10000 - ceil(width))));
            }

            // generate corresponding rectangle
            Rectangle candidateRectangle = generateCandidateRectangle(candidate);

            // check whether candidate is possible
            if (tree.query2D(candidateRectangle).size() == 0) {
                fullInsert(candidateRectangle);
            }
        }

        // return points
        Point[] associatedPoints = new Point[rectangles.size()];
        for (int i = 0; i < rectangles.size(); i++) {
            associatedPoints[i] = (Point) rectangles.get(i).getPoI();
        }

        return associatedPoints;
    }


    /**
     * Generates starting set of rectangles determining the optimal height
     */
    abstract void generateStart();

    /**
     * Generates a possible rectangle associated with the specified point
     * @param candidate point for which a rectangle is to be generated
     * @return generated Rectangle object
     */
    abstract Rectangle generateCandidateRectangle(Point candidate);

    /**
     * Adds the specified Rectangle object 'rect' to all datastructures.
     * @pre rect != null && rect.getPoI() != null
     * @param rect
     * @post rectangles.contains(rect) && tree.contains(rect) && pointsTree.contains(rect.getPoI())
     */
    void fullInsert(Rectangle rect) {
        rectangles.add(rect);
        tree.insert(rect);
        pointsTree.insert(rect.getPoI());
    }

}
