package main;

import Collections.QuadTree;
import Parser.DataRecord;
import Parser.Parser;
import algorithms.GreedySliderAlgorithm;
import algorithms.TwoPositionBinarySearcher;
import interfaces.ParserInterface;

import java.io.IOException;

public class Controller {
    private ParserInterface parser;

    public Controller() {
        this.parser = new Parser();
    }

    public static void main(String[] args) {
        try {
            (new Controller()).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException, NullPointerException {
        DataRecord record = this.parser.input(System.in, QuadTree.class);
        switch (record.placementModel) {
            case ONE_SLIDER:
                (new GreedySliderAlgorithm()).solve(record);
                break;
            case TWO_POS:
                (new TwoPositionBinarySearcher()).solve(record);
                break;
            case FOUR_POS:
                throw new UnsupportedOperationException("No 4-pos algorithm implemented yet");
        }
        this.parser.output(record, System.out);
    }
}
