package com.example.chat_wifidirect.Presenters;

import com.example.chat_wifidirect.Contracts.MessageContract;
import com.example.chat_wifidirect.Models.MessageModel;
import com.example.chat_wifidirect.data.ChatRepository;
import com.example.chat_wifidirect.data.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class MessagePagePresenter implements MessageContract.Presenter {

    private MessageContract.View view;
    ChatRepository chatRepository ;

    public MessagePagePresenter(MessageContract.View view) {
        this.view = view;
        this.chatRepository = new ChatRepository();

    }

    @Override
    public void start(Long id) {
        view.initRecyclerView(getMessages(id));
    }


    @Override
    public void deleteChat(long id) {
        chatRepository.delete(id);
        view.back();
    }


    public List getMessages(Long id) {
        List<MessageEntity> messages = chatRepository.selectMessagesByChatId(id);
        List<MessageModel> messages_model= new ArrayList<>();
        for (MessageEntity messageEntity: messages) {
            messages_model.add(new MessageModel(messageEntity.getId(), messageEntity.getChat_id(), messageEntity.isIs_sender(), messageEntity.getMessage(), messageEntity.getDate()));
        }
        return messages_model;
    }
}
