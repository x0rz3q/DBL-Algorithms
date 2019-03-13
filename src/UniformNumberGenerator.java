import org.apache.commons.math3.distribution.UniformIntegerDistribution;

// Concrete number generator based on uniform distribution
class UniformNumberGenerator extends NumberGenerator {
    /**
     * Constructor for UniformNumberGenerator
     * @pre upper > lower
     * @param lower lower bound for Uniform distribution
     * @param upper upper bound for Uniform distribution
     */
    UniformNumberGenerator(int lower, int upper) {
        this.dist = new UniformIntegerDistribution(lower, upper);
    }
}
