import models.Point;
import models.Rectangle;

// Concrete generation strategy for 4pos
class Strategy4pos extends GenerationStrategy {
    @Override
    Point[] generate() {
        // TODO implement
        return new Point[0];
    }

    @Override
    Rectangle[] generateStart() {
        // TODO implement
        return new Rectangle[0];
    }

    Strategy4pos(TestData data) {
        this.data = data;
    }
}
