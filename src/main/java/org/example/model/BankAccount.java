package org.example.model;

import org.example.exceptions.AccountException;
import java.math.BigDecimal;

public abstract class BankAccount {
    private String customerName;
    private final long accountNumber;
    private long userId;
    private String userPassword;
    private BigDecimal accountBalance = BigDecimal.ZERO;

    public BankAccount(String customerName, long accountNumber, long userId, String userPassword) {
        this.accountNumber = accountNumber;
        setCustomerName(customerName);
        setUserId(userId);
        setUserPassword(userPassword);
    }

    public final void setCustomerName(String name) {
        if (name == null || name.isBlank()) {
            throw new AccountException("Введіть ім’я власника.");
        }
        customerName = name;
    }

    public String getCustomerName() {
        return customerName;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public long getUserId() {
        return userId;
    }

    public final void setUserId(long id) {
        userId = id;
    }

    public final void setUserPassword(String password) {
        if (password == null || password.length() < 8 || password.chars().noneMatch(Character::isDigit)) {
            throw new AccountException("Пароль має містити щонайменше 8 символів і одну цифру.");
        }
        userPassword = password;
    }

    public boolean matchesPassword(String password) {
        return userPassword.equals(password);
    }

    public double getAccountBalance() {
        return accountBalance.doubleValue();
    }

    public void setAccountBalance(double balance) {
        if (!Double.isFinite(balance) || balance < 0) {
            throw new AccountException("Баланс має бути невід’ємним скінченним числом.");
        }
        accountBalance = BigDecimal.valueOf(balance);
    }

    protected static BigDecimal positiveAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new AccountException("Сума має бути додатним скінченним числом.");
        }
        return BigDecimal.valueOf(amount);
    }

    public void deposit(double amount) {
        BigDecimal balance = accountBalance.add(positiveAmount(amount));
        if (!Double.isFinite(balance.doubleValue())) {
            throw new AccountException("Завелика сума поповнення.");
        }
        accountBalance = balance;
    }

    public void withdraw(double amount) {
        BigDecimal balance = accountBalance.subtract(positiveAmount(amount));
        if (balance.signum() < 0) {
            throw new AccountException("Недостатньо коштів.");
        }
        accountBalance = balance;
    }
}
