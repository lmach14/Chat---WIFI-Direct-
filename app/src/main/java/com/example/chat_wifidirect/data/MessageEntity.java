package com.example.chat_wifidirect.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class MessageEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "chat_id")
    private long chat_id;

    @ColumnInfo(name = "is_sender")
    private boolean is_sender;

    @ColumnInfo(name = "message")
    private String message;

    public MessageEntity(boolean is_sender, String message, long chat_id) {
        this.chat_id = chat_id;
        this.is_sender = is_sender;
        this.message = message;
    }


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
}

