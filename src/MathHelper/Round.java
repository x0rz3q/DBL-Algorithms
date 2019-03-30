package MathHelper;

public class Round {
    public static Double round(double input) {
        return Math.round(input * 1E6) / 1E6;
    }
}
