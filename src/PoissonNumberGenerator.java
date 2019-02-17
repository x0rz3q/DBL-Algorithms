import org.apache.commons.math3.distribution.PoissonDistribution;

// Concrete number generator based on Poisson distribution
class PoissonNumberGenerator extends NumberGenerator {
    /**
     * Constructor for PoissonNumberGenerator
     * @pre p > 0
     * @param p mean & variance
     */
    PoissonNumberGenerator(double p) {
        this.dist = new PoissonDistribution(p);
    }
}
