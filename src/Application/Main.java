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

        Integer number = null;
        LocalDate date = null;
        Double value = null;
        Integer installmentsAmount = null;
        int errorCount = 0;
        int errorLimit = 3;

        System.out.println("CONTRACT DATA");
        System.out.print("Number: ");
        while (number == null) {
            try {
                number = Integer.parseInt(scanner.nextLine().strip());
                errorCount = 0;
            } catch (NumberFormatException e) {
                if (++errorCount == errorLimit) {
                    throw new NumberFormatException("Reached attempts limit.");
                }
                System.out.print("Invalid input. The contract number must be an integer: ");
            }
        }

        System.out.print("Date (DD/MM/YYYY): ");
        while(date == null) {
            String dateInput = scanner.nextLine().strip();
            for (DateTimeFormatter formatter : dateFormatters) {
                try {
                    date = LocalDate.parse(dateInput, formatter);
                    errorCount = 0;
                    if (formatter != dateFormatters[0] && formatter != dateFormatters[1]) {
                        System.out.println("Parsed date: " + dateFormatters[0].format(date));
                    }
                    break;
                } catch (DateTimeParseException e) {
                    continue;
                }
            }
            if (date == null) {
                if (++errorCount == errorLimit) {
                    throw new DateTimeParseException("Reached attempts limit.", dateInput, -1);
                }
                System.out.print("Invalid date format. Enter a valid date format: ");
            }
        }

        System.out.print("Contract value: ");
        while (value == null) {
            try {
                value = Double.parseDouble(scanner.nextLine().replace(",", ".").strip());
                errorCount = 0;
            } catch (NumberFormatException e) {
                if (++errorCount == errorLimit) {
                    throw new NumberFormatException("Reached attempts limit.");
                }
                System.out.print("Invalid input. Contract value must be a number: ");
            }
        }

        while (installmentsAmount == null) {
            try{
                System.out.print("Installments amount: ");
                installmentsAmount = Integer.parseInt(scanner.nextLine().strip());
            } catch (NumberFormatException e) {
                if (++errorCount == errorLimit) {
                    throw new NumberFormatException("Reached attempts limit.");
                }
                System.out.println("Invalid input. Number of installments must be an integer: ");
            }
        }

        System.out.println();
        Contract contract = new Contract(number, date, value);
        ContractService service = new ContractService(new PayPalService());
        service.processContract(contract, installmentsAmount);

        int dashLength = contract.getInstallments().get(installmentsAmount - 1).toString().length();
        System.out.println("INSTALLMENTS:" );
        System.out.printf(Installment.outputFormat + "\n", "Due Date", "Value");
        System.out.println(String.format("%" + dashLength + "s", " ").replace(" ", "-"));

        for (Installment installment : contract.getInstallments()) {
            System.out.println(installment);
        }
    }
}