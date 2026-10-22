package bus.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
