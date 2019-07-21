package com.example.chat_wifidirect.Presenters;

import com.example.chat_wifidirect.Contracts.MessageContract;
import com.example.chat_wifidirect.Models.MessageModel;
import com.example.chat_wifidirect.data.ChatJoinEntity;
import com.example.chat_wifidirect.data.ChatRepository;
import com.example.chat_wifidirect.data.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class MessagePagePresenter implements MessageContract.Presenter {

    private MessageContract.View view;
    ChatRepository chatRepository ;
    ChatJoinEntity chat;

    public MessagePagePresenter(MessageContract.View view) {
        this.view = view;
        this.chatRepository = new ChatRepository();


    }

    @Override
    public void start(Long id) {
        chat = chatRepository.getChatByid(id);
        view.initRecyclerView(getMessages(id));
        view.insertHeader(chat.chat.getName(), chat.chat.getDate());

    }


    @Override
    public String getChatNameByID(long id) {
        return chatRepository.getChatNameByID(id);
    }

    @Override
    public void deleteChat(long id) {
        chatRepository.delete(id);
        view.back();
    }


    public List getMessages(Long id) {
        List<MessageEntity> messages = chat.mesageList;
        List<MessageModel> messages_model= new ArrayList<>();
        for (MessageEntity messageEntity: messages) {
            messages_model.add(new MessageModel(messageEntity.getId(), messageEntity.getChat_id(), messageEntity.isIs_sender(), messageEntity.getMessage(), messageEntity.getDate()));
        }
        return messages_model;
    }
}
