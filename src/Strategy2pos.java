import Collections.QuadTree;
import models.Point;
import models.Rectangle;

import java.util.ArrayList;

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

        QuadTree tree = new QuadTree(new Rectangle(0, 0, 100000, 100000, new Point(0, 0)));
        QuadTree pointsTree = new QuadTree(new Rectangle(0, 0, 100000, 100000));
        for (Rectangle r : rectangles) {
            tree.insert(r);
            pointsTree.insert(new Rectangle(r.getPoI(), r.getPoI(), r.getPoI()));
        }

        while (rectangles.size() < data.n && counter < data.n * 100000) {
            counter++;

            Point candidate = new Point(data.xGenerator.sample(), data.yGenerator.sample());
            while (pointsTree.query2D(new Rectangle(candidate.getX() - 0.5, candidate.getY() - 0.5, candidate.getX() + 0.5, candidate.getY() + 0.5)).size() > 0) {
                candidate = new Point(data.xGenerator.sample(), data.yGenerator.sample());
            }
            boolean useLeft = rand.nextBoolean();
            if (useLeft) {
                // Check left
                Rectangle candidateRectangle = new Rectangle(new Point(candidate.getX() - width, candidate.getY()), new Point(candidate.getX(), candidate.getY() + data.result), candidate);

                if (tree.query2D(candidateRectangle).size() == 0) {
                    rectangles.add(candidateRectangle);
                    tree.insert(candidateRectangle);
                    pointsTree.insert(new Rectangle(candidate, candidate, candidate));
                    continue;
                }

                // Then right
                candidateRectangle = new Rectangle(candidate, new Point(candidate.getX() + width, candidate.getY() + data.result), candidate);

                if (tree.query2D(candidateRectangle).size() == 0) {
                    rectangles.add(candidateRectangle);
                    tree.insert(candidateRectangle);
                    pointsTree.insert(new Rectangle(candidate, candidate, candidate));
                    continue;
                }

            } else {
                // Check right
                Rectangle candidateRectangle = new Rectangle(candidate, new Point(candidate.getX() + width, candidate.getY() + data.result), candidate);

                if (tree.query2D(candidateRectangle).size() == 0) {
                    rectangles.add(candidateRectangle);
                    tree.insert(candidateRectangle);
                    pointsTree.insert(new Rectangle(candidate, candidate, candidate));
                    continue;
                }

                // Then left
                candidateRectangle = new Rectangle(new Point(candidate.getX() - width, candidate.getY()), new Point(candidate.getX(), candidate.getY() + data.result), candidate);

                if (tree.query2D(candidateRectangle).size() == 0) {
                    rectangles.add(candidateRectangle);
                    tree.insert(candidateRectangle);
                    pointsTree.insert(new Rectangle(candidate, candidate, candidate));
                    continue;
                }
            }
        }

        Point[] associatedPoints = new Point[rectangles.size()];
        for (int i = 0; i < rectangles.size(); i++) {
            associatedPoints[i] = (Point) rectangles.get(i).getPoI();
        }

        return associatedPoints;
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
        Rectangle leftRectangle = new Rectangle(new Point(startPoint.getX() - width, startPoint.getY()), new Point(startPoint.getX(), startPoint.getY() + data.result), startPoint);
        Rectangle rightRectangle = new Rectangle(new Point(startPoint.getX(), startPoint.getY()), new Point(startPoint.getX() + width, startPoint.getY() + data.result), startPoint);

        boolean useLeft = rand.nextBoolean();
        if (useLeft) {
            rectangles.add(leftRectangle);

            // Constructing rectangle making right option invalid
            Point[] internalRight = rightRectangle.getInternal();
            int randIndex = rand.nextInt(internalRight.length);
            Point invalidator = internalRight[randIndex];
            rectangles.add(new Rectangle(invalidator, new Point(invalidator.getX() + width, invalidator.getY() + data.result), invalidator));

            // Constructing rectangle limiting size of starting rectangle
            Point[] boundaryLeft = leftRectangle.getBoundary(true,false, false, true);
            if (boundaryLeft.length == 0) {
                // must lock in with two rectangles
                Point lockPoint = new Point(startPoint.getX() - 2 * width, startPoint.getY());
                rectangles.add(new Rectangle(lockPoint, new Point(startPoint.getX() - width, startPoint.getY() + data.result), lockPoint));

                // Construct final blocker
                Point[] finalBlockerOptions = (new Rectangle(new Point(lockPoint.getX() - width, lockPoint.getY()), new Point(lockPoint.getX(), lockPoint.getY() + data.result), lockPoint)).getInternal();
                randIndex = rand.nextInt(finalBlockerOptions.length);
                Point finalBlocker = finalBlockerOptions[randIndex];
                rectangles.add(new Rectangle(new Point(finalBlocker.getX() - width, finalBlocker.getY()), new Point(finalBlocker.getX(), finalBlocker.getY() + data.result), finalBlocker));
            } else {
                randIndex = rand.nextInt(boundaryLeft.length);
                Point blocker = boundaryLeft[randIndex];
                while (blocker.getX() == startPoint.getX()) {
                    randIndex = rand.nextInt(boundaryLeft.length);
                    blocker = boundaryLeft[randIndex];
                }

                if (blocker.getY() < leftRectangle.getTopRight().getY()) {
                    rectangles.add(new Rectangle(new Point(blocker.getX() - width, blocker.getY()), new Point(blocker.getX(), blocker.getY() + data.result), blocker));
                } else {
                    useLeft = rand.nextBoolean();
                    if (useLeft) {
                        rectangles.add(new Rectangle(new Point(blocker.getX() - width, blocker.getY()), new Point(blocker.getX(), blocker.getY() + data.result), blocker));
                    } else {
                        rectangles.add(new Rectangle(new Point(blocker.getX(), blocker.getY()), new Point(blocker.getX() + width, blocker.getY() + data.result), blocker));
                    }
                }
            }
        } else {
            rectangles.add(rightRectangle);

            // Constructing rectangle making left option invalid
            Point[] internalLeft = leftRectangle.getInternal();
            int randIndex = rand.nextInt(internalLeft.length);
            Point invalidator = internalLeft[randIndex];
            rectangles.add(new Rectangle(new Point(invalidator.getX() -width, invalidator.getY()), new Point(invalidator.getX(), invalidator.getY() + data.result), invalidator));

            // Constructing rectangle limiting size of starting rectangle
            Point[] boundaryRight = rightRectangle.getBoundary(true,true, false, false);
            if (boundaryRight.length == 0) {
                // must lock in with two rectangles
                Point lockPoint = new Point(startPoint.getX() + 2 * width, startPoint.getY());
                rectangles.add(new Rectangle(new Point(startPoint.getX() + width, startPoint.getY()), new Point(startPoint.getX() + 2 * width, startPoint.getY() + data.result), lockPoint));

                // Construct final blocker
                Point[] finalBlockerOptions = (new Rectangle(lockPoint, new Point(lockPoint.getX() + width, lockPoint.getY() + data.result), lockPoint)).getInternal();
                randIndex = rand.nextInt(finalBlockerOptions.length);
                Point finalBlocker = finalBlockerOptions[randIndex];
                rectangles.add(new Rectangle(finalBlocker, new Point(finalBlocker.getX() + width, finalBlocker.getY() + data.result), finalBlocker));
            } else {
                randIndex = rand.nextInt(boundaryRight.length);
                Point blocker = boundaryRight[randIndex];
                while (blocker.getX() == startPoint.getX()) {
                    randIndex = rand.nextInt(boundaryRight.length);
                    blocker = boundaryRight[randIndex];
                }

                if (blocker.getY() < leftRectangle.getTopRight().getY()) {
                    rectangles.add(new Rectangle(blocker, new Point(blocker.getX() + width, blocker.getY() + data.result), blocker));
                } else {
                    useLeft = rand.nextBoolean();
                    if (useLeft) {
                        rectangles.add(new Rectangle(new Point(blocker.getX() - width, blocker.getY()), new Point(blocker.getX(), blocker.getY() + data.result), blocker));
                    } else {
                        rectangles.add(new Rectangle(new Point(blocker.getX(), blocker.getY()), new Point(blocker.getX() + width, blocker.getY() + data.result), blocker));
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
