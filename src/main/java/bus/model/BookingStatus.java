package bus.model;

public enum BookingStatus {
    NEW("Створене"),
    PAID("Оплачене"),
    BOARDED("Посадка"),
    COMPLETED("Поїздка завершена"),
    CANCELLED("Скасоване");

    private final String title;

    BookingStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
