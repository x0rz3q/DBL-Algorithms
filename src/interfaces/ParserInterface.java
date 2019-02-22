package interfaces;

import Parser.DataRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ParserInterface {
    /**
     * Parse input to program structure.
     *
     * @param source {@link Readable}
     * @return DataRecord
     * @throws NullPointerException if {@code source == null}
     * @throws IOException          if read error occurs
     */
    DataRecord input(InputStream source, Class<? extends AbstractCollectionInterface> collectionClass) throws NullPointerException, IOException;

    /**
     * Serialize internal program structure towards output stream.
     *
     * @param record {@link DataRecord}
     * @param stream {@link OutputStream}
     * @return OutputStream
     * @throws NullPointerException if {@code record == null || stream == null}
     */
    void output(DataRecord record, OutputStream stream) throws NullPointerException;
}
