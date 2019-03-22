import Collections.QuadTree;
import models.Point;
import models.Rectangle;

import java.util.ArrayList;

// Concrete generation strategy for 1slider
class Strategy1slider extends GenerationStrategy {
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
        double height = data.result;

        QuadTree tree = new QuadTree(new Rectangle(0, 0, 100000, 100000, new Point(0, 0)));
        QuadTree pointsTree = new QuadTree(new Rectangle(0, 0, 100000, 100000));
        for (Rectangle r : rectangles) {
            tree.insert(r);
            pointsTree.insert(new Rectangle(r.getPoI(), r.getPoI(), r.getPoI()));
        }

        while (rectangles.size() < data.n && counter < data.n * 1e5) {
            counter++;

            Point candidate = new Point(data.xGenerator.sample(0, 10000), data.yGenerator.sample(0, 10000));
            while (pointsTree.query2D(new Rectangle(candidate.getX() - 0.5, candidate.getY() - 0.5, candidate.getX() + 0.5, candidate.getY() + 0.5)).size() > 0) {
                candidate = new Point(data.xGenerator.sample(0, 10000), data.yGenerator.sample(0, 10000));
            }
            double shift = rand.nextDouble();
            Rectangle candidateRectangle = new Rectangle(candidate.getX() - (shift - 1.0) * width, candidate.getY(), candidate.getX() + shift * width, candidate.getY() + height);

            if (tree.query2D(candidateRectangle).size() == 0) {
                candidateRectangle.setPoI(candidate);
                rectangles.add(candidateRectangle);
                tree.insert(candidateRectangle);
                pointsTree.insert(new Rectangle(candidate, candidate, candidate));
                continue;
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
        double width = data.result * data.ratio;
        double height = data.result;
        if (2 * width == Math.ceil(2 * width) && width >= 1.5) {
            return generateStartWidth();
        } else {
            return generateStartHeight();
        }
    }

    Rectangle[] generateStartWidth() {
        double width = data.result * data.ratio;
        double height = data.result;

        // Generate starting location
        double startX = data.xGenerator.sample((int) Math.ceil(width), (int) Math.floor(10000 - 3 * width));
        double startY = data.yGenerator.sample(0, (int) Math.floor(10000 - height));

        // Assign rectangles
        Rectangle startLeft = new Rectangle(startX, startY, startX + width, startY + height);
        Rectangle startRight = new Rectangle(startX + width, startY, startX + 2 * width, startY + height);
        Rectangle blockLeft = new Rectangle(startX - width, startY, startX, startY + height);
        Rectangle blockRight = new Rectangle(startX + 2 * width, startY, startX + 3 * width, startY + height);

        // Assign points
        Point[] startLeftPoints = startLeft.getBoundaryStrict(false, false, true, false);
        Point[] startRightPoints = startRight.getBoundaryStrict(false, false, true, false);
        int indexLeft = rand.nextInt(startLeftPoints.length);
        int indexRight = rand.nextInt(startRightPoints.length);
        startLeft.setPoI(startLeftPoints[indexLeft]);
        startRight.setPoI(startRightPoints[indexRight]);

        blockLeft.setPoI(blockLeft.getBottomRight());
        blockRight.setPoI(blockRight.getBottomLeft());

        // Assign to return array
        Rectangle[] rectangleArray = new Rectangle[4];
        rectangleArray[0] = startLeft;
        rectangleArray[1] = blockLeft;
        rectangleArray[2] = startRight;
        rectangleArray[3] = blockRight;

        return rectangleArray;
    }

    Rectangle[] generateStartHeight() {
        double width = data.result * data.ratio;
        double height = data.result;

        // Generate starting location
        double startX = data.xGenerator.sample((int) Math.ceil(width + 1), (int) Math.floor(9999 - width));
        double startY = data.yGenerator.sample(0, (int) Math.floor(10000 - 2 * height));

        // Assign Rectangles
        double shift = rand.nextDouble();
        Rectangle startDown = new Rectangle(startX - (shift - 1.0) * width, startY, startX + shift * width, startY + height);

        Rectangle blockLeft = new Rectangle(startX - width, startY + height, startX, startY + 2 * height);
        Rectangle blockRight = new Rectangle(startX, startY + height, startX + width, startY + 2 * height);

        // Assign points
        startDown.setPoI(new Point(startX, startY));
        blockLeft.setPoI(new Point(startX - 1, startY + height));
        blockRight.setPoI(new Point(startX, startY + height));

        // Assign to return array
        Rectangle[] rectangleArray = new Rectangle[3];
        rectangleArray[0] = startDown;
        rectangleArray[1] = blockLeft;
        rectangleArray[2] = blockRight;

        return rectangleArray;
    }

    Strategy1slider(TestData data) {
        this.data = data;
    }
}
