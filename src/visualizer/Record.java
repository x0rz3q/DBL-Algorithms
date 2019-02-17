package visualizer;

import Collections.QuadTree;
import com.sun.org.apache.regexp.internal.RE;
import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;
import models.Anchor;
import models.DirectionEnum;
import models.PositionLabel;
import models.Square;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Record {
    public Double aspectRatio;
    public Double height;
    public HashMap<LabelInterface, Collection<SquareInterface>> intersects;
    public int pointCount;
    public int actualPointCount = 0;
    public double extent;

    public static Record parse() {
        Record record  = new Record();

        Pattern pattern = Pattern.compile("aspect ratio:(\\s|\\t)*(?<ratio>\\d+(.\\d+){0,1})|number of points:(\\s|\\t)*(?<numpoints>\\d+)|height:(\\s|\\t)*(?<height>\\d+(.\\d+){0,1})|(?<coords>\\d+ \\d+) (?<direction>NW|NE)", RE.MATCH_MULTILINE);
        Scanner scanner = new Scanner(System.in);

        QuadTree tree = new QuadTree(new Square(new Anchor(0, 0), 10000));
        tree.setDataLimit(5);
        record.intersects = new HashMap<>();

        double xMin = Integer.MAX_VALUE;
        double xMax = Integer.MIN_VALUE;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    if (matcher.group("ratio") != null) {
                        record.aspectRatio = Double.parseDouble(matcher.group("ratio"));
                    } else if(matcher.group("height") != null) {
                        record.height = Double.parseDouble(matcher.group("height"));
                    } else if(matcher.group("numpoints") != null) {
                        record.pointCount = Integer.parseInt(matcher.group("numpoints"));
                    } else if (matcher.group("coords") != null) {
                        record.actualPointCount++;
                        String[] split = matcher.group("coords").split(" ");

                        PositionLabel label = new PositionLabel(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                                record.height, DirectionEnum.fromString(matcher.group("direction")), -1);

                        xMin = Math.min(label.getXMin(), xMin);
                        xMax = Math.max(label.getXMax(), xMax);

                        record.intersects.put(label, new ArrayList<>());
//                        tree.insert(label);
                    }
                }
            }
        }

        record.extent = xMax - xMin;

        return record;
    }
}
