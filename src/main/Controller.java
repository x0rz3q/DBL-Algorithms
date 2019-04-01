package main;

import Collections.KDTree;
import Collections.QuadTree;
import Parser.DataRecord;
import Parser.Parser;
import algorithms.FourPositionWagnerWolff;
import algorithms.GreedySliderAlgorithm;
import algorithms.TwoPositionBinarySearcher;
import interfaces.ParserInterface;

import java.io.IOException;
import java.rmi.server.ExportException;

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
        DataRecord record = this.parser.input(System.in);
        switch (record.placementModel) {
            case ONE_SLIDER:
                (new GreedySliderAlgorithm()).solve(record);
                break;
            case TWO_POS:
                (new TwoPositionBinarySearcher()).solve(record);
                break;
            case FOUR_POS:
                (new FourPositionWagnerWolff(3)).solve(record);
                System.err.println(record.height);
//                int i = 0;
//                while(true) {
//                    (new FourPositionWagnerWolff(i++)).solve(record);
//                    System.err.println(i + "," + record.height);
//                }
//                break;
            default:
                throw new ExportException("Not implemented yet");
        }
//        this.parser.output(record, System.out);
    }
}
