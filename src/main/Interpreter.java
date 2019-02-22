package main;/*
 * author = Jeroen Schols
 */

import Parser.DataRecord;
import interfaces.models.LabelInterface;

public class Interpreter {

    static double getScore(DataRecord record) {
        if (isValid(record)) return record.height;
        return 0;
    }


    public static boolean isValid(DataRecord record) {
        if (record.labels == null || record.placementModel == null) return false;

        for (LabelInterface label : record.labels) {
            if (record.collection.query2D(label.getRectangle()).size() > 1) return false;
        }

        return true;
    }

}
