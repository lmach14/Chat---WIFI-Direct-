package com.example.chat_wifidirect.data;

import com.example.chat_wifidirect.App;

import androidx.room.Room;
import androidx.room.RoomDatabase;



@androidx.room.Database(entities = {MessageEntity.class, ChatEntity.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "app_database";

    private static DataBase INSTANCE;

    private static final Object lock = new Object();

    public abstract DataDao dataDao();

    public static DataBase getInstance(){
        synchronized (lock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                        App.getContext(),
                        DataBase.class,
                        DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

}


