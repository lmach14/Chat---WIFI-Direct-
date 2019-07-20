package com.example.chat_wifidirect.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.chat_wifidirect.data.ChatJoinEntity;

import java.util.List;


@Dao
public interface DataDao {

    @Query("SELECT * FROM chat")
    List<ChatJoinEntity> selectAll();


    @Query("SELECT * FROM chat where id=:id")
    ChatJoinEntity selectById(Long id);


    @Delete
    void deleteChat(ChatEntity chat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertChat(ChatEntity chat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(MessageEntity messageEntity);

    @Query("select * from message where chat_id=:index")
    List<MessageEntity> selectMessagesByChatId(long index);


    @Query("select * from message")
    List<MessageEntity> selectAllMassages();


    @Query("select * from chat")
    List<ChatEntity> selectAllChats();



}

