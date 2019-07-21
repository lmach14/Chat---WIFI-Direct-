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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(i == 1) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_text_layout, viewGroup, false);
            return new ViewHolder(view);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_text_layout, viewGroup, false);
            return new ViewHolder(view);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        ViewHolder holder = (ViewHolder) viewHolder;
        holder.message.setText(items.get(i).getMessage());
        holder.date.setText(items.get(i).getDate());

    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }else{
            return 0;
        }
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView message;
        private TextView date;
//
//        public void setListener(final NoteActyvity.NoteListener listener,final int id) {
//            this.check_box.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.check(id);
//                }
//            });
//        }
//        public void setTextListener(final NoteActyvity.NoteListener listener,final int id) {
//            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        listener.updateText(id, name.getText().toString());
//                    }
//                }
//            });
//        }

        public ViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.send_date);

        }
    }

}


