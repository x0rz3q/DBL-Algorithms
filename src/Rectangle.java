import java.util.ArrayList;

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
