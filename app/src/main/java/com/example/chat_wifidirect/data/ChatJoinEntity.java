package com.example.chat_wifidirect.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ChatJoinEntity {
    public ChatEntity getChatEntity() {
        return chat;
    }

    public void setChatEntity(ChatEntity chatEntity) {
        this.chat = chatEntity;
    }

    public List<MessageEntity> getMesageList() {
        return mesageList;
    }

    public void setMesageList(List<MessageEntity> mesageList) {
        this.mesageList = mesageList;
    }

    @Embedded
    public ChatEntity chat;

    @Relation(parentColumn = "id",
            entityColumn = "chat_id", entity = MessageEntity.class)
    public List<MessageEntity> mesageList;

}
