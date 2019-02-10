/*
 * author = Jeroen Schols
 */

import interfaces.ParserInterface;
import models.InputRecord;
import models.OutputRecord;
import models.PlacementModelEnum;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser implements ParserInterface {

    @Override
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
            // @TODO definition of used types should be communicated to parser
            // y?[] points = new y?[n];
            // for (int i = 0; i < n; i++) points[i] = new y?(sc.nextInt(), sc.nextInt());
            // rec.points = new x?<>(Arrays.asList(points)); @TODO insert(Collection) should be constructor only
        } catch (NoSuchElementException e) {
            throw new IOException("parser.input: number of points does not correspond to found coordinates");
        }

        sc.close();
        return rec;
    }


    @Override
    public OutputStream output(OutputRecord record) throws NullPointerException {
        if (record == null) throw new NullPointerException("parser.output: record not found");

        return null;
    }
}

