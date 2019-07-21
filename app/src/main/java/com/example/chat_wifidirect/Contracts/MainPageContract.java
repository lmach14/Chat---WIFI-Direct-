package com.example.chat_wifidirect.Contracts;

import java.util.List;

public interface MainPageContract {
    interface View extends ContractViews {
        void showDeleteDialog(long id);
        void updateHistory();
        void deleteAllChats();
        void openActivity(Long id, boolean isNew);

    }

    interface Presenter {
        void openChatActivity(Long id, boolean isNew);
        List getChats();
        void deleteChat(long id, boolean agreed);
        void deleteAllChats();
        String getChatNameByID(long id);
    }
}
