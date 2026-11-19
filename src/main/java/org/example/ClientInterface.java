package org.example;

import org.example.exceptions.AccountException;
import org.example.model.CheckingAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final Map<Long, CheckingAccount> accounts = new HashMap<>();
    private long nextAccountNumber = 1;

    public static void main(String[] args) {
        new ClientInterface().start();
    }

    public void start() {
        while (true) {
            System.out.println("""
                    1 — Створити рахунок
                    2 — Видалити рахунок
                    3 — Поповнити рахунок
                    4 — Зняти кошти
                    5 — Переглянути рахунок
                    6 — Вийти""");
            try {
                switch (read("Оберіть дію").strip()) {
                    case "1" -> createAccount();
                    case "2" -> deleteAccount();
                    case "3" -> {
                        authenticate().deposit(readAmount());
                        System.out.println("Рахунок поповнено.");
                    }
                    case "4" -> {
                        authenticate().withdraw(readAmount());
                        System.out.println("Кошти знято.");
                    }
                    case "5" -> printInfo(authenticate());
                    case "6" -> { return; }
                    default -> throw new AccountException("Виберіть пункт від 1 до 6.");
                }
            } catch (AccountException e) {
                System.err.println("Помилка: " + e.getMessage());
            } catch (NoSuchElementException endOfInput) {
                return;
            }
        }
    }

    private String read(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    private long readId() {
        try {
            return Long.parseLong(read("ID користувача").strip());
        } catch (NumberFormatException e) {
            throw new AccountException("ID має бути цілим числом.");
        }
    }

    private double readAmount() {
        try {
            return Double.parseDouble(read("Сума").strip().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new AccountException("Введіть числову суму.");
        }
    }

    private void createAccount() {
        String name = read("Ім’я власника").strip();
        long id = readId();
        if (accounts.containsKey(id)) {
            throw new AccountException("Цей ID вже використовується.");
        }
        String password = read("Пароль");
        if (!password.equals(read("Підтвердіть пароль"))) {
            throw new AccountException("Паролі не збігаються.");
        }
        CheckingAccount account = new CheckingAccount(name, nextAccountNumber, id, password);
        accounts.put(id, account);
        nextAccountNumber++;
        System.out.println("Рахунок створено.");
    }

    private CheckingAccount authenticate() {
        long id = readId();
        String password = read("Пароль");
        CheckingAccount account = accounts.get(id);
        if (account == null || !account.matchesPassword(password)) {
            throw new AccountException("Неправильний ID або пароль.");
        }
        return account;
    }

    private void deleteAccount() {
        CheckingAccount account = authenticate();
        if (account.getAccountBalance() != 0) {
            throw new AccountException("Для видалення баланс має дорівнювати нулю.");
        }
        accounts.remove(account.getUserId());
        System.out.println("Рахунок видалено.");
    }

    private void printInfo(CheckingAccount account) {
        System.out.printf("Власник: %s%nНомер рахунку: %d%nID: %d%nТип: Checking%nБаланс: %s%nДенний ліміт: %s%n",
                account.getCustomerName(), account.getAccountNumber(), account.getUserId(),
                account.getAccountBalance(), account.getDailyWithdrawalLimit());
    }
}
