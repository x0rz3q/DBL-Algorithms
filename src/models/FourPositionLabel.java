package models;

import java.util.ArrayList;

public class FourPositionLabel extends PositionLabel {

    private FourPositionPoint PoI = null;

    private ArrayList<FourPositionLabel> conflicts = new ArrayList<>();

    public FourPositionLabel(double x, double y, double size, DirectionEnum direction, int ID, FourPositionPoint point) {
        super(x, y, size, direction, ID);
        PoI = point;
        point.addCandidate(this);
    }

    public FourPositionPoint getPoI() {
        return PoI;
    }

    public ArrayList<FourPositionLabel> getConflicts() {
        return conflicts;
    }

    public void addConflicts(FourPositionLabel label) {
        conflicts.add(label);
    }

    public void removeConflict(FourPositionLabel label) {
        conflicts.remove(label);
    }
}
