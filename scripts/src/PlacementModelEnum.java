

public enum PlacementModelEnum {
    ONE_SLIDER("1slider"), TWO_POS("2pos"), FOUR_POS("4pos");

    private String model;

    PlacementModelEnum(String model) {
        this.model = model;
    }

    public static PlacementModelEnum fromString(String model) throws IllegalArgumentException {
        for (PlacementModelEnum m : PlacementModelEnum.values()) {
            if (m.toString().equals(model)) {
                return m;
            }
        }

        throw new IllegalArgumentException(model + " not a valid placement model");
    }

    @Override
    public String toString() {
        return this.model;
    }
}
