package models;

import interfaces.models.LabelInterface;
import interfaces.models.PointInterface;

import java.util.ArrayList;

public class FourPositionPoint extends Point {

    private int id;

    private FourPositionLabel originalRecordLabel;

    private ArrayList<FourPositionLabel> candidates = new ArrayList<>();

    public FourPositionPoint(FourPositionLabel label) {
        super(label.getXMin(), label.getYMin());
        originalRecordLabel = label;
    }

    public void addCandidate(FourPositionLabel label) {
        candidates.add(label);
    }

    public void removeCandidate(FourPositionLabel label) {
        candidates.remove(label);
    }

    public ArrayList<FourPositionLabel> getCandidates() { return candidates; }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public FourPositionLabel getOriginalRecordLabel() {
        return originalRecordLabel;
    }
}
