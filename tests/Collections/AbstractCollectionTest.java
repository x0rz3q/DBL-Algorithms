package Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractCollectionTest {
    protected AbstractCollection instance;
    protected abstract void setInstance();

    @BeforeEach
    void setup() { setInstance(); }



    @Test
    void getSize() {
    }
}