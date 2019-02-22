package models;

import interfaces.models.AnchorInterface;
import interfaces.models.LabelInterface;

import java.util.ArrayList;

public class FourPositionPoint extends Square {

    private ArrayList<LabelInterface> candidates = new ArrayList<>();

    public FourPositionPoint(AnchorInterface anchor) {
        super(anchor, 0);
    }

    public void addCandidate(LabelInterface label) {
        candidates.add(label);
    }

    public void removeCandidate(FourPositionLabel label) {
        candidates.remove(label);
    }

    public ArrayList<LabelInterface> getCandidates() { return candidates; }
}
