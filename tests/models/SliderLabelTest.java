package models;

class SliderLabelTest extends AbstractSquareTest {
    @Override
    protected void setInstance() {
        this.anchor = new Anchor(10, 10);
        this.instance = new PositionLabel(10, 10,0.0, DirectionEnum.NE);
        this.edgeLength = 0.0;
    }
}