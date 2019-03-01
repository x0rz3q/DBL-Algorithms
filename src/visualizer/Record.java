package visualizer;

import Collections.QuadTree;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import models.*;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Record {
    private HashMap<LabelInterface, Set<LabelInterface>> labels;
    private QuadTree tree;
    private int pointCount = 0;
    private double extent;
    private double xMin = 0;
    private double yMin = 0;

    private String scan(String regex, Scanner scanner) {
        while(!scanner.hasNext(regex)) scanner.next();

        return scanner.next().split(": ")[1];
    }

    public Record(InputStream stream) {
        this.labels = new HashMap<>();
        this.tree = new QuadTree(new Rectangle(0, 0, 10000, 10000));
        Scanner scanner = new Scanner(stream).useDelimiter(System.lineSeparator());

        PlacementModelEnum model = PlacementModelEnum.fromString(scan("placement model: .*", scanner));
        double ratio = Double.parseDouble(scan("aspect ratio: .*", scanner));
        double height = Double.parseDouble(scan("height: .*", scanner));
        this.pointCount = Integer.parseInt(scan("number of points: .*", scanner));

        scanner.nextLine();
        scanner.useDelimiter(Pattern.compile("\\p{javaWhitespace}+"));

        double yMax = 0;
        double xMax = 0;

        while (scanner.hasNext()) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            LabelInterface label = null;

            if (scanner.hasNextDouble()) {
                double alpha = scanner.nextDouble();
                label = new SliderLabel(x, y, height,ratio, alpha, 0);
            } else
                label = new PositionLabel(x, y, height, ratio, 0, DirectionEnum.fromString(scanner.next()));

            Collection<GeometryInterface> intersections = this.tree.query2D(label.getRectangle());

            for (GeometryInterface geo : intersections) {
                if(this.labels.containsKey((LabelInterface) geo)) {
                    this.labels.get((LabelInterface)geo).add(label);
                }
            }

            this.tree.insert(label);
            this.labels.put(label, new HashSet(intersections));

            this.xMin = Math.min(label.getXMin(), this.xMin);
            this.yMin = Math.min(label.getXMax(), this.yMin);
            xMax = Math.max((int)label.getXMax(), xMax);
            yMax = Math.max(label.getYMax(), yMax);
        }

        this.extent = Math.max(xMax - xMin, yMax - yMin);
    }

    public HashMap<LabelInterface, Set<LabelInterface>> getLabels() {
        return labels;
    }

    public int getPointCount() {
        return pointCount;
    }

    public double getExtent() {
        return extent;
    }

    public double getxMin() {
        return xMin;
    }

    public double getyMin() {
        return yMin;
    }
}