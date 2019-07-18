package com.example.chat_wifidirect.Models;

import java.util.List;

public class ChatJoinModel {
    public ChatModel getChat() {
        return chat;
    }

    public ChatJoinModel(ChatModel chat, List<MessageModel> message) {
        this.chat = chat;
        this.message = message;
    }

    public void setChat(ChatModel chat) {
        this.chat = chat;
    }

    public List<MessageModel> getMessage() {
        return message;
    }

    public void setMessage(List<MessageModel> message) {
        this.message = message;
    }

    ChatModel chat;
    List<MessageModel> message;
}
