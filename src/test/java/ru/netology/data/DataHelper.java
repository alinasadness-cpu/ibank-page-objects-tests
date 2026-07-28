package ru.netology.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {}

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @Value
    public static class CardInfo {
        private String id;
        private int balance;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static CardInfo getFirstCard() {
        return new CardInfo("5559 0000 0000 0001", 10000);
    }

    public static CardInfo getSecondCard() {
        return new CardInfo("5559 0000 0000 0002", 10000);
    }
}