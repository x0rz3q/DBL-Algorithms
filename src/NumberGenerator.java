import org.apache.commons.math3.distribution.AbstractIntegerDistribution;

// Abstract number generator
abstract class NumberGenerator {
    AbstractIntegerDistribution dist;

    /**
     * Samples set distribution within limits (>= lower and <= upper)
     * @param lower
     * @param upper
     * @return sample
     */
    int sample(int lower, int upper) {
        int sample = dist.sample();
        while (sample < lower || sample > upper) {
            sample = dist.sample();
        }
        return sample;
    }
}
