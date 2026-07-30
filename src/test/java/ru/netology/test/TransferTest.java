package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    private DashboardPage dashboardPage;

  
    private final String CARD_1_MASKED = DataHelper.getFirstCard().getMaskedNumber();
    private final String CARD_2_MASKED = DataHelper.getSecondCard().getMaskedNumber();
    
   
    private final String CARD_1_FULL = DataHelper.getFirstCard().getNumber();
    private final String CARD_2_FULL = DataHelper.getSecondCard().getNumber();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        
        var authInfo = DataHelper.getAuthInfo();
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(
                authInfo.getLogin(),
                authInfo.getPassword()
        );
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void shouldTransferFromCard2ToCard1() {
        
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_MASKED);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_MASKED);
        int transferAmount = 1000;

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_MASKED);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_2_FULL
        );

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_MASKED);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_MASKED);

        assertEquals(initialFirstBalance + transferAmount, updatedFirstBalance);
        assertEquals(initialSecondBalance - transferAmount, updatedSecondBalance);
    }

    @Test
    void shouldTransferFromCard1ToCard2() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_MASKED);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_MASKED);
        int transferAmount = 500;

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_2_MASKED);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_1_FULL
        );

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_MASKED);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_MASKED);

        assertEquals(initialFirstBalance - transferAmount, updatedFirstBalance);
        assertEquals(initialSecondBalance + transferAmount, updatedSecondBalance);
    }

    @Test
    void shouldNotTransferMoreThanBalance() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_MASKED);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_MASKED);
        int transferAmount = initialSecondBalance + 1000;

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_MASKED);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_2_FULL
        );

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_MASKED);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_MASKED);

        assertEquals(initialFirstBalance, updatedFirstBalance);
        assertEquals(initialSecondBalance, updatedSecondBalance);
    }

    @Test
    void shouldNotTransferZeroAmount() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_MASKED);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_MASKED);

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_MASKED);
        DashboardPage updatedDashboard = transferPage.transfer("0", CARD_2_FULL);

        updatedDashboard = updatedDashboard.refresh();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_MASKED);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_MASKED);

        assertEquals(initialFirstBalance, updatedFirstBalance);
        assertEquals(initialSecondBalance, updatedSecondBalance);
    }

    @Test
    void shouldCancelTransfer() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_MASKED);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_MASKED);

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_MASKED);
        DashboardPage updatedDashboard = transferPage.cancelTransfer();

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_MASKED);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_MASKED);

        assertEquals(initialFirstBalance, updatedFirstBalance);
        assertEquals(initialSecondBalance, updatedSecondBalance);
    }
}
