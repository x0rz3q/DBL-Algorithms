import org.apache.commons.math3.distribution.GeometricDistribution;

// Concrete number generator based on Geometric distribution
class GeometricNumberGenerator extends NumberGenerator {
    /**
     * Constructor for GeometricNumberGenerator
     * @pre 0 <= p <= 1
     * @param p success probability
     */
    GeometricNumberGenerator(double p) {
        this.dist = new GeometricDistribution(p);
    }
}
