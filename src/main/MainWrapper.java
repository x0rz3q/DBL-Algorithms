package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainWrapper {
    public static void main(String[] args) throws IOException, IllegalArgumentException {
//        if (args.length == 0 || !(new File(args[0]).exists())) {
//            throw new IllegalArgumentException("Invalid file parameter");
//        }
        String[] a = new String[]{"/home/juris/Uni/DBL-Algorithms/profiling/hiddetests/4pos_10000_3.0_10.0_UniformNumberGenerator@6e2c634b_UniformNumberGenerator@37a71e93RobinRobinRobinRobi.txt"};
        FileInputStream is = new FileInputStream(new File(a[0]));
        System.setIn(is);
        Controller.main(a);
    }
}
