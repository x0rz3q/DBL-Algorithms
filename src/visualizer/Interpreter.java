package visualizer;

import Collections.QuadTree;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import interfaces.models.PointInterface;
import models.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class Interpreter {
    public static boolean overlap(List<LabelInterface> geoms) {
        QuadTree tree = new QuadTree();

        for (LabelInterface geo : geoms) {
            if (tree.query2D(geo.getRectangle()).size() > 0) {
                return true;
            }

            tree.insert(geo);
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 0 && new File(args[0]).exists()) {
            FileInputStream is = new FileInputStream(new File(args[0]));
            System.setIn(is);
        }

        Record record = new Record(System.in);

        record.getLabels().forEach((key, value) -> {
            PointInterface point = key.getPOI();
            if (value.size() > 0) {
                System.out.println("(" + point.getX() + "," + point.getY() +") overlaps:");

                value.forEach((v) -> {
                    PointInterface p = v.getPOI();

                    System.out.println("\t(" + p.getX() + "," + p.getY() + ")");
                });
            }
        });
    }
}
