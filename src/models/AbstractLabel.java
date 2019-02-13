package models;

import interfaces.models.LabelInterface;

public abstract class AbstractLabel extends Square implements LabelInterface {
    protected Point poi;

    public AbstractLabel(double x, double y, double size) {
        super(new Anchor(x, y), size);
        this.poi = new Point(x, y);
    }

    @Override
    public Point getPOI() {
        return this.poi;
    }

    public Boolean equals(LabelInterface square) {
        return super.equals(square) && this.getPOI().equals(square.getPOI());
    }
}
