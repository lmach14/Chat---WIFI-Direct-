package com.example.chat_wifidirect.Contracts;

import java.util.List;

public interface MainPageContract {
    interface View {
        void showDeleteDialog(long id);
        void updateHistory();

    }

    interface Presenter {
        List getChats();
        void deleteChat(long id, boolean agreed);
    }
}
