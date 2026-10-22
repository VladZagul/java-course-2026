package bus.exception;

public class EntityNotFoundException extends RuntimeException {
    private final long id;

    public EntityNotFoundException(String entityName, long id) {
        super(entityName + " з id = " + id + " не знайдено");
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
