package com.example.chat_wifidirect.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_wifidirect.Contracts.AdapterContract;
import com.example.chat_wifidirect.Models.ChatJoinModel;
import com.example.chat_wifidirect.R;

import java.util.List;

public class MainPageRecyclerViewAdapter extends RecyclerView.Adapter<MainPageRecyclerViewAdapter.ChatViewHolder> {

    List<ChatJoinModel> chats;
    AdapterContract.HistoryPageAdapterListener listener;


    public void setChatListener(AdapterContract.HistoryPageAdapterListener listener) {
        this.listener = listener;
    }

    public MainPageRecyclerViewAdapter(List<ChatJoinModel> chats) {
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_header, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatJoinModel model = chats.get(position);

        holder.person.setText(model.getChat().getName());
        holder.date.setText(model.getChat().getDate());
        holder.message_count.setText(model.getChat().getMessage_num());


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void updateSourse(List<ChatJoinModel> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView person;
        TextView date;
        TextView message_count;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.person = itemView.findViewById(R.id.chat_name);
            this.date = itemView.findViewById(R.id.create_date);
            this.message_count = itemView.findViewById(R.id.message_count);

        }
    }



}
