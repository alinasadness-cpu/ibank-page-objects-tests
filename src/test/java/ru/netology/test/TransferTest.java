package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    private DashboardPage dashboardPage;
<<<<<<< HEAD

    // ✅ ИСПРАВЛЕНО: данные из DataHelper
    private final String CARD_1_ID = DataHelper.getFirstCard().getId();
    private final String CARD_2_ID = DataHelper.getSecondCard().getId();
    private final int INITIAL_BALANCE = DataHelper.getFirstCard().getBalance();
=======
    private final String CARD_1_ID = "5559 0000 0000 0001";
    private final String CARD_2_ID = "5559 0000 0000 0002";
>>>>>>> 70d1f813f840f055675bcab5f722f3f6c48193be

    @BeforeEach
    void setUp() {
        // ✅ ИСПРАВЛЕНО: данные из DataHelper
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
<<<<<<< HEAD
        var verificationPage = loginPage.validLogin(
                authInfo.getLogin(),
                authInfo.getPassword()
        );
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode.getCode());
=======
        var verificationPage = loginPage.validLogin("vasya", "qwerty123");
        dashboardPage = verificationPage.validVerify("12345");
>>>>>>> 70d1f813f840f055675bcab5f722f3f6c48193be
    }

    @Test
    void shouldTransferFromCard2ToCard1() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_ID);
        int transferAmount = 1000;

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_2_ID
        );

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_ID);

        assertEquals(initialFirstBalance + transferAmount, updatedFirstBalance);
        assertEquals(initialSecondBalance - transferAmount, updatedSecondBalance);
    }

    @Test
    void shouldTransferFromCard1ToCard2() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_ID);
        int transferAmount = 500;

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_2_ID);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_1_ID
        );

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_ID);

        assertEquals(initialFirstBalance - transferAmount, updatedFirstBalance);
        assertEquals(initialSecondBalance + transferAmount, updatedSecondBalance);
    }

    @Test
    void shouldNotTransferMoreThanBalance() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_ID);
        int transferAmount = initialSecondBalance + 1000;

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_2_ID
        );

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_ID);

        assertEquals(initialFirstBalance, updatedFirstBalance);
        assertEquals(initialSecondBalance, updatedSecondBalance);
    }

    @Test
    void shouldNotTransferZeroAmount() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_ID);

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);
        DashboardPage updatedDashboard = transferPage.transfer("0", CARD_2_ID);

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_ID);

        assertEquals(initialFirstBalance, updatedFirstBalance);
        assertEquals(initialSecondBalance, updatedSecondBalance);
    }

    @Test
    void shouldCancelTransfer() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_ID);

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);
        DashboardPage updatedDashboard = transferPage.cancelTransfer();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_ID);

        assertEquals(initialFirstBalance, updatedFirstBalance);
        assertEquals(initialSecondBalance, updatedSecondBalance);
    }
}