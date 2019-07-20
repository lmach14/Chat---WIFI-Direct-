package com.example.chat_wifidirect.Presenters;

import com.example.chat_wifidirect.Contracts.MainPageContract;
import com.example.chat_wifidirect.Models.ChatModel;
import com.example.chat_wifidirect.data.ChatEntity;
import com.example.chat_wifidirect.data.ChatRepository;
import com.example.chat_wifidirect.data.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class MainPagePresenter implements MainPageContract.Presenter {
    ChatRepository chatRepository ;
    MainPageContract.View activity;

    public MainPagePresenter(MainPageContract.View activity) {
        this.chatRepository = new ChatRepository();
        this.activity = activity;
    }



    public void insertChat(ChatEntity chatEntity, List<MessageEntity> messageEntities) {
        chatRepository.insert(chatEntity, messageEntities);
    }

    @Override
    public List getChats() {
        List<ChatEntity> chats = chatRepository.getAllChats();
        List<ChatModel> chats_m = new ArrayList<>();
        for (ChatEntity chatEntity: chats) {
            chats_m.add(new ChatModel(chatEntity.getId(), chatEntity.getName(), chatEntity.getDate(), chatEntity.getMessage_num()));
        }
        return chats_m;
    }



    @Override
    public void deleteChat(long id, boolean agreed ) {
        if(!agreed) {
            activity.showDeleteDialog(id);
        } else {
            chatRepository.delete(id);
            activity.updateHistory();
        }
    }

    @Override
    public void deleteAllChats() {
        chatRepository.deleteAll();
        activity.updateHistory();
    }
}
