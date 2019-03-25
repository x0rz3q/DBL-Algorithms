import models.Point;
import models.Rectangle;
import Collections.QuadTree;

import java.util.*;

import static java.lang.Math.ceil;

// Concrete generation strategy for 4pos
class Strategy4pos extends GenerationStrategy {
    @Override
    Point[] generate() {
        ArrayList<Rectangle> rectangles = new ArrayList<>();

        // Add starting rectangles
        Rectangle[] startingRectangles = generateStart();
        for (Rectangle r : startingRectangles) {
            rectangles.add(r);
        }
        int counter = 0;

        QuadTree tree = new QuadTree(new Rectangle(0, 0, 100000, 100000, new Point(0, 0)));
        QuadTree pointsTree = new QuadTree(new Rectangle(0, 0, 100000, 100000));
        for (Rectangle r : rectangles) {
            tree.insert(r);
            pointsTree.insert(new Rectangle(r.getPoI(), r.getPoI(), r.getPoI()));
        }

        while (rectangles.size() < data.n && counter < data.n * 1e7) {
            counter++;


            Point pointGuess = new Point(data.xGenerator.sample(0, 10000), data.yGenerator.sample(0, 10000));
            /*boolean noNW = pointsTree.query2D(new Rectangle(pointGuess.getX() - 0.5, pointGuess.getY() + height - 0.5, pointGuess.getX() + 0.5, pointGuess.getY() + height + 0.5)).size() > 0;
            boolean noNE = pointsTree.query2D(new Rectangle(pointGuess.getX() + width - 0.5, pointGuess.getY() + height - 0.5, pointGuess.getX() + width + 0.5, pointGuess.getY() + height + 0.5)).size() > 0;
            boolean noSE = pointsTree.query2D(new Rectangle(pointGuess.getX() + width - 0.5, pointGuess.getY() - 0.5, pointGuess.getX() + width + 0.5, pointGuess.getY() + 0.5)).size() > 0;
            boolean noSW = pointsTree.query2D(new Rectangle(pointGuess.getX() - 0.5, pointGuess.getY() - 0.5, pointGuess.getX() + 0.5, pointGuess.getY() + 0.5)).size() > 0;

            while (noNE && noNW && noSE && noSW) {
                pointGuess = new Point(data.xGenerator.sample(0, 10000), data.yGenerator.sample(0, 10000));
            }
            */
            while (pointsTree.query2D(new Rectangle(pointGuess.getX() - 0.5, pointGuess.getY() - 0.5, pointGuess.getX() + 0.5, pointGuess.getY() + 0.5)).size() > 0) {
                pointGuess = new Point(data.xGenerator.sample(0, 10000), data.yGenerator.sample(0, 10000));
            }
            int randOrientation = rand.nextInt(4);

            Rectangle guess;
            if (randOrientation == 0) { // NW
                guess = new Rectangle(pointGuess.getX() - width, pointGuess.getY(), pointGuess.getX(), pointGuess.getY() + height);
            } else if (randOrientation == 1) { // NE
                guess = new Rectangle(pointGuess.getX(), pointGuess.getY(), pointGuess.getX() + width, pointGuess.getY() + height);
            } else if (randOrientation == 2) { // SW
                guess = new Rectangle(pointGuess.getX() - width, pointGuess.getY() - height, pointGuess.getX(), pointGuess.getY());
            } else { // SE
                guess = new Rectangle(pointGuess.getX(), pointGuess.getY() - height, pointGuess.getX() + width, pointGuess.getY());
            }

            if (tree.query2D(guess).size() == 0) {
                guess.setPoI(pointGuess);
                pointsTree.insert(new Rectangle(pointGuess, pointGuess, pointGuess));
                rectangles.add(guess);
                tree.insert(guess);
            }


            /*
            Rectangle guess = new Rectangle(pointGuess.getX(), pointGuess.getY(), pointGuess.getX() + width, pointGuess.getY() + height);
            if (tree.query2D(guess).size() == 0) {
                int randCorner = rand.nextInt(4);

                guess.setPoI(pointGuess);

                if (randCorner == 0) {
                    guess.setPoI(pointGuess);
                } else if (randCorner == 1) {
                    guess.setPoI(new Point(pointGuess.getX() + width, pointGuess.getY()));
                } else if (randCorner == 2) {
                    guess.setPoI(new Point(pointGuess.getX(), pointGuess.getY() + height));
                } else {
                    guess.setPoI(new Point(pointGuess.getX() + width, pointGuess.getY()+ width));
                }

                pointsTree.insert(new Rectangle(pointGuess, pointGuess, pointGuess));
                rectangles.add(guess);
                tree.insert(guess);
            }
            */
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

        // initial point
        Point startPoint = new Point(data.xGenerator.sample(3 * (int) ceil(width), (int) (10000 - 3 * ceil(width))), data.yGenerator.sample(3 * (int) ceil(width), (int) (10000 - 3 * ceil(width))));

        int selectedDirection = rand.nextInt(4);

        // possible rectangles for startPoint
        Rectangle ne = new Rectangle(startPoint.getX(), startPoint.getY(), startPoint.getX() + width, startPoint.getY() + height);
        Rectangle nw = new Rectangle(startPoint.getX() - width, startPoint.getY(), startPoint.getX(), startPoint.getY() + height);
        Rectangle sw = new Rectangle(startPoint.getX() - width, startPoint.getY() - height, startPoint.getX(), startPoint.getY());
        Rectangle se = new Rectangle(startPoint.getX(), startPoint.getY() - height, startPoint.getX() + width, startPoint.getY());

        Map<Integer, Rectangle> map = new HashMap<>();
        map.put(0, ne);
        map.put(1, nw);
        map.put(2, sw);
        map.put(3, se);

        Rectangle sel = map.get(selectedDirection);

        Set<Rectangle> unselected = new HashSet<>();
        for (int i : map.keySet()) {
            if (i != selectedDirection) {
                unselected.add(map.get(i));
            }
        }

        sel.setPoI(startPoint);
        rectangles.add(sel);

        // create invalidators for other candidates startPoint
        for (int i : map.keySet()) {
            if (i != selectedDirection) {
                Point[] internal = map.get(i).getInternal();
                Point randInternalPoint = internal[rand.nextInt(internal.length)];
                Rectangle invalidator;

                if (i == 0) {
                    invalidator = new Rectangle(randInternalPoint.getX(), randInternalPoint.getY(), randInternalPoint.getX() + width, randInternalPoint.getY() + height, randInternalPoint);
                } else if (i == 1) {
                    invalidator = new Rectangle(randInternalPoint.getX() - width, randInternalPoint.getY(), randInternalPoint.getX(), randInternalPoint.getY() + height, randInternalPoint);
                } else if (i == 2) {
                    invalidator = new Rectangle(randInternalPoint.getX() - width, randInternalPoint.getY() - height, randInternalPoint.getX(), randInternalPoint.getY(), randInternalPoint);
                } else {
                    invalidator = new Rectangle(randInternalPoint.getX(), randInternalPoint.getY() - height, randInternalPoint.getX() + width, randInternalPoint.getY(), randInternalPoint);
                }

                rectangles.add(invalidator);
            }
        }

        // select correct boundary to be locked in
        Point[] boundarySelected;
        if (selectedDirection == 0) {
            boundarySelected = sel.getBoundaryStrict(true, true, false, false);
        } else if (selectedDirection == 1) {
            boundarySelected = sel.getBoundaryStrict(true, false, false, true);
        } else if (selectedDirection == 2) {
            boundarySelected = sel.getBoundaryStrict(false, false, true, true);
        } else {
            boundarySelected = sel.getBoundaryStrict(false, true, true, false);
        }

        Point randBoundPoint = boundarySelected[rand.nextInt(boundarySelected.length)];

        // create blocker
        Rectangle blocker;
        if (selectedDirection == 0) {
            blocker = new Rectangle(randBoundPoint.getX(), randBoundPoint.getY(), randBoundPoint.getX() + width, randBoundPoint.getY() + height, randBoundPoint);
        } else if (selectedDirection == 1) {
            blocker = new Rectangle(randBoundPoint.getX() - width, randBoundPoint.getY(), randBoundPoint.getX(), randBoundPoint.getY() + height, randBoundPoint);
        } else if (selectedDirection == 2) {
            blocker = new Rectangle(randBoundPoint.getX() - width, randBoundPoint.getY() - height, randBoundPoint.getX(), randBoundPoint.getY(), randBoundPoint);
        } else {
            blocker = new Rectangle(randBoundPoint.getX(), randBoundPoint.getY() - height, randBoundPoint.getX() + width, randBoundPoint.getY(), randBoundPoint);
        }

        rectangles.add(blocker);

        Rectangle[] rectangleArray = new Rectangle[rectangles.size()];
        rectangleArray = rectangles.toArray(rectangleArray);
        return rectangleArray;
    }

    Strategy4pos(TestData data) {
        this.data = data;
        this.height = data.result;
        this.width = data.ratio * data.result;
    }
}
