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

    // Number Generators in the x- and y-dimensions
    NumberGenerator xGenerator;
    NumberGenerator yGenerator;

    // Setters
    void setModel(String newModel) {this.model = newModel;}
    void setN(int newN) {this.n = newN;}
    void setRatio(double newRatio) {this.ratio = newRatio;}
    void setResult(double newResult) throws IllegalArgumentException {
        this.result = newResult;
        if (this.model.equals("2pos") && (this.result != Math.ceil(this.result) && this.result * this.ratio * 2 != Math.ceil(this.result * this.ratio * 2))) {
            throw new IllegalArgumentException("Combination model-ratio-result impossible");
        }
        if (this.model.equals("4pos") && (this.result * 2 != Math.ceil(this.result * 2) && this.result * this.ratio * 2 != Math.ceil(this.result * this.ratio * 2))) {
            throw new IllegalArgumentException("Combination model-ratio-result impossible");
        }
    }
    void setxGenerator(NumberGenerator newxGenerator) {this.xGenerator = newxGenerator;}
    void setyGenerator(NumberGenerator newyGenerator) {this.yGenerator = newyGenerator;}
}
