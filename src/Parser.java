/*
 * author = Jeroen Schols
 */

import interfaces.AbstractCollectionInterface;
import interfaces.ParserInterface;
import interfaces.models.PointInterface;
import models.InputRecord;
import models.OutputRecord;
import models.PlacementModelEnum;

import java.io.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser implements ParserInterface {

     private PointInterface[] points;
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
            points = new PointInterface[n];

            for (int i = 0; i < n; i++) {
                points[i] = pointClass.newInstance();
                // @TODO setting of coordinates not yet supported
                // points[i].setX = sc.nextInt();
                // points[i].setY = sc.nextInt();
            }

            rec.points = collectionClass.newInstance();
            rec.points.insert(Arrays.asList(points));
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

        for (int i = 0; i < points.length; i++) {
            // get each point and output it in order
        }

        writer.flush();
    }
}

