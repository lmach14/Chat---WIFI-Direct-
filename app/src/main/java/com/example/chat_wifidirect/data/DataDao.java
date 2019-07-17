package com.example.chat_wifidirect.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.chat_wifidirect.data.ChatJoinEntity;


@Dao
public interface DataDao {

    @Query("SELECT * FROM chat")
    ChatJoinEntity selectAll();


    @Query("SELECT * FROM chat where id=:id")
    ChatJoinEntity selectById(Long id);


    @Query("DELETE FROM chat where id=:i")
    void deleteChat(Long i);


}

