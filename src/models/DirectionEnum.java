package models;

public enum DirectionEnum {
    NW("NW"), NE("NE"), SW("SW"), SE("SE");

    private String direction;
    DirectionEnum(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return this.direction;
    }
}
