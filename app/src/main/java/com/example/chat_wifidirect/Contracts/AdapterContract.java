package com.example.chat_wifidirect.Contracts;

public interface AdapterContract {
    interface HistoryPageAdapterListener {
        void openChat(Long chatId);
        void deleteChat(Long chatId);
    }

    interface AdapterObservable {
        void setListener(final int id);
        void setLongClickListener(final int id);
    }
}


