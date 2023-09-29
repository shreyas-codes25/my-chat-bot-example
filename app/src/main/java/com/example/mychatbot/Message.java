package com.example.mychatbot;

public class Message {
    public static String SEND_BY_ME="me";
    public static String SEND_BY_BOT="bot";

    public Message(String message, String sendBy) {
        this.message = message;
        this.sendBy = sendBy;
    }

    String message,sendBy;

    public String getMessage() {
        return message;
    }

    public String getSendBy() {
        return sendBy;
    }
}
