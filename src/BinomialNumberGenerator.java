import org.apache.commons.math3.distribution.BinomialDistribution;

// Concrete number generator based on Binomial distribution
class BinomialNumberGenerator extends NumberGenerator {
    /**
     * Constructor for BinomialNumberGenerator
     * @pre 0 <= p <= 1 && n > 0
     * @param p success probability per trial
     * @param n number of trials
     */
    BinomialNumberGenerator(double p, int n) {
        this.dist = new BinomialDistribution(n, p);
    }
}
