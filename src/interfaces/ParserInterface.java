package interfaces;

import models.InputRecord;
import models.OutputRecord;

import java.io.OutputStream;

public interface ParserInterface {
    InputRecord input(Readable source);
    OutputStream output(OutputRecord record);
}
