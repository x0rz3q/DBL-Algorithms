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
        while (rectangles.size() < data.n && counter < data.n * 100000) {
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
