import models.Point;
import models.Rectangle;

// Concrete generation strategy for 1slider
class Strategy1slider extends GenerationStrategy {
    @Override
    Rectangle generateCandidateRectangle(Point candidate) {
        double shift = rand.nextDouble();
        return new Rectangle(candidate.getX() + (shift - 1.0) * width, candidate.getY(), candidate.getX() + shift * width, candidate.getY() + height, candidate);
    }

    @Override
    void generateStart() {
        if (2 * width == Math.ceil(2 * width) && width >= 1.5) {
            generateStartWidth();
        } else {
            generateStartHeight();
        }
    }

    private void generateStartWidth() {

        // Generate starting location
        double startX = data.xGenerator.sample((int) Math.ceil(width), (int) Math.floor(10000 - 3 * width));
        double startY = data.yGenerator.sample(0, (int) Math.floor(10000 - height));

        // Assign rectangles
        Rectangle startLeft = new Rectangle(startX, startY, startX + width, startY + height);
        Rectangle startRight = new Rectangle(startX + width, startY, startX + 2 * width, startY + height);
        Rectangle blockLeft = new Rectangle(startX - width, startY, startX, startY + height);
        Rectangle blockRight = new Rectangle(startX + 2 * width, startY, startX + 3 * width, startY + height);

        // Assign points
        startLeft.setPoI(generateRandSouthStrict(startLeft));
        startRight.setPoI(generateRandSouthStrict(startRight));

        blockLeft.setPoI(blockLeft.getBottomRight());
        blockRight.setPoI(blockRight.getBottomLeft());

        // Insert into data structures
        fullInsert(startLeft);
        fullInsert(startRight);
        fullInsert(blockLeft);
        fullInsert(blockRight);
    }

    private Point generateRandSouthStrict(Rectangle rect) {
        Point[] candidatePoints = rect.getBoundaryStrict(false, false, true, false);
        int randIndex = rand.nextInt(candidatePoints.length);
        return candidatePoints[randIndex];
    }

    private void generateStartHeight() {

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

        fullInsert(startDown);
        fullInsert(blockLeft);
        fullInsert(blockRight);
    }

    Strategy1slider(TestData data) {
        this.data = data;
        this.height = data.result;
        this.width = data.ratio * data.result;
    }
}
