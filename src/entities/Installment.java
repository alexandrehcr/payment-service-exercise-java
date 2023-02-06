package entities;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Installment {
    public static String outputFormat = "%-16s %-16s %s";
    private final LocalDate dueDate;
    private final Double amount;

    public Installment(LocalDate dueDate, Double amount) {
        this.dueDate = dueDate;
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        return String.format(outputFormat, dueDate.getDayOfWeek(), dateFormatter.format(dueDate), currencyFormatter.format(getAmount()));
    }
}
