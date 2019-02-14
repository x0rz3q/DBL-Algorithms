package Parser;
/*
 * @author = Jeroen Schols
 */

import interfaces.AbstractCollectionInterface;
import interfaces.ParserInterface;
import interfaces.models.LabelInterface;
import models.*;
import Collections.QuadTree;
import Collections.KDTree;

import java.io.*;
import java.util.*;

public class Parser implements ParserInterface {
    @Override
    public DataRecord input(InputStream source, Class<? extends AbstractCollectionInterface> collectionClass) throws NullPointerException, IOException {
        if (source == null) throw new NullPointerException("parser.input: source not found");

        DataRecord rec = new DataRecord();
        Scanner sc = new Scanner(source);

        if (!sc.hasNext()) {
            throw new IllegalArgumentException("Parser.input.pre violated: source length is zero");
        }

        try {
            while (!sc.hasNext("2pos|4pos|1slider")) sc.next();
            rec.placementModel = PlacementModelEnum.fromString(sc.next("2pos|4pos|1slider"));
        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: no placement model found");
        }

        try {
            while (!sc.hasNextFloat()) sc.next();
            rec.aspectRatio = sc.nextFloat();
        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: no aspect ratio found");
        }

        double xMin = 10001;
        double yMin = 10001;
        double xMax = -1;
        double yMax = -1;

        try {
            while (!sc.hasNextInt()) sc.next();
            int n = sc.nextInt();
            rec.points = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                int x = sc.nextInt();
                int y = sc.nextInt();
                if (x < 0 || x > 10000 || y < 0 || y > 10000) {
                    throw new InputMismatchException("parser.input coordinates not in range {0,1,...,10000}");
                }

                xMin = Math.min(xMin, x);
                yMin = Math.min(yMin, y);
                xMax = Math.max(xMax, x);
                yMax = Math.max(yMax, y);

                LabelInterface label = null;
                switch (rec.placementModel) {
                    case TWO_POS:
                    case FOUR_POS:
                        label = new PositionLabel(x, y*rec.aspectRatio, 0, DirectionEnum.NE, i);
                        break;
                    case ONE_SLIDER:
                        label = new SliderLabel(x, y*rec.aspectRatio, 0, 0, i);
                        break;
                }

                rec.points.add(label);
            }
        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: number of points does not correspond to found coordinates");
        }

        if (collectionClass == QuadTree.class) {
            rec.collection = new QuadTree(new Square(new Anchor(xMin, yMin), Math.max(yMax - yMin, xMax - xMin)),
                                            rec.points);
        } else if (collectionClass == KDTree.class) {
            rec.collection = initKDTree(rec.points);
        } else {
            throw new InputMismatchException("parser.input collection class initializer undefined");
        }

        sc.close();
        return rec;
    }

    private QuadTree initQuadTree(Collection<LabelInterface> points) {
        return new QuadTree(points);
    }

    private KDTree initKDTree(Collection<LabelInterface> points) {
        // @TODO initialize a KDTree
        return null;
    }

    @Override
    public void output(DataRecord record, OutputStream stream) throws NullPointerException {
        if (record == null) throw new NullPointerException("parser.output: record not found");

        PrintWriter writer = new PrintWriter(stream);

        switch (record.placementModel) {
            case TWO_POS:
                writer.print("placement model: 2pos\n");
                break;
            case FOUR_POS:
                writer.print("placement model: 4pos\n");
                break;
            case ONE_SLIDER:
                writer.print("placement model: 1slider\n");
                break;
            default:
                throw new NoSuchElementException("parser.output placement model unknown");
        }

        writer.write(
            "aspect ratio: " + record.aspectRatio + "\n"
            + "number of points: " + record.points.size() + "\n"
            + "height: " + record.height + "\n"
        );

        for (LabelInterface label : record.points) {
            if (label.getPOI().getEdgeLength() != 0) {
                throw new IllegalStateException("parser.output POI of label not of width/height 0");
            }
            writer.write( Math.round(label.getPOI().getXMin()) + " " + Math.round(label.getPOI().getYMin() / record.aspectRatio) + " " + label.getPlacement() + "\n");
        }

        writer.flush();
    }
}