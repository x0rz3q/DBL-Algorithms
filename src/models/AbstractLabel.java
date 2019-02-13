package models;

import interfaces.models.LabelInterface;

public abstract class AbstractLabel extends Square implements LabelInterface {
    protected Point poi;
    protected Integer ID;

    public AbstractLabel(double x, double y, double size, int ID) {
        super(new Anchor(x, y), size);
        this.poi = new Point(x, y);
        this.ID = ID;
    }

    @Override
    public Point getPOI() {
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
