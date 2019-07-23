package com.example.chat_wifidirect.MessageActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.example.chat_wifidirect.Contracts.MessageContract;
import com.example.chat_wifidirect.MainActivity;
import com.example.chat_wifidirect.Models.MessageModel;
import com.example.chat_wifidirect.Presenters.MessagePagePresenter;
import com.example.chat_wifidirect.RecyclerView.MessageRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat_wifidirect.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessagesActivity extends AppCompatActivity implements MessageContract.View {
    private Long chat_id;
    private RecyclerView recyclerView;
    private MessageRecyclerViewAdapter adapter;
    private MessagePagePresenter presenter;
    private ImageView delete;
    private ImageView back;
    private TextView name;
    private TextView date;
    private ImageView send_message;
    private TextInputEditText input_text;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        delete = findViewById(R.id.delete_message);
        back = findViewById(R.id.back);
        name = findViewById(R.id.chat_name);
        date = findViewById(R.id.chat_date);
        send_message = findViewById(R.id.send_message);
        input_text = findViewById(R.id.input_message);

        Bundle b = getIntent().getExtras();
        chat_id = b.getLong("chat_id");
        if(b.getBoolean("is_new")){
            input_text.setVisibility(View.GONE);
            send_message.setVisibility(View.GONE);
        }
        recyclerView = findViewById(R.id.message_page_recycler);
        presenter = new MessagePagePresenter(this);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        presenter.start(chat_id);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                Fragment prev = getFragmentManager().findFragmentByTag("dialog");

                ft.addToBackStack(null);

                MainActivity.DeleteChatDialog newFragment = MainActivity.DeleteChatDialog.newInstance(MessagesActivity.this, presenter.getChatNameByID(chat_id));
                newFragment.show(ft, "dialog");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        input_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final int end = adapter.getItemCount() - 1;

                if (end > -1) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(end);
                        }
                    }, 100);
                }
                Log.d("test", "test");
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_text.getTextSize() > 0 && !input_text.getText().toString().trim().isEmpty()) {
                    String input = input_text.getText().toString();
                    input_text.getText().clear();
                    String date = Calendar.getInstance().getTime().toString();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat input_d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                    try {
                        Date input_date = input_d.parse(date);
                        presenter.postMessage(chat_id, input,output.format(input_date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });



    }


    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void initRecyclerView(List<MessageModel> file) {
        adapter = new MessageRecyclerViewAdapter(this, file);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        int end = adapter.getItemCount() - 1;
        if (end > -1)
            recyclerView.scrollToPosition(end);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void insertHeader(String name, String date) {
        this.name.setText(name);
        this.date.setText(date);

    }

    @Override
    public void back() {
        input_text.clearFocus();
        Intent myIntent = new Intent( MessagesActivity.this, MainActivity.class);
        finish();
        this.startActivity(myIntent);
    }

    @Override
    public void postMessage(long id) {
        adapter.updateSourse(presenter.getMessages(id));
        presenter.start(id);
    }

    @Override
    public void deleteChat() {
        presenter.deleteChat(chat_id);
    }
}
