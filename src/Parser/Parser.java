package Parser;
/*
 * @author = Jeroen Schols
 */

import interfaces.AbstractCollectionInterface;
import interfaces.ParserInterface;
import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;
import models.PlacementModelEnum;

import java.io.*;
import java.util.*;

class Parser implements ParserInterface {

     // The class that is used for implementing the AbstractCollectionInterface
     private Class<? extends AbstractCollectionInterface> collectionClass;

     // The class that is used for implementing the PointInterface
     private Class<? extends SquareInterface> pointClass;

    /**
     * Create a parser that parses input-and-output files using collectionClass<pointClass> as datastructure
     *
     * @param collectionClass The class used for implementing {@link AbstractCollectionInterface}
     * @param pointClass The class used for implementing {@link LabelInterface}
     */
     Parser (Class<? extends AbstractCollectionInterface> collectionClass, Class<? extends SquareInterface> pointClass) {
         if (collectionClass == null) {
             throw new NullPointerException("parser.input: class implementing AbstractCollectionInterface not found");
         }
         if (pointClass == null) {
             throw new NullPointerException("parser.input: class implementing SquareInterface not found");
         }

         this.collectionClass = collectionClass;
         this.pointClass = pointClass;
     }


    public DataRecord input(Readable source) throws NullPointerException, IOException {
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
            List<SquareInterface> points = new ArrayList<>(n);
            List<Parser.DataRecord.CoordinatedPoint> pointsOrig = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {

                int x = sc.nextInt();
                int y = sc.nextInt();

                SquareInterface point = pointClass.newInstance();
//  @TODO       point.setLoc((float) x, y * rec.aspectRatio);
                point.setAnchor();
                point.setSize(0d, 0d);
                points.add(point);

                DataRecord.CoordinatedPoint cPoint = new Parser.DataRecord.CoordinatedPoint(point, x, y);
                pointsOrig.add(cPoint);
            }

            rec.points = points;

            rec.labels = collectionClass.newInstance();
            rec.labels.insert(points);

            rec.pointsOrig = pointsOrig;

        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: number of points does not correspond to found coordinates");
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }

        sc.close();
        return rec;
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
            writer.write(cPoint.x + " " + cPoint.y + " "
//  @TODO           + cPoint.square. "get relative position"
                    + "\n");
        }

        writer.flush();
    }
}

