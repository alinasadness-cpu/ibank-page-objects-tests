package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id='login'] input");
    private final SelenideElement passwordField = $("[data-test-id='password'] input");
    private final SelenideElement loginButton = $("[data-test-id='action-login']");
    private final SelenideElement verificationCodeField = $("[data-test-id='code'] input");
    private final SelenideElement verificationButton = $("[data-test-id='action-verify']");
    private final SelenideElement heading = $("h1");

    public DashboardPage validLogin(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();

        // Ждём появления поля для кода
        verificationCodeField.shouldBe(Condition.visible);
        verificationCodeField.setValue("12345");
        verificationButton.click();

        // Ждём загрузки дашборда
        heading.shouldHave(Condition.text("Личный кабинет"));
        return new DashboardPage();
    }
}