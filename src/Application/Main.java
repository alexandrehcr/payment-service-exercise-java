package Application;

import entities.Contract;
import entities.Installment;
import services.ContractService;
import services.PayPalService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);
        final DateTimeFormatter[] dateFormatters = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yy"),
                DateTimeFormatter.ofPattern("ddMMyyyy"),
                DateTimeFormatter.ofPattern("ddMMyy")
        };

        int number;
        LocalDate date = null;
        double value;
        int installmentsAmount;

        System.out.println("CONTRACT DATA");
        System.out.print("Number: ");
        number = Integer.parseInt(scanner.nextLine().strip());

        System.out.print("Date (DD/MM/YYYY): ");
        String dateInput = scanner.nextLine().strip();
        for (DateTimeFormatter formatter : dateFormatters) {
            try {
                date = LocalDate.parse(dateInput, formatter);
                if (formatter != dateFormatters[0] && formatter != dateFormatters[1]) {
                    System.out.println("Parsed date: " + dateFormatters[0].format(date));
                }
                break;
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        if (date == null) {
            throw new RuntimeException("Invalid date format.");
        }

        System.out.print("Contract value: ");
        value = Double.parseDouble(scanner.nextLine().replace(",", ".").strip());
        System.out.print("Installments amount: ");
        installmentsAmount = Integer.parseInt(scanner.nextLine().strip());
        System.out.println();

        Contract contract = new Contract(number, date, value);
        ContractService service = new ContractService(new PayPalService());
        service.processContract(contract, installmentsAmount);

        System.out.println("INSTALLMENTS:");
        for (Installment installment : contract.getInstallments()) {
            System.out.println(installment);
        }
    }
}