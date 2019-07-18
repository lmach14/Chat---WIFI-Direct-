package com.example.chat_wifidirect.data;

import android.os.AsyncTask;

import java.util.List;

public class ChatRepository {
    DataDao dao;

    public ChatRepository() {
        DataBase dataBase = DataBase.getInstance();
        dao = dataBase.dataDao();
    }

    public List<ChatJoinEntity> getAllNotes() {
        List<ChatJoinEntity> chatJoinEntities = dao.selectAll();
        return chatJoinEntities;
    }


    public void insert(ChatEntity chat, List<MessageEntity> messages) {
        Object[] chatObjects = new Object[2];
        chatObjects[0] = chat;
        chatObjects[1] = messages;


    }

    private static class InsertChatAsyncTask extends AsyncTask<Object, Void, Void> {

        DataDao dao;

        private InsertChatAsyncTask(DataDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Object... object) {
            List<MessageEntity> t = (List<MessageEntity>) object[1];
            ChatEntity chat = (ChatEntity) object[0];
            long i = dao.insertChat(chat);
            for (int j = 0; j < t.size(); j++) {
                t.get(j).setChat_id(i);
                dao.insertMessage(t.get(j));
            }

            return null;

        }


    }

}