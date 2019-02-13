package interfaces;

import interfaces.models.SquareInterface;
import models.DataRecord;

public interface AbstractAlgorithmInterface {
    /**
     * Place all labels with the height being maximized.
     *
     * @Modifies nodes
     * @param record {@link DataRecord}
     */
    void solve(DataRecord record);
}
