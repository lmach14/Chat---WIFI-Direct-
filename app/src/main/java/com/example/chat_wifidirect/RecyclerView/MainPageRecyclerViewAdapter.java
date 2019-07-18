package com.example.chat_wifidirect.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_wifidirect.Models.ChatJoinModel;

import java.util.List;

public class MainPageRecyclerViewAdapter extends RecyclerView.Adapter<MainPageRecyclerViewAdapter.ChatViewHolder> {

    List<ChatJoinModel> chats;

    public MainPageRecyclerViewAdapter(List<ChatJoinModel> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void updateSourse(List<ChatJoinModel> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {


        public chatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



}
