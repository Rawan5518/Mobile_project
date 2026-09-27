package com.example.firstproject;

public class Message {
    private String messageId;
    private String senderId;
    private String username;
    private String text;
    private long timestamp;

    public Message() {}

    public Message(String messageId, String senderId, String username, String text, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.username = username;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getUsername() { return username; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
}