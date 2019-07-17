package com.example.chat_wifidirect.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface DataDao {

//    @Query("SELECT * FROM main where description like :name")
//    List<MainDataModel> getMainData(String name);
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateMain(DataMainEntity dataMainEntity);
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void updateItem(DataItemsEntity dataItemsEntity);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long insertMainData(DataMainEntity dataMainEntity);
//
//    @Query("SELECT * FROM items where main_id = :main_id")
//    List<ItemDataModel>  getitemsData(int main_id);
//
//    @Query("SELECT * FROM items where id = :id")
//    DataItemsEntity  getItemData(int id);
//
//    @Query("SELECT * FROM main where id = :id")
//    DataMainEntity getNoteData(int id);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    long insertItemsData(DataItemsEntity dataItemsEntity);
}

