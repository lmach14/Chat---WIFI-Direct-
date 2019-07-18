package com.example.chat_wifidirect.Models;

public class ChatModel {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMessage_num() {
        return message_num;
    }

    public void setMessage_num(int message_num) {
        this.message_num = message_num;
    }

    public ChatModel(int id, String name, String date, int message_num) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.message_num = message_num;
    }

    private int id;

    private String name;

    private String date;

    private int message_num;
}
