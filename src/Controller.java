import Collections.QuadTree;
import algorithms.TwoPositionBinarySearcher;
import interfaces.ParserInterface;
import Parser.*;

import java.io.IOException;

public class Controller {
    private ParserInterface parser;

    public Controller() {
        this.parser = new Parser();
    }

    public void run() throws IOException, NullPointerException{
        DataRecord record = this.parser.input(System.in, QuadTree.class);
        (new TwoPositionBinarySearcher()).solve(record);
        this.parser.output(record, System.out);
    }

    public static void main(String[] args) {
        try {
            (new Controller()).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
