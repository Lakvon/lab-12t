package org.example;

import org.example.exceptions.*;
import org.example.model.CheckingAccount;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LabTests {
    @Test
    void timeOperations() {
        TimeSpan time = new TimeSpan();
        time.add(90);
        time.add(1, 50);
        time.subtract(new TimeSpan(20));
        time.scale(2);
        assertEquals(6, time.getHours());
        assertEquals(0, time.getMinutes());
        TimeSpan copy = new TimeSpan(time);
        copy.subtract(0, 15);
        assertEquals(5.75, copy.getTotalHours());
        assertEquals(360, time.getTotalMinutes());
    }

    @Test
    void invalidIntervalsLeaveTimeUnchanged() {
        TimeSpan time = new TimeSpan(10);
        assertThrows(IllegalInputTimeSpanException.class, () -> time.add(1, 60));
        assertThrows(IllegalInputTimeSpanException.class, () -> time.subtract(-1));
        assertThrows(NegativeTimeSpanException.class, () -> time.subtract(11));
        assertThrows(IllegalScaleFactorException.class, () -> time.scale(0));
        assertEquals(10, time.getTotalMinutes());
    }

    @Test
    void passwordAndBalanceChecks() {
        assertThrows(AccountException.class, () -> new CheckingAccount("Анна", 1, 7, "password"));
        CheckingAccount account = new CheckingAccount("Анна", 1, 7, "password1");
        assertEquals(0.0, account.getAccountBalance());
        assertThrows(AccountException.class, () -> account.withdraw(5));
        assertThrows(AccountException.class, () -> account.deposit(0));
        assertThrows(AccountException.class, () -> account.deposit(Double.NaN));
        account.deposit(0.3);
        account.withdraw(0.1);
        account.withdraw(0.2);
        assertEquals(0.0, account.getAccountBalance());
    }

    @Test
    void dailyLimitAppliesToSumOfWithdrawals() {
        CheckingAccount account = new CheckingAccount("Анна", 1, 7, "password1");
        account.deposit(500);
        account.withdraw(200);
        assertThrows(AccountException.class, () -> account.withdraw(101));
        account.withdraw(100);
        assertEquals(200.0, account.getAccountBalance());
        account.setDailyWithdrawalLimit(350);
        account.withdraw(50);
        assertEquals(150.0, account.getAccountBalance());
    }
}
