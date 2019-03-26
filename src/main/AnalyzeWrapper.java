package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AnalyzeWrapper {
    public static void main(String[] args) throws IOException, IllegalArgumentException {
        if (args.length == 0 || !(new File(args[0]).exists())) {
            throw new IllegalArgumentException("Invalid file parameter");
        }

				long start = System.currentTimeMillis();

        FileInputStream is = new FileInputStream(new File(args[0]));
        System.setIn(is);
        Controller.main(args);

				long end = System.currentTimeMillis();
				System.out.println("Time :" + (end-start));
    }
}
