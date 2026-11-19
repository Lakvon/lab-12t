package org.example.model;

import org.example.exceptions.AccountException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CheckingAccount extends BankAccount {
    private double dailyWithdrawalLimit = 300;
    private LocalDate lastWithdrawalDate;
    private BigDecimal withdrawnToday = BigDecimal.ZERO;

    public CheckingAccount(String name, long accountNumber, long userId, String password) {
        super(name, accountNumber, userId, password);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double limit) {
        if (!Double.isFinite(limit) || limit < 0) {
            throw new AccountException("Денний ліміт має бути невід’ємним скінченним числом.");
        }
        dailyWithdrawalLimit = limit;
    }

    @Override
    public void withdraw(double amount) {
        LocalDate today = LocalDate.now();
        BigDecimal previous = today.equals(lastWithdrawalDate) ? withdrawnToday : BigDecimal.ZERO;
        BigDecimal total = previous.add(positiveAmount(amount));
        if (total.compareTo(BigDecimal.valueOf(dailyWithdrawalLimit)) > 0) {
            throw new AccountException("Перевищено денний ліміт зняття.");
        }
        super.withdraw(amount);
        withdrawnToday = total;
        lastWithdrawalDate = today;
    }
}
