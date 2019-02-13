package Parser;
/*
 * @author = Jeroen Schols
 */

import interfaces.AbstractCollectionInterface;
import interfaces.ParserInterface;
import models.PlacementModelEnum;
import models.Point;
import Collections.QuadTree;
import Collections.KDTree;

import java.io.*;
import java.util.*;

class Parser implements ParserInterface {


    @Override
    public DataRecord input(Readable source, Class<? extends AbstractCollectionInterface> collectionClass) throws NullPointerException, IOException {
        if (source == null) throw new NullPointerException("parser.input: source not found");

        DataRecord rec = new DataRecord();
        Scanner sc = new Scanner(source);

        try {
            while (!sc.hasNext("2pos|4pos|1slider")) sc.next();
            switch (sc.next("2pos|4pos|1slider")) {
                case "2pos":
                    rec.placementModel = PlacementModelEnum.TWO_POS;
                    break;
                case "4pos":
                    rec.placementModel = PlacementModelEnum.FOUR_POS;
                    break;
                case "1slider":
                    rec.placementModel = PlacementModelEnum.ONE_SLIDER;
                    break;
            }
        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: no placement model found");
        }

        try {
            while (!sc.hasNextFloat()) sc.next();
            rec.aspectRatio = sc.nextFloat();
        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: no aspect ratio found");
        }

        try {
            while (!sc.hasNextInt()) sc.next();
            int n = sc.nextInt();
            rec.points = new ArrayList<>(n);
            rec.pointsOrig = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                int x = sc.nextInt();
                int y = sc.nextInt();

                Point point = new Point(x, y * rec.aspectRatio);
                rec.points.add(point);
                rec.pointsOrig.add(new DataRecord.CoordinatedPoint(point, x, y));
            }


            if (collectionClass == QuadTree.class) {
                rec.collection = initQuadTree();
            } else if (collectionClass == KDTree.class) {
                rec.collection = initKDTree();
            }

        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: number of points does not correspond to found coordinates");
        }

        sc.close();
        return rec;
    }

    private QuadTree initQuadTree() {
        // @TODO initialize a QuadTree
        return null;
    }

    private KDTree initKDTree() {
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
        }

        writer.write(
            "aspect ratio: " + record.aspectRatio + "\n"
            + "number of points" + record.points.size() + "\n"
            + "height:" + record.height + "\n"
        );

        for (Parser.DataRecord.CoordinatedPoint cPoint : record.pointsOrig) {
            writer.write(cPoint.x + " " + cPoint.y + " " cPoint.point.
//  @TODO           + cPoint.square. "get relative position"
                    + "\n");
        }

        writer.flush();
    }
}

