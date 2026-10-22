public class InvalidQuantityException extends DomainException {
    private final int invalidValue;

    public InvalidQuantityException(String message, int invalidValue) {
        super(message);
        this.invalidValue = invalidValue;
    }

    public int getInvalidValue() {
        return invalidValue;
    }
}
