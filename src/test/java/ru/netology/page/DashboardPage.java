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
    
    
    public int getCardBalance(String maskedCardNumber) {
        SelenideElement card = cards.find(Condition.text(maskedCardNumber));
        String text = card.text();
        return extractBalance(text);
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
    
  
    public TransferPage selectCardForTransfer(String maskedCardNumber) {
        SelenideElement card = cards.find(Condition.text(maskedCardNumber));
        card.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }
    
    public DashboardPage refresh() {
        refreshButton.click();
        cards.first().shouldBe(Condition.visible);
        return this;
    }
}
