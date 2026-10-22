package bus.fare;

import bus.fare.external.StateBenefitSystem;

public class FareFactory {
    private static final String[] CODES = {"full", "student", "child", "group", "benefit"};

    private FareFactory() {
    }

    public static Fare create(String code) {
        switch (code.trim().toLowerCase()) {
            case "full":
                return new FullFare();
            case "student":
                return new StudentFare();
            case "child":
                return new ChildFare();
            case "group":
                return new GroupFare();
            case "benefit":
                return new BenefitFareAdapter(new StateBenefitSystem());
            default:
                throw new IllegalArgumentException("Невідомий тариф: " + code);
        }
    }

    public static Fare[] createAll() {
        Fare[] fares = new Fare[CODES.length];
        for (int i = 0; i < CODES.length; i++) {
            fares[i] = create(CODES[i]);
        }
        return fares;
    }

    public static String[] getCodes() {
        return CODES.clone();
    }
}
