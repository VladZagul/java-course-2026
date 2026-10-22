package bus.model;

public abstract class BaseEntity {
    private final long id;
    private final String name;

    protected BaseEntity(long id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва (маршрут або ім'я пасажира) не може бути порожньою");
        }
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        return id == other.id;
    }

    @Override
    public final int hashCode() {
        return Long.hashCode(id);
    }
}
