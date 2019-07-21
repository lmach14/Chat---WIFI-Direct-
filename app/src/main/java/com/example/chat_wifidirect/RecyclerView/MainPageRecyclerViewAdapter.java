package com.example.chat_wifidirect.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_wifidirect.Contracts.AdapterContract;
import com.example.chat_wifidirect.Models.ChatModel;
import com.example.chat_wifidirect.R;

import java.util.List;

public class MainPageRecyclerViewAdapter extends RecyclerView.Adapter<MainPageRecyclerViewAdapter.ChatViewHolder> {

    List<ChatModel> chats;
    AdapterContract.HistoryPageAdapterListener listener;


    public void setChatListener(AdapterContract.HistoryPageAdapterListener listener) {
        this.listener = listener;
    }

    public MainPageRecyclerViewAdapter(List<ChatModel> chats) {
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
        ChatModel model = chats.get(position);

        holder.person.setText(model.getName());
        holder.date.setText(model.getDate());
        holder.message_count.setText(model.getMessage_num()+"");

        holder.setListener(model.getId());
        holder.setLongClickListener(model.getId());

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void updateSourse(List<ChatModel> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements AdapterContract.AdapterObservable {
        TextView person;
        TextView date;
        TextView message_count;

        @Override
        public void setListener(final int id) {
            ((View)(person.getParent())).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.openChat((long) id);
                }
            });
        }


        @Override
        public void setLongClickListener(final int id) {
            ((View)(person.getParent())).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    view.setBackgroundColor(Color.BLUE);
                    listener.deleteChat((long)id);
                    return true;
                }
            });
        }
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            this.person = itemView.findViewById(R.id.message_text);
            this.date = itemView.findViewById(R.id.send_date);
            this.message_count = itemView.findViewById(R.id.message_count);



        }
    }



}
