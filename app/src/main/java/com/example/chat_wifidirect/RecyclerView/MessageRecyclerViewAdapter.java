package com.example.chat_wifidirect.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_wifidirect.Models.MessageModel;
import com.example.chat_wifidirect.R;
import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MessageModel> items;
    private Context con;
    private RecyclerView mRecyclerView;
//    private NoteActyvity.NoteListener noteListener;


    public MessageRecyclerViewAdapter(Context con, List<MessageModel> items) { //, NoteActyvity.NoteListener noteListener
        this.con = con;
//        this.noteListener = noteListener;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).isIs_sender()){
            return 1;
        } else {
            return 2;
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(i == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_text_layout_left, viewGroup, false);
            return new ViewHolderLeft(view);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_text_layout_right, viewGroup, false);
            return new ViewHolderRight(view);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        View view;
        if(viewHolder.getItemViewType() == 1) {
            ViewHolderLeft holder = (ViewHolderLeft) viewHolder;
            holder.message.setText(items.get(i).getMessage());
            holder.date.setText(items.get(i).getDate());
            view = holder.itemView;
        }else {
            ViewHolderRight holder = (ViewHolderRight) viewHolder;
            holder.message.setText(items.get(i).getMessage());
            holder.date.setText(items.get(i).getDate());
            view = holder.itemView;
        }

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TextView t = view.findViewById(R.id.send_date);
                if(t.getVisibility() == View.VISIBLE) {
                    t.setVisibility(View.GONE);
                }else {
                    t.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }else{
            return 0;
        }
    }

    public  class ViewHolderLeft extends RecyclerView.ViewHolder{
        private TextView message;
        private TextView date;
        private View itemView;

        public ViewHolderLeft(View itemView) {
            super(itemView);
            this.itemView = itemView;
            message = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.send_date);

        }
    }

    public  class ViewHolderRight extends RecyclerView.ViewHolder{
        private TextView message;
        private TextView date;
        private View itemView;

        public ViewHolderRight(View itemView) {
            super(itemView);
            this.itemView = itemView;
            message = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.send_date);

        }
    }

}


