

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

    public static DirectionEnum fromString(String direction) throws IllegalArgumentException {
        for (DirectionEnum m : DirectionEnum.values()) {
            if (m.toString().equals(direction)) {
                return m;
            }
        }

        throw new IllegalArgumentException(direction + " not a valid placement model");
    }
}
