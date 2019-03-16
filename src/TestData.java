// data structure containing all test variables
class TestData {
    // model: 2pos, 4pos or 1slider
    String model;

    // number of points/labels
    int n;

    // aspect ratio label
    double ratio;

    /* correct result (maximum height)
    *   2pos: result == integer || result * alpha * 2 == integer
    *   4pos: result * 2 == integer || result * alpha * 2 == integer
    *   1slider: result == real
    */
    double result;

    /* expected minimum result
    *   2pos: expectedMinimum = result
    *   4pos: expectedMinimum = result / 2
    *   1slider: expectedMinimum = result / 2
    */
    double expectedMinimum;

    // Number Generators in the x- and y-dimensions
    NumberGenerator xGenerator;
    NumberGenerator yGenerator;

    // Setters

    /**
     * Setter for this.model
     * @pre newModel == "2pos" || newModel == "4pos" || newModel == "1slider"
     * @param newModel
     * @throws IllegalArgumentException if precondition violated
     */
    void setModel(String newModel) throws IllegalArgumentException {
        if (!newModel.equals("2pos") && !newModel.equals("4pos") && !newModel.equals("1slider")) {
            throw new IllegalArgumentException("Invalid model");
        }
        this.model = newModel;
    }

    /**
     * Setter for this.n
     * @pre newN > 4
     * @param newN
     * @throws IllegalArgumentException if newN <= 4
     */
    void setN(int newN) throws IllegalArgumentException {
        if (newN <= 4) {
            throw new IllegalArgumentException("Invalid n (number of points)");
        }
        this.n = newN;
    }

    /**
     * Setter for this.ratio
     * @pre newRatio > 0
     * @param newRatio
     * @throws IllegalArgumentException if newRatio <= 0
     */
    void setRatio(double newRatio) throws IllegalArgumentException {
        if (newRatio <= 0) {
            throw new IllegalArgumentException("Invalid ratio");
        }
        this.ratio = newRatio;
    }

    /**
     * Setter for this.result
     * @pre this.ratio set
     * @param newResult
     * @throws IllegalArgumentException if this.ratio undefined, or result does not satisfy specified requirements
     */
    void setResult(double newResult) throws IllegalArgumentException {
        this.result = newResult;
        if (this.ratio == 0) {
            throw new IllegalArgumentException("Ratio undefined");
        }
        if (this.model.equals("2pos") && (this.result != Math.ceil(this.result) && this.result * this.ratio * 2 != Math.ceil(this.result * this.ratio * 2))) {
            throw new IllegalArgumentException("Combination model-ratio-result impossible");
        }
        if (this.model.equals("4pos") && (this.result * 2 != Math.ceil(this.result * 2) && this.result * this.ratio * 2 != Math.ceil(this.result * this.ratio * 2))) {
            throw new IllegalArgumentException("Combination model-ratio-result impossible");
        }

        if (this.model.equals("2pos")) {
            this.expectedMinimum = newResult;
        } else {
            this.expectedMinimum = newResult / 2;
        }
    }

    /**
     * Sets generator for x-coordinates
     * @pre newxGenerator != null
     * @param newxGenerator
     * @throws NullPointerException if newxGenerator == null
     */
    void setxGenerator(NumberGenerator newxGenerator) {
        if (newxGenerator == null) {
            throw new NullPointerException("newxGenerator == null");
        }
        this.xGenerator = newxGenerator;
    }

    /**
     * Sets generator for y-coordinates
     * @pre newyGenerator != null
     * @param newyGenerator
     * @throws NullPointerException if newyGenerator == null
     */
    void setyGenerator(NumberGenerator newyGenerator) throws NullPointerException {
        if (newyGenerator == null) {
            throw new NullPointerException("newyGenerator == null");
        }
        this.yGenerator = newyGenerator;
    }

    public String toString() {
        return model + " " + n + " " + ratio + " " +result;
    }
}
