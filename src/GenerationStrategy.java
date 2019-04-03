import Collections.QuadTree;
import models.Point;
import models.Rectangle;

import java.util.ArrayList;
import java.util.Random;

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
    abstract Point[] generate();
    // TODO provide contract
    abstract Rectangle[] generateStart();
    // TODO provide contract

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
