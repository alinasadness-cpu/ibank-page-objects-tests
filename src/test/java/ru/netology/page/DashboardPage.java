package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;

public class DashboardPage {
    private ElementsCollection cards = $$("[data-test-id='card']");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private ElementsCollection transferButtons = $$("[data-test-id='action-transfer']");
    private SelenideElement refreshButton = $("[data-test-id='action-refresh']");

    public DashboardPage() {
        // Ждём, пока загрузятся карты
        cards.first().shouldBe(Condition.visible);
    }

    public int getCardBalance(int cardIndex) {
        if (cardIndex < 0 || cardIndex >= cards.size()) {
            throw new IllegalArgumentException("Invalid card index: " + cardIndex);
        }
        String text = cards.get(cardIndex).text();
        return extractBalance(text);
    }

    public int getCardBalance(String cardId) {
        for (SelenideElement card : cards) {
            String text = card.text();
            if (text.contains(cardId)) {
                return extractBalance(text);
            }
        }
        throw new IllegalArgumentException("Card with ID " + cardId + " not found");
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);

        if (start == -1 || finish == -1) {
            throw new IllegalArgumentException("Balance not found in text: " + text);
        }

        String value = text.substring(start + balanceStart.length(), finish).trim();
        String cleanValue = value.replaceAll("[^0-9]", "");

        if (cleanValue.isEmpty()) {
            throw new IllegalArgumentException("No numeric value found in: " + value);
        }

        return Integer.parseInt(cleanValue);
    }

    public TransferPage selectCardForTransfer(int cardIndex) {
        if (cardIndex < 0 || cardIndex >= transferButtons.size()) {
            throw new IllegalArgumentException("Invalid card index: " + cardIndex);
        }
        transferButtons.get(cardIndex).click();
        return new TransferPage();
    }

    public TransferPage selectCardForTransfer(String cardId) {
        for (int i = 0; i < cards.size(); i++) {
            String text = cards.get(i).text();
            if (text.contains(cardId)) {
                transferButtons.get(i).click();
                return new TransferPage();
            }
        }
        throw new IllegalArgumentException("Card with ID " + cardId + " not found");
    }

    public DashboardPage refresh() {
        refreshButton.click();
        // Ждём обновления данных
        cards.first().shouldBe(Condition.visible);
        return this;
    }
}