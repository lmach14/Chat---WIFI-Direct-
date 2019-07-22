package com.example.chat_wifidirect.Contracts;

import com.example.chat_wifidirect.Models.MessageModel;

import java.util.List;

public interface MessageContract {
    interface View extends ContractViews{
        void initRecyclerView(List<MessageModel> file);
        void insertHeader(String name, String date);
        void back();
        void postMessage(long id);
    }

    interface Presenter {
        void start(Long id);
        void deleteChat(long id);
        String getChatNameByID(long id);
        void postMessage(long id, String text, String date);

    }
}


