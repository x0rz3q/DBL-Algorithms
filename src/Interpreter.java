/*
 * author = Jeroen Schols
 */

import interfaces.models.LabelInterface;
import models.OutputRecord;

class Interpreter {

    /**
     * returns the score worth of given output record
     *
     * @param record {@link OutputRecord}
     * @return Float
     */
    static float getScore (OutputRecord record) {
        if (isValid(record)) return record.height;
        return 0;
    }

    /**
     * returns whether a given output record is valid
     * does not know which problem instance is solved by this record
     *
     * @param record {@link OutputRecord}
     * @return Boolean
     */
    static boolean isValid (OutputRecord record) {
        if (record.points == null || record.placementModel == null) return false;

        for (LabelInterface label : record.points) {
            if (!label.getHeight().equals(record.height) || record.points.query2D(label).size() > 1) return false;
        }

        return true;
    }

}
