package visualizer;

import Collections.QuadTree;
import com.sun.org.apache.regexp.internal.RE;
import interfaces.models.GeometryInterface;
import models.DirectionEnum;
import models.PositionLabel;
import models.Rectangle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Record {
    public HashMap<Rectangle, Boolean> rectangles;
    public int pointCount;
    public int actualPointCount = 0;
    public double extent;
    public int intersections = 0;
    public double shiftX = 0;
    public double shiftY = 0;

    public static Record parse() {
        Record record  = new Record();

        Pattern pattern = Pattern.compile("aspect ratio:(\\s|\\t)*(?<ratio>\\d+(.\\d+){0,1})|number of points:(\\s|\\t)*(?<numpoints>\\d+)|height:(\\s|\\t)*(?<height>\\d+(.\\d+){0,1})|(?<coords>\\d+ \\d+) (?<direction>NW|NE)", RE.MATCH_MULTILINE);
        Scanner scanner = new Scanner(System.in);

        QuadTree tree = new QuadTree(new Rectangle(0, 0, 10000, 10000));
        record.rectangles = new HashMap<>();

        double xMin = Integer.MAX_VALUE;
        double xMax = Integer.MIN_VALUE;
        double yMin = Integer.MAX_VALUE;
        double yMax = Integer.MIN_VALUE;
        double aspectRatio = 0.0;
        double height = 0.0;


        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    if (matcher.group("ratio") != null) {
                        aspectRatio = Double.parseDouble(matcher.group("ratio"));
                    } else if(matcher.group("height") != null) {
                        height = Double.parseDouble(matcher.group("height"));
                    } else if(matcher.group("numpoints") != null) {
                        record.pointCount = Integer.parseInt(matcher.group("numpoints"));
                    } else if (matcher.group("coords") != null) {
                        record.actualPointCount++;
                        String[] split = matcher.group("coords").split(" ");

                        PositionLabel label = new PositionLabel(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                                height, aspectRatio, -1, DirectionEnum.fromString(matcher.group("direction")));

                        xMin = Math.min(label.getXMin(), xMin);
                        xMax = Math.max(label.getXMax(), xMax);
                        yMin = Math.min(label.getYMin(), yMin);
                        yMax = Math.max(label.getYMax(), yMax);

                        record.shiftY = Math.min(record.shiftY, label.getRectangle().getYMin());
                        record.shiftX = Math.min(record.shiftX, label.getRectangle().getXMin());

                        Collection<GeometryInterface> intersection = tree.query2D(label.getRectangle());
                        record.rectangles.put(label.getRectangle(), intersection.size() > 0);
                        tree.insert(label.getRectangle());

                        for (GeometryInterface geom : intersection) {
                            Rectangle rectangle = (Rectangle) geom;
                            record.intersections++;

                            if(!record.rectangles.get(rectangle)) {
                                record.rectangles.put(rectangle, true);
                                record.intersections++;
                            }
                        }
                    }
                }
            }
        }

        record.extent = Math.max(xMax - xMin, yMax - yMin);

        return record;
    }
}
