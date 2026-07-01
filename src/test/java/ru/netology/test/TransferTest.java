package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TransferTest {
    private DashboardPage dashboardPage;
    private final String CARD_1_ID = "5559 0000 0000 0001";
    private final String CARD_2_ID = "5559 0000 0000 0002";
    private final int INITIAL_BALANCE = 10000;

    @BeforeEach
    void setUp() {
        LoginPage loginPage = new LoginPage();
        dashboardPage = loginPage.validLogin("vasya", "qwerty123");
    }

    @Test
    void shouldTransferBetweenOwnCards() {
        // Получаем начальные балансы
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_ID);
        int transferAmount = 1000;

        // Выбираем первую карту для пополнения
        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);

        // Выполняем перевод
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_2_ID  // Откуда переводим - со второй карты
        );

        // Обновляем страницу для получения актуальных данных
        updatedDashboard = updatedDashboard.refresh();

        // Получаем обновлённые балансы
        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_ID);

        // Проверяем корректность перевода
        assertEquals(initialFirstBalance + transferAmount, updatedFirstBalance);
        assertEquals(initialSecondBalance - transferAmount, updatedSecondBalance);
    }

    @Test
    void shouldTransferFromSecondToFirst() {
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
        int transferAmount = initialSecondBalance + 1000; // Больше, чем есть на второй карте

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_2_ID
        );

        // Проверяем, что баланс не изменился (перевод не должен пройти)
        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);
        int updatedSecondBalance = updatedDashboard.getCardBalance(CARD_2_ID);

        assertEquals(initialFirstBalance, updatedFirstBalance);
        assertEquals(initialSecondBalance, updatedSecondBalance);

        // Проверяем наличие сообщения об ошибке
        // SelenideElement error = $("[data-test-id='error-notification']");
        // error.shouldBe(Condition.visible);
    }

    @Test
    void shouldNotTransferNegativeAmount() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);
        int initialSecondBalance = dashboardPage.getCardBalance(CARD_2_ID);
        int transferAmount = -1000;

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);
        DashboardPage updatedDashboard = transferPage.transfer(
                String.valueOf(transferAmount),
                CARD_2_ID
        );

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

    @Test
    void shouldNotTransferToNonExistentCard() {
        int initialFirstBalance = dashboardPage.getCardBalance(CARD_1_ID);

        TransferPage transferPage = dashboardPage.selectCardForTransfer(CARD_1_ID);
        // Пытаемся перевести на несуществующую карту
        DashboardPage updatedDashboard = transferPage.transfer("1000", "9999 0000 0000 9999");

        int updatedFirstBalance = updatedDashboard.getCardBalance(CARD_1_ID);

        // Баланс не должен измениться
        assertEquals(initialFirstBalance, updatedFirstBalance);
    }
}