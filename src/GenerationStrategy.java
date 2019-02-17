import java.util.Random;

// Abstract generation strategy
abstract class GenerationStrategy {
    TestData data;
    Random rand = new Random();

    // TODO provide contract
    abstract Point[] generate();
    // TODO provide contract
    abstract Rectangle[] generateStart();
    // TODO provide contract

}
