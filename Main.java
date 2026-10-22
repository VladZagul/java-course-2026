import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Скільки замовлень введемо? ");
        int n = scanner.nextInt();
        scanner.nextLine();

        Order[] orders = new Order[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Замовлення #" + (i + 1) + ":");

            System.out.print("  Ім'я клієнта: ");
            String customerName = scanner.nextLine();

            System.out.print("  Назва товару: ");
            String itemName = scanner.nextLine();

            System.out.print("  Кількість: ");
            int quantity = scanner.nextInt();

            System.out.print("  Ціна за одиницю: ");
            double pricePerUnit = scanner.nextDouble();
            scanner.nextLine();

            orders[i] = new Order(customerName, itemName, quantity, pricePerUnit);
        }

        System.out.println();
        System.out.println("===== Усі замовлення =====");
        for (Order order : orders) {
            System.out.println(order);
        }

        System.out.print("\nВведіть поріг суми для підрахунку: ");
        double threshold = scanner.nextDouble();

        int count = 0;
        for (Order order : orders) {
            if (order.getTotalCost() > threshold) {
                count++;
            }
        }
        System.out.printf("Замовлень із сумою більше %.2f: %d%n", threshold, count);

        System.out.println("\n===== До сортування =====");
        for (Order order : orders) {
            System.out.println(order);
        }

        bubbleSortByTotalCost(orders);

        System.out.println("\n===== Після сортування (за сумою, зростання) =====");
        for (Order order : orders) {
            System.out.println(order);
        }

        System.out.println("\nВведіть дані замовлення для пошуку в масиві:");
        System.out.print("  Ім'я клієнта: ");
        String searchCustomer = scanner.nextLine();

        System.out.print("  Назва товару: ");
        String searchItem = scanner.nextLine();

        System.out.print("  Кількість: ");
        int searchQuantity = scanner.nextInt();

        System.out.print("  Ціна за одиницю: ");
        double searchPrice = scanner.nextDouble();

        Order sample = new Order(searchCustomer, searchItem, searchQuantity, searchPrice);
        int foundIndex = linearSearch(orders, sample);

        if (foundIndex != -1) {
            System.out.println("Знайдено збіг на позиції " + foundIndex + ": " + orders[foundIndex]);
        } else {
            System.out.println("Точного збігу не знайдено.");
        }

        scanner.close();
    }

    private static void bubbleSortByTotalCost(Order[] orders) {
        int n = orders.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (orders[j].getTotalCost() > orders[j + 1].getTotalCost()) {
                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                }
            }
        }
    }

    private static int linearSearch(Order[] orders, Order sample) {
        for (int i = 0; i < orders.length; i++) {
            if (orders[i].equals(sample)) {
                return i;
            }
        }
        return -1;
    }
}
