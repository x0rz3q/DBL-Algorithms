import models.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

// Allows for writing to file
class Printer {
    FileWriter fileWriter;
    PrintWriter printWriter;

    /**
     * prints the problem statement: model, aspect ratio and number of points to the specified file
     * @param model
     * @param ratio
     * @param n
     */
    void printBasics(String model, double ratio, int n) {
        printWriter.println("placement model: " + model);
        printWriter.println("aspect ratio: " + ratio);
        printWriter.println("number of points: " + n);
    }

    /**
     * prints the coordinates of the generated points to the specified file
     * @param points
     */
    void printPoints(Point[] points) {
        Collections.shuffle(Arrays.asList(points));
        for (Point p : points) {
            printWriter.println(p.getIntX() + " " + p.getIntY());
        }
    }

    /**
     * prints the correct result to the specified file
     * @param result
     */
    void printResult(double result, double expectedMinimum) {
        printWriter.println("label height: " + result);
        printWriter.println("minimum expected result: " + expectedMinimum);
        printWriter.close();
    }

    /**
     * Set file to be written to
     * @param file
     * @throws IOException
     */
    void setFile(File file) throws IOException {
        this.fileWriter = new FileWriter(file);
        this.printWriter = new PrintWriter(fileWriter);
    }

}
