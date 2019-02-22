package interfaces;

import Parser.DataRecord;

public interface AbstractAlgorithmInterface {
    /**
     * Place all labels with the height being maximized.
     *
     * @param record {@link DataRecord}
     * @Modifies nodes
     */
    void solve(DataRecord record);
}
