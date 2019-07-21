package com.example.chat_wifidirect.RecyclerView;

import com.example.chat_wifidirect.Contracts.AdapterContract;
import com.example.chat_wifidirect.Contracts.MainPageContract;

public class MainPageAdapterListener implements AdapterContract.HistoryPageAdapterListener {


    MainPageContract.Presenter presenter;

    public MainPageAdapterListener(MainPageContract.Presenter presenter) {
        this.presenter = presenter;
    }
    @Override
    public void openChat(Long chatId) {
        presenter.openChatActivity(chatId, false);
    }

    @Override
    public void deleteChat(Long chatId) {
        this.presenter.deleteChat(chatId, false);
    }
}
