public interface Fare {
    String getName();

    double calculateCost(Booking booking);

    default boolean requiresDocument() {
        return false;
    }

    default String describe(Booking booking) {
        String document = requiresDocument() ? "потрібен документ" : "без документів";
        return String.format("%-18s до сплати: %9.2f грн (%s)", getName(), calculateCost(booking), document);
    }
}
