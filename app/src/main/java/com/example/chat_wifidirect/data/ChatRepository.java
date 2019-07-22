package com.example.chat_wifidirect.data;

import android.os.AsyncTask;

import java.util.List;
import java.util.Objects;

public class ChatRepository {
    DataDao dao;

    public ChatRepository() {
        DataBase dataBase = DataBase.getInstance();
        dao = dataBase.dataDao();
    }

    public List<ChatJoinEntity> getAll() {
        List<ChatJoinEntity> chatJoinEntities = dao.selectAll();
        return chatJoinEntities;
    }


    public List<ChatEntity> getAllChats() {
        List<ChatEntity> chatEntities = dao.selectAllChats();
        return chatEntities;
    }

    public ChatJoinEntity getChatByid(Long id ) {
        return dao.selectById(id);
    }

    public void insert(ChatEntity chat, List<MessageEntity> messages) {
        Object[] chatObjects = new Object[2];
        chatObjects[0] = chat;
        chatObjects[1] = messages;
        new InsertChatAsyncTask(dao).doInBackground(chatObjects);


    }
    
    public void delete(long id) {
        ChatJoinEntity chatMessage = dao.selectById(id);
        Object[] chatObjects = new Object[2];
        chatObjects[0] = chatMessage.getChatEntity();
        chatObjects[1] = chatMessage.getMesageList();
        new DeleteChatAsyncTask(dao).doInBackground(chatObjects);
    }

    public List<MessageEntity> selectMessagesByChatId(long index) {
        return dao.selectMessagesByChatId(index);
    }

    public String getChatNameByID(long id) {
        return dao.getChatNameByID(id);
    }

    public void deleteAll() {
        new  DeleteAllAsyncTask(dao).doInBackground();
    }



   public void insertMesseages(MessageEntity messageEntity) {
       Object[] chatObjects = new Object[1];
       chatObjects[0] = messageEntity;
        new InsertMessageAsyncTask(dao).doInBackground(chatObjects);
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
            if(t != null) {
                for (int j = 0; j < t.size(); j++) {
                    t.get(j).setChat_id(i);
                    dao.insertMessage(t.get(j));
                }
            }
            return null;

        }


    }


    private static class InsertMessageAsyncTask extends AsyncTask<Object, Void, Void> {
        DataDao dao;

        private InsertMessageAsyncTask(DataDao dao) {
            this.dao = dao;
        }
        
        @Override
        protected Void doInBackground(Object... objects) {
            MessageEntity messageEntity = (MessageEntity)objects[0];
            dao.insertMessage(messageEntity);
            return null;
        }
    }

    private static class DeleteChatAsyncTask extends AsyncTask<Object, Void, Void> {
        DataDao dao;

        private DeleteChatAsyncTask(DataDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Object... objects) {

            List<MessageEntity> t = (List<MessageEntity>) objects[1];
            ChatEntity chat = (ChatEntity) objects[0];
            List<MessageEntity> x1 = dao.selectMessagesByChatId(chat.getId());
            dao.deleteChat(chat);
//            for (:
//                 ) {
//
//            }
            List<MessageEntity> x = dao.selectMessagesByChatId(chat.getId());
            return null;
        }
    }


    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        DataDao dao;

        private DeleteAllAsyncTask(DataDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            List<MessageEntity> x1 = dao.selectAllMassages();
            dao.delelteAllChats();
            List<MessageEntity> x = dao.selectAllMassages();
            return null;
        }


    }

}
