package models;

import interfaces.models.PointInterface;

public class PositionLabel extends AbstractLabel {
    protected DirectionEnum direction;

    public PositionLabel(double x, double y, double height, double aspectRation, int ID, DirectionEnum direction) {
        super(x, y, height, aspectRation, ID);
        this.direction = direction;
        this.setHeight(height);
    }

    public DirectionEnum getDirection() {
        return this.direction;
    }

    public void setDirection(DirectionEnum direction) throws IllegalArgumentException {
        PointInterface point = this.poi;

        switch (direction) {
            case NE:
                this.rectangle = new Rectangle(point.getX(), point.getY(),
                        point.getX() + this.height * this.aspectRation, point.getY() + this.height);
                break;
            case NW:
                this.rectangle = new Rectangle(point.getX() - this.height*this.aspectRation,
                                        point.getY(),
                                        point.getX(),
                                        point.getY() + this.height
                                    );
                break;
            default:
                throw new IllegalArgumentException("Not supported direction");
        }

        this.direction = direction;
    }

    public void setHeight(double height) {
        this.height = height;
        this.setDirection(this.direction);
    }

    @Override
    public String getPlacement() {
        return this.direction.toString();
    }
}
