package services;

import entities.Contract;
import entities.Installment;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ContractService {
    private OnlinePaymentService onlinePaymentService;
    private List<Installment> installments = new ArrayList<>();

    public ContractService(OnlinePaymentService onlinePaymentService) {
        this.onlinePaymentService = onlinePaymentService;
    }

    public void processContract(Contract contract, Integer months) {
        double baseValue = contract.getTotalValue() / months;
        for (int i = 1; i <= months; i++) {
            double interest = onlinePaymentService.interest(baseValue, i);
            double paymentFee = onlinePaymentService.paymentFee(baseValue + interest);
            double installmentValue = baseValue + interest + paymentFee;
            LocalDate installmentDate = contract.getDate().plusMonths(i);
           if (installmentDate.getDayOfWeek().getValue() == 6) {
               installmentDate = installmentDate.plusDays(2);
           } else if (installmentDate.getDayOfWeek().getValue() == 7) {
               installmentDate = installmentDate.plusDays(1);
           }
            contract.getInstallments().add(new Installment(installmentDate, installmentValue));
        }
    }
}
