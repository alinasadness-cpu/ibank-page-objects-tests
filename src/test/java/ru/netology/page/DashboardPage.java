package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
  
    private ElementsCollection cards = $$(".list__item");
    
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    
    private SelenideElement refreshButton = $("[data-test-id='action-reload']");
    
    public DashboardPage() {
        cards.first().shouldBe(Condition.visible);
    }
    
    public int getCardBalance(int cardIndex) {
        String text = cards.get(cardIndex).text();
        return extractBalance(text);
    }
    
    public int getCardBalance(String cardNumber) {
        for (SelenideElement card : cards) {
            String text = card.text();
            if (text.contains(cardNumber)) {
                return extractBalance(text);
            }
        }
        throw new IllegalArgumentException("Card with number " + cardNumber + " not found");
    }
    
    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish).trim();
        String cleanValue = value.replaceAll("[^0-9]", "");
        return Integer.parseInt(cleanValue);
    }
    
    public TransferPage selectCardForTransfer(int cardIndex) {
        cards.get(cardIndex).$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }
    
    public TransferPage selectCardForTransfer(String cardNumber) {
        for (int i = 0; i < cards.size(); i++) {
            String text = cards.get(i).text();
            if (text.contains(cardNumber)) {
                cards.get(i).$("[data-test-id='action-deposit']").click();
                return new TransferPage();
            }
        }
        throw new IllegalArgumentException("Card with number " + cardNumber + " not found");
    }
    
    public DashboardPage refresh() {
        refreshButton.click();
        cards.first().shouldBe(Condition.visible);
        return this;
    }
}
