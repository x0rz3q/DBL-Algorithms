package models;

import interfaces.models.AnchorInterface;

import java.util.ArrayList;

public class FourPositionPoint extends Point {
    public FourPositionPoint(AnchorInterface anchor) {
        super(anchor);
    }

    public FourPositionPoint(double x, double y) {
        super(x, y);
    }

    ArrayList<FourPositionLabel> candidates = new ArrayList<>();

    public void addCandidate(FourPositionLabel label) {
        candidates.add(label);
    }

    public void removeCandidate(FourPositionLabel label) {
        candidates.remove(label);
    }
}
