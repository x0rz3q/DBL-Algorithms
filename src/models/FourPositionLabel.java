package models;

import java.util.ArrayList;

public class FourPositionLabel extends PositionLabel {

    private FourPositionPoint PoI = null;

    private ArrayList<FourPositionLabel> conflicts = new ArrayList<>();

    public FourPositionLabel(double x, double y, double height, double aspectRatio, int ID, FourPositionPoint point, DirectionEnum direction) {
        super(x, y, height, aspectRatio, ID, direction);
        PoI = point;
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
}
