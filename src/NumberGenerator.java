import org.apache.commons.math3.distribution.AbstractIntegerDistribution;

// Abstract number generator
abstract class NumberGenerator {
    AbstractIntegerDistribution dist;
    // TODO write contract
    int sample() {
        return dist.sample();
    }
}
