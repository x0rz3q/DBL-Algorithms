package models;

import interfaces.models.PointInterface;

import java.util.ArrayList;

public class FourPositionLabel extends AbstractLabel {
    private DirectionEnum direction;

    private FourPositionPoint PoI;

    private ArrayList<FourPositionLabel> conflicts = new ArrayList<>();

    public FourPositionLabel(double x, double y, double height, double aspectRatio, int ID, DirectionEnum direction) {
        super(x, y, height, aspectRatio, ID);
        this.direction = direction;
        this.PoI = new FourPositionPoint(this.poi);
        this.setHeight(height);
    }

    public FourPositionLabel(double height, double aspectRatio, int ID, FourPositionPoint point, DirectionEnum direction) {
        super(point.getX(), point.getY(), height, aspectRatio, ID);
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
        this.direction = direction;

        switch (direction) {
            case NE:
                this.rectangle = new Rectangle(point.getX(), point.getY(),
                        point.getX() + this.height * this.aspectRatio, point.getY() + this.height);
                break;
            case NW:
                this.rectangle = new Rectangle(point.getX() - this.height * this.aspectRatio,
                        point.getY(),
                        point.getX(),
                        point.getY() + this.height
                );
                break;
            case SE:
                this.rectangle = new Rectangle(point.getX(), point.getY() - this.height,
                        point.getX() + this.height * this.aspectRatio, point.getY());
                break;
            case SW:
                this.rectangle = new Rectangle(point.getX() - this.height * this.aspectRatio, point.getY() - this.height,
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

    public DirectionEnum getDirection() {
        String direction = this.getPlacement();
        switch (direction) {
            case "NE": return DirectionEnum.NE;
            case "NW": return DirectionEnum.NW;
            case "SE": return DirectionEnum.SE;
            case "SW": return DirectionEnum.SW;
        }
        throw new IllegalStateException("Has no direction for some fckin reason");
    }

    public static Rectangle[] getAllDirectionRectangles(double pX, double pY, double width, double height) {

        return new Rectangle[] {
                new Rectangle(pX, pY, pX + width, pY + height),
                new Rectangle(pX - width, pY, pX, pY + height),
                new Rectangle(pX, pY - height, pX + width, pY),
                new Rectangle(pX - width, pY - height, pX, pY)
        };
    }

    @Override
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public void setID(int id) {
        this.ID = id;
    }
}
