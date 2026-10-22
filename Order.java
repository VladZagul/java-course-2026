import java.util.Objects;

public class Order {
    private String customerName;
    private String itemName;
    private int quantity;
    private double pricePerUnit;

    public Order(String customerName, String itemName, int quantity, double pricePerUnit)
            throws InvalidPriceException, InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException(
                "Кількість товару має бути додатньою, отримано: " + quantity, quantity
            );
        }
        if (pricePerUnit <= 0) {
            throw new InvalidPriceException(
                "Ціна за одиницю має бути додатньою, отримано: " + pricePerUnit, pricePerUnit
            );
        }

        this.customerName = customerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public double getTotalCost() {
        return quantity * pricePerUnit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public String toString() {
        return String.format(
            "Клієнт: %-15s Товар: %-15s Кількість: %-4d Ціна: %-8.2f Сума: %.2f",
            customerName, itemName, quantity, pricePerUnit, getTotalCost()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Order other = (Order) obj;
        return quantity == other.quantity
                && Double.compare(pricePerUnit, other.pricePerUnit) == 0
                && customerName.equals(other.customerName)
                && itemName.equals(other.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName, itemName, quantity, pricePerUnit);
    }
}
