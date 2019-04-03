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

    // TODO provide contract
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
                rectangles.add(candidateRectangle);
                tree.insert(candidateRectangle);
                pointsTree.insert(candidate);
            }
        }

        // return points
        Point[] associatedPoints = new Point[rectangles.size()];
        for (int i = 0; i < rectangles.size(); i++) {
            associatedPoints[i] = (Point) rectangles.get(i).getPoI();
        }

        return associatedPoints;
    }


    // TODO provide contract
    abstract void generateStart();

    // TODO provide contract
    abstract Rectangle generateCandidateRectangle(Point candidate);

    /**
     * Creates Array containing elements of returnArrayList and returns it
     *
     * @param returnArrayList
     * @return array containing elements of returnArrayList
     */
    Rectangle[] toArray(ArrayList<Rectangle> returnArrayList) {
        Rectangle[] returnArray = new Rectangle[returnArrayList.size()];
        returnArray = returnArrayList.toArray(returnArray);
        return returnArray;
    }

}
