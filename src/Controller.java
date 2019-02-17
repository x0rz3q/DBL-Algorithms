import java.io.File;
import java.io.IOException;

// controller for generation process of test
class Controller {
    // object containing all data for single test
    private TestData data;
    // object for to file printing
    private Printer printer;

    private GenerationStrategy strategy;

    private File writeFile;


    /**
     * generates a test in the given file location
     * @pre data != null (including all fields) && printer != null;
     * @post file at specified location contains test for data specified, with correct result at the end
     */
    void generate() {
        printer.printBasics(data.model, data.ratio, data.n);
        chooseStrategy();
        Point[] points = strategy.generate();
        printer.printPoints(points);
        printer.printResult(data.result);
    }

    /**
     * chooses the strategy to use based on the model
     * @pre data != null (including all fields)
     * @post Controller.strategy is set according to the model
     * @throws IllegalArgumentException
     */
    void chooseStrategy() throws IllegalArgumentException {
        if (data.model.equals("2pos")) {
            strategy = new Strategy2pos(data);
        } else if (data.model.equals("4pos")) {
            strategy = new Strategy4pos(data);
        } else if (data.model.equals("1slider")) {
            strategy = new Strategy1slider(data);
        } else {
            throw new IllegalArgumentException("Non-existent model specified");
        }
    }

    void setData(TestData newData) throws IOException {
        data = newData;
        writeFile = new File(data.model + "_" + data.n + "_" + data.ratio + "_" + data.result + "_" + data.xGenerator.toString() + "_" + data.yGenerator.toString()+".txt");
        writeFile.createNewFile();
        this.printer.setFile(writeFile);
    }

    Controller() throws IOException {
        this.printer = new Printer();
    }


}
