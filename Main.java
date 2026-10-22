import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            runOrderProcessing(scanner);
        } finally {
            System.out.println("\nРоботу програми завершено.");
            scanner.close();
        }
    }

    private static void runOrderProcessing(Scanner scanner) {

        int n = 0;
        boolean validCount = false;
        while (!validCount) {
            System.out.print("Скільки замовлень введемо? ");
            try {
                n = scanner.nextInt();
                scanner.nextLine();
                validCount = true;
            } catch (InputMismatchException e) {
                System.out.println("Помилка: потрібно ввести ціле число. Спробуйте ще раз.");
                scanner.nextLine();
            }
        }

        Order[] orders = new Order[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Замовлення #" + (i + 1) + ":");

            System.out.print("  Ім'я клієнта: ");
            String customerName = scanner.nextLine();

            System.out.print("  Назва товару: ");
            String itemName = scanner.nextLine();

            int quantity = 0;
            double pricePerUnit = 0;
            try {
                System.out.print("  Кількість: ");
                quantity = scanner.nextInt();

                System.out.print("  Ціна за одиницю: ");
                pricePerUnit = scanner.nextDouble();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Помилка: кількість і ціна мають бути числами. Це замовлення пропущено.");
                scanner.nextLine();
                i--; 
                continue;
            }

            try {
                orders[i] = createOrderWithLogging(customerName, itemName, quantity, pricePerUnit);
            } catch (InvalidQuantityException e) {
                System.out.println("Некоректна кількість (" + e.getInvalidValue() + "): " + e.getMessage());
                i--;
            } catch (InvalidPriceException e) {
                System.out.println("Некоректна ціна (" + e.getInvalidValue() + "): " + e.getMessage());
                i--;
            } catch (DomainException e) {
                System.out.println("Доменна помилка: " + e.getMessage());
                i--;
            }
        }

        System.out.println();
        System.out.println("===== Усі замовлення =====");
        for (Order order : orders) {
            System.out.println(order);
        }

        System.out.print("\nНа скільки клієнтів розділити загальну суму (для середнього)? ");
        try {
            int divisor = scanner.nextInt();
            scanner.nextLine();
            int totalCostInt = 0;
            for (Order order : orders) {
                totalCostInt += (int) order.getTotalCost();
            }
            int average = totalCostInt / divisor;
            System.out.println("Середня сума (ціла): " + average);
        } catch (ArithmeticException e) {
            System.out.println("Помилка: ділення на нуль неможливе. " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Помилка: потрібно було ввести ціле число.");
            scanner.nextLine();
        }

        System.out.print("\nВведіть індекс замовлення для перегляду: ");
        try {
            int index = scanner.nextInt();
            scanner.nextLine();
            Order selected = orders[index]; 
            System.out.println("Обране замовлення: " + selected);
        } catch (InputMismatchException e) {

            System.out.println("Помилка: індекс має бути цілим числом.");
            scanner.nextLine();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Помилка: такого індексу немає в масиві (допустимо 0.." + (orders.length - 1) + ").");
        } catch (Exception e) {
            System.out.println("Непередбачена помилка: " + e.getMessage());
        }

        System.out.println("\n--- Демонстрація ієрархії власних винятків ---");
        DomainException[] demoExceptions = {
            new InvalidPriceException("демо: від'ємна ціна", -5.0),
            new InvalidQuantityException("демо: нульова кількість", 0)
        };
        for (DomainException demo : demoExceptions) {
            try {
                throw demo;
            } catch (DomainException e) {
                System.out.println("Перехоплено як DomainException -> насправді: "
                        + e.getClass().getSimpleName() + " | " + e.getMessage());
            }
        }
    }

    private static Order createOrderWithLogging(String customerName, String itemName, int quantity, double pricePerUnit)
            throws DomainException {
        try {
            return new Order(customerName, itemName, quantity, pricePerUnit);
        } catch (InvalidPriceException | InvalidQuantityException e) {
            System.out.println("[LOG] Під час створення замовлення сталася помилка: " + e.getMessage());
            throw e;
        }
    }
}
