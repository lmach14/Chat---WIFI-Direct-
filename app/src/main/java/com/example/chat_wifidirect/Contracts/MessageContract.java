package com.example.chat_wifidirect.Contracts;

import com.example.chat_wifidirect.Models.MessageModel;

import java.util.List;

public interface MessageContract {
    interface View {
        void initRecyclerView(List<MessageModel> file);
    }

    interface Presenter {
        void start(Long id);

    }
}


