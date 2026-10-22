package bus.exception;

public class InvalidPriceException extends DomainException {
    private final double invalidValue;

    public InvalidPriceException(String message, double invalidValue) {
        super(message);
        this.invalidValue = invalidValue;
    }

    public double getInvalidValue() {
        return invalidValue;
    }
}
