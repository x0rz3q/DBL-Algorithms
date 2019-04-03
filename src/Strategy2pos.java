import Collections.QuadTree;
import models.Point;
import models.Rectangle;

import java.util.ArrayList;

import static java.lang.Math.ceil;

// Concrete generation strategy for 2pos
class Strategy2pos extends GenerationStrategy {
    @Override
    Rectangle generateCandidateRectangle(Point candidate) {
        boolean useLeft = rand.nextBoolean();

        if (useLeft) {
            return new Rectangle(candidate.getX() - width, candidate.getY(), candidate.getX(), candidate.getY() + height, candidate);
        } else {
            return new Rectangle(candidate.getX(), candidate.getY(), candidate.getX() + width, candidate.getY() + height, candidate);
        }
    }

    @Override
    void generateStart() {
        // Constructing initial point and rectangles
        Point startPoint = new Point(data.xGenerator.sample(3 * (int) ceil(width), (int) (10000 - 3 * ceil(width))), data.yGenerator.sample((int) ceil(height), (int) (10000 - ceil(height))));
        Rectangle rightRectangle = new Rectangle(startPoint.getX(), startPoint.getY(), startPoint.getX() + width, startPoint.getY() + height, startPoint);
        Rectangle leftRectangle = new Rectangle(startPoint.getX() - width, startPoint.getY(), startPoint.getX(), startPoint.getY() + height, startPoint);

        // invalidate left rectangle
        Point[] internalLeft = leftRectangle.getInternal();
        int randIndex = rand.nextInt(internalLeft.length);
        Point invalidator = internalLeft[randIndex];
        // create corresponding rectangle invalidator
        Rectangle invalidatorRectangle = new Rectangle(invalidator.getX() - width, invalidator.getY(), invalidator.getX(), invalidator.getY() + height, invalidator);

        // lock in right rectangle
        Point[] boundaryRight = rightRectangle.getBoundaryStrict(true, true, false, false);
        randIndex = rand.nextInt(boundaryRight.length);
        Point blocker = boundaryRight[randIndex];
        // create corresponding rectangle blocker
        Rectangle blockerRectangle = new Rectangle(blocker.getX(), blocker.getY(), blocker.getX() + width, blocker.getY() + height, blocker);

        // Add constructed rectangles to rectangles
        rectangles.add(rightRectangle);
        rectangles.add(invalidatorRectangle);
        rectangles.add(blockerRectangle);

        // Add rectangles to tree
        tree.insert(rightRectangle);
        tree.insert(invalidatorRectangle);
        tree.insert(blockerRectangle);

        // Add points to pointsTree
        pointsTree.insert(startPoint);
        pointsTree.insert(invalidator);
        pointsTree.insert(blocker);
    }


    Strategy2pos(TestData data) {
        this.data = data;
        this.height = data.result;
        this.width = data.ratio * data.result;
    }
}
