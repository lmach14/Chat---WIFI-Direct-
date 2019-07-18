package com.example.chat_wifidirect.Models;

public class MessageModel {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public boolean isIs_sender() {
        return is_sender;
    }

    public void setIs_sender(boolean is_sender) {
        this.is_sender = is_sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageModel(int id, long chat_id, boolean is_sender, String message) {
        this.id = id;
        this.chat_id = chat_id;
        this.is_sender = is_sender;
        this.message = message;
    }

    private long chat_id;

    private boolean is_sender;

    private String message;


}
