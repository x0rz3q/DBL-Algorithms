/*
 * author = Jeroen Schols
 */

import interfaces.AbstractCollectionInterface;
import interfaces.ParserInterface;
import interfaces.models.LabelInterface;
import interfaces.models.PointInterface;
import models.InputRecord;
import models.OutputRecord;
import models.PlacementModelEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Parser implements ParserInterface {

     private ArrayList<PointInterface> points;
     private Class<? extends AbstractCollectionInterface> collectionClass;
     private Class<? extends PointInterface> pointClass;


     Parser (Class<? extends AbstractCollectionInterface> collectionClass, Class<? extends PointInterface> pointClass) {
         if (collectionClass == null) {
             throw new NullPointerException("parser.input: class implementing AbstractCollectionInterface not found");
         }
         if (pointClass == null) {
             throw new NullPointerException("parser.input: class implementing PointInterface not found");
         }

         this.collectionClass = collectionClass;
         this.pointClass = pointClass;
     }


    public InputRecord input(Readable source) throws NullPointerException, IOException {
        if (source == null) throw new NullPointerException("parser.input: source not found");

        InputRecord rec = new InputRecord();
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
            points = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                PointInterface point = pointClass.newInstance();
                // @TODO setting of coordinates not yet supported
                // point.setX = sc.nextInt();
                // point.setY = sc.nextInt();
                points.add(point);
            }

            rec.points = collectionClass.newInstance();
            rec.points.insert(points);
        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: number of points does not correspond to found coordinates");
        } catch (InstantiationException|IllegalAccessException e) {
            e.printStackTrace();
        }

        sc.close();
        return rec;
    }

    @Override
    public OutputStream output(OutputRecord record) throws NullPointerException {
        return null;
    }

    // suggestion of how to work with output
    public void output2(OutputRecord record, OutputStream stream) throws NullPointerException {
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

        String[] output = new String[record.points.size()];

        for (LabelInterface label : record.points) {
            PointInterface point = label.getPOI();
            int i = points.indexOf(point);
            if (i < 0) throw new NullPointerException("parser.output: point" + point + "unknown");
            output[i] = point.getX() + " " + point.getY() + " " +
                    // label position with respect to point +
                    "\n";
        }

        for (int i = 0; i < record.points.size(); i++) {
            writer.write(output[i]);
        }

        writer.flush();
    }
}

