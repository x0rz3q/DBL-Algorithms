import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainWrapper {
    public static void main(String[] args) throws IOException, IllegalArgumentException {
        if (args.length == 0 || !(new File(args[0]).exists())) {
            throw new IllegalArgumentException("Invalid file parameter");
        }

        FileInputStream is = new FileInputStream(new File(args[0]));
        System.setIn(is);
        Controller.main(args);
    }
}
