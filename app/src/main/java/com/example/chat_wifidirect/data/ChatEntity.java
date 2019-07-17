package com.example.chat_wifidirect.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat")
public class ChatEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "message_num")
    private int message_num;

    public ChatEntity (String name, String date, int message_num) {
        this.name = name;
        this.date = date;
        this.message_num = message_num;
    }

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
}


