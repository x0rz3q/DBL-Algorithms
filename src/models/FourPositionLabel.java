package models;

import interfaces.models.PointInterface;

import java.util.ArrayList;

public class FourPositionLabel extends AbstractLabel {
    protected  DirectionEnum direction;

    private FourPositionPoint PoI = null;

    private ArrayList<FourPositionLabel> conflicts = new ArrayList<>();

    public FourPositionLabel(double x, double y, double height, double aspectRatio, int ID, FourPositionPoint point, DirectionEnum direction) {
        super(x, y, height, aspectRatio, ID);
        this.direction = direction;
        PoI = point;
        this.setHeight(height);
    }

    public FourPositionPoint getPoI() {
        return PoI;
    }

    public ArrayList<FourPositionLabel> getConflicts() {
        return conflicts;
    }

    public void addConflict(FourPositionLabel label) {
        conflicts.add(label);
    }

    public void removeConflict(FourPositionLabel label) {
        conflicts.remove(label);
    }

    public void setDirection(DirectionEnum direction) throws IllegalArgumentException {
        PointInterface point = this.PoI;

        switch (direction) {
            case NE:
                this.rectangle = new Rectangle(point.getX(), point.getY(),
                        point.getX() + this.height * this.aspectRation, point.getY() + this.height);
                break;
            case NW:
                this.rectangle = new Rectangle(point.getX() - this.height * this.aspectRation,
                        point.getY(),
                        point.getX(),
                        point.getY() + this.height
                );
                break;
            case SE:
                this.rectangle = new Rectangle(point.getX(), point.getY() - this.height,
                        point.getX() + this.height * this.aspectRation, point.getY());
                break;
            case SW:
                this.rectangle = new Rectangle(point.getX() - this.height * this.aspectRation, point.getY() - this.height,
                        point.getX(), point.getY());
                break;
            default: throw new IllegalArgumentException("Not a valid direction: " + direction.toString());
        }
    }

    public void setHeight(double height) {
        this.height = height;
        this.setDirection(this.direction);
    }

    @Override
    public String getPlacement() {
        return this.direction.toString();
    }

    @Override
    public Rectangle getRectangle() {
        return this.rectangle;
    }
}
