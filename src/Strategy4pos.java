import models.Point;
import models.Rectangle;

import java.util.*;

import static java.lang.Math.ceil;

// Concrete generation strategy for 4pos
class Strategy4pos extends GenerationStrategy {
    @Override
    Rectangle generateCandidateRectangle(Point candidate) {
        int randOrientation = rand.nextInt(4);

        if (randOrientation == 0) { // NW
            return new Rectangle(candidate.getX() - width, candidate.getY(), candidate.getX(), candidate.getY() + height);
        } else if (randOrientation == 1) { // NE
            return new Rectangle(candidate.getX(), candidate.getY(), candidate.getX() + width, candidate.getY() + height);
        } else if (randOrientation == 2) { // SW
            return new Rectangle(candidate.getX() - width, candidate.getY() - height, candidate.getX(), candidate.getY());
        } else { // SE
            return new Rectangle(candidate.getX(), candidate.getY() - height, candidate.getX() + width, candidate.getY());
        }
    }

    @Override
    void generateStart() {

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
        fullInsert(sel);

        // create invalidators for other candidates startPoint
        for (int i : map.keySet()) {
            if (i != selectedDirection) {
                Point[] internal = map.get(i).getInternal();
                Point randInternalPoint = internal[rand.nextInt(internal.length)];

                Rectangle invalidator = getDirectionalRectangle(randInternalPoint, i);
                fullInsert(invalidator);
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
        Rectangle blocker = getDirectionalRectangle(randBoundPoint, selectedDirection);
        fullInsert(blocker);
    }

    /**
     * Creates rectangle based on direction according to 4pos model
     * @pre direction \in {0, 1, 2, 3}
     * @param point
     * @param direction
     * @return created rectangle
     */
    private Rectangle getDirectionalRectangle(Point point, int direction) throws IllegalArgumentException {
        switch (direction) {
            case 0:
                return new Rectangle(point.getX(), point.getY(), point.getX() + width, point.getY() + height, point);
            case 1:
                return new Rectangle(point.getX() - width, point.getY(), point.getX(), point.getY() + height, point);
            case 2:
                return new Rectangle(point.getX() - width, point.getY() - height, point.getX(), point.getY(), point);
            case 3:
                return new Rectangle(point.getX(), point.getY() - height, point.getX() + width, point.getY(), point);
            default:
                throw new IllegalArgumentException("direction out of bounds");
        }
    }

    Strategy4pos(TestData data) {
        this.data = data;
        this.height = data.result;
        this.width = data.ratio * data.result;
    }
}
