package com.example.chat_wifidirect.MessageActivity;

import android.os.Bundle;

import com.example.chat_wifidirect.Contracts.MessageContract;
import com.example.chat_wifidirect.Models.MessageModel;
import com.example.chat_wifidirect.Presenters.MessagePagePresenter;
import com.example.chat_wifidirect.RecyclerView.MessageRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;

import com.example.chat_wifidirect.R;

import java.util.List;

public class MessagesActivity extends AppCompatActivity implements MessageContract.View {
    private Long chat_id;
    private RecyclerView recyclerView;
    private MessageRecyclerViewAdapter adapter;
    private MessagePagePresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        chat_id = b.getLong("chat_id");
        recyclerView = findViewById(R.id.message_page_recycler);
        presenter = new MessagePagePresenter(this);

        presenter.start(chat_id);


    }


    @Override
    public void initRecyclerView(List<MessageModel> file) {
        adapter = new MessageRecyclerViewAdapter(this, file);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

}
