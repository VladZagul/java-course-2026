import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Система бронювання квитків на автобус =====");
        System.out.println();

    
        System.out.print("Введіть маршрут (напр. Київ - Львів): ");
        String route = scanner.nextLine();

        System.out.print("Введіть ПІБ пасажира: ");
        String passengerName = scanner.nextLine();

    
        System.out.print("Введіть номер місця (1-50): ");
        int seatNumber = scanner.nextInt();

       
        System.out.print("Введіть кількість квитків: ");
        int quantity = scanner.nextInt();

        
        System.out.print("Введіть базову ціну одного квитка (грн): ");
        double basePrice = scanner.nextDouble();

       
        System.out.print("Введіть знижку у відсотках (0, якщо немає): ");
        double discountPercent = scanner.nextDouble();

        scanner.nextLine(); 
        double priceAfterDiscountPerTicket = basePrice * (1 - discountPercent / 100);
        double totalPrice = priceAfterDiscountPerTicket * quantity;
        double totalSaved = (basePrice * quantity) - totalPrice;

    
        System.out.println();
        System.out.println("================ Підсумок бронювання ================");
        System.out.printf("Маршрут:                 %s%n", route);
        System.out.printf("Пасажир:                 %s%n", passengerName);
        System.out.printf("Номер місця:             %d%n", seatNumber);
        System.out.printf("Кількість квитків:       %d%n", quantity);
        System.out.printf("Ціна за квиток (базова): %.2f грн%n", basePrice);
        System.out.printf("Знижка:                  %.2f%%%n", discountPercent);
        System.out.printf("Ціна за квиток зі знижкою:%.2f грн%n", priceAfterDiscountPerTicket);
        System.out.printf("Заощаджено:              %.2f грн%n", totalSaved);
        System.out.printf("До сплати загалом:       %.2f грн%n", totalPrice);
        System.out.println("=======================================================");

        scanner.close();
    }
}
