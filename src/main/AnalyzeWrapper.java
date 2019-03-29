package main;

import Parser.DataRecord;
import Parser.Parser;
import algorithms.FourPositionWagnerWolff;
import algorithms.GreedySliderAlgorithm;
import algorithms.TwoPositionBinarySearcher;
import interfaces.models.LabelInterface;
import models.FourPositionLabel;
import models.PositionLabel;
import visualizer.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.server.ExportException;
import java.util.Scanner;

public class AnalyzeWrapper {
    public static void main(String[] args) throws Exception {
        if (args.length == 0 || !(new File(args[0]).exists())) {
            throw new IllegalArgumentException("Invalid file parameter");
        }

        long start = System.currentTimeMillis();

        FileInputStream is = new FileInputStream(new File(args[0]));
        Parser parser = new Parser();
        DataRecord record = parser.input(is);

        switch (record.placementModel) {
            case ONE_SLIDER:
                (new GreedySliderAlgorithm()).solve(record);
                break;
            case TWO_POS:
                (new TwoPositionBinarySearcher()).solve(record);
                break;
            case FOUR_POS:
                (new FourPositionWagnerWolff()).solve(record);
                break;
            default:
                throw new ExportException("Not implemented yet");
        }

        record.height = Math.round(record.height * 1E6) / 1E6;

        long end = System.currentTimeMillis();

        is = new FileInputStream(new File(args[0]));
        Scanner scanner = new Scanner(is);

        String maxHeight = null;
        while (scanner.hasNextLine()) {
            maxHeight = scanner.nextLine();
            if (maxHeight.contains("label height")) {
                break;
            }
        }

        String difficulty = null;
        while (scanner.hasNextLine()) {
            difficulty = scanner.nextLine();
            if (difficulty.contains("Difficulty")) {
                difficulty = difficulty.split("Difficulty: ")[1];
            }
        }

        if (maxHeight == null)
            throw new Exception("No maximal label height found in file " + args[0]);

        System.out.println("Model: " + record.placementModel.toString());
        System.out.println("Label count: " + record.labels.size());
        System.out.println("Time: " + (end - start));
        System.out.println("Height: " + record.height);
        System.out.println("Max: " + maxHeight);
        System.out.println("Difficulty: " + difficulty);

        for (LabelInterface label : record.labels) {
            // field extended label already has a height set
            switch (record.placementModel) {
                case TWO_POS:
                    ((PositionLabel) label).setHeight(record.height);
                    break;
                case FOUR_POS:
                    ((FourPositionLabel) label).setHeight(record.height);
                    break;
            }
        }

        System.out.println("Overlaps: " + Interpreter.overlapCount(record.labels));
    }
}
