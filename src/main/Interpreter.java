package main;/*
 * author = Jeroen Schols
 */

import Parser.DataRecord;
import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;
import javafx.util.Pair;

import java.util.Collection;

public class Interpreter {

    static double getScore(DataRecord record) {
        if (isValid(record)) return record.height;
        return 0;
    }


    public static boolean isValid (DataRecord record) {
        if (record.labels == null || record.placementModel == null) return false;

        boolean valid = true;
        for (LabelInterface label : record.labels) {
            Collection<SquareInterface> labels = record.collection.query2D(label);
            if (labels.size() > 1) {
                System.out.println();
                for (SquareInterface l : labels) {
                    System.out.println("X = ["+l.getXMin()+","+l.getXMax()+"], y = ["+l.getYMin()+","+l.getYMax()+"]");
                }
//                valid = false;
            }
        }

        return valid;
    }

}
