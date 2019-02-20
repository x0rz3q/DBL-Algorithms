package models;

import interfaces.models.LabelInterface;
import interfaces.models.PointInterface;

public abstract class AbstractLabel implements LabelInterface {
    protected Rectangle rectangle;
    protected int ID;
    protected double aspectRation;
    protected PointInterface poi;

    public AbstractLabel(double x, double y, double size, double aspectRation, int ID) {
        this.poi = new Point(x, y);
        this.ID = ID;
        this.aspectRation = aspectRation;

        this.rectangle = new Rectangle(x, y, size*aspectRation, y);
    }

    @Override
    public PointInterface getPOI() {
        return this.poi;
    }

    public Boolean equals(LabelInterface square) {
        return super.equals(square) && this.getPOI().equals(square.getPOI());
    }

    @Override
    public Integer getID() {
        return ID;
    }
}
