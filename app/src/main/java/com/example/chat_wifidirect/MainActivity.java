package com.example.chat_wifidirect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_wifidirect.Contracts.ContractViews;
import com.example.chat_wifidirect.Contracts.MainPageContract;
import com.example.chat_wifidirect.MessageActivity.MessagesActivity;
import com.example.chat_wifidirect.Presenters.MainPagePresenter;
import com.example.chat_wifidirect.RecyclerView.MainPageAdapterListener;
import com.example.chat_wifidirect.RecyclerView.MainPageRecyclerViewAdapter;
import com.example.chat_wifidirect.data.ChatEntity;
import com.example.chat_wifidirect.data.MessageEntity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainPageContract.View {

    private  MainPageContract.Presenter presenter;
    public AtomicLong id;
    RecyclerView recyclerView;
    MainPageRecyclerViewAdapter adapter;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = findViewById(R.id.main_page_recycler);

        presenter = new MainPagePresenter(this);

        ChatEntity c1 = new ChatEntity("luka", "14/12/1223",12);

        MessageEntity m1 = new MessageEntity(-1, "foo",true,"14/03/2019");
        MessageEntity m2 = new MessageEntity(-1, "foo",false,"14/03/2019");
        MessageEntity m3 = new MessageEntity(-1, "foo",true,"14/03/2019");
        MessageEntity m4 = new MessageEntity(-1, "foo",false,"14/03/2019");
        MessageEntity m5 = new MessageEntity(-1, "foo",true,"14/03/2019");

        List<MessageEntity> ml = new ArrayList<>();
        ml.add(m1);
        ml.add(m2);
        ml.add(m3);
        ml.add(m4);
        ml.add(m5);

        ChatEntity c2 = new ChatEntity("gela", "14/12/1223",12);
        ChatEntity c3 = new ChatEntity("bubu", "14/12/1223",15);
        ChatEntity c4 = new ChatEntity("shonzo", "14/12/1223",2);

        ((MainPagePresenter) presenter).insertChat(c1,ml);
        ((MainPagePresenter) presenter).insertChat(c2, null);
        ((MainPagePresenter) presenter).insertChat(c3, null);
        ((MainPagePresenter) presenter).insertChat(c4, null);

        List chats = presenter.getChats();
        adapter = new MainPageRecyclerViewAdapter(chats);

        adapter.setChatListener(new MainPageAdapterListener(presenter));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllChats();
            }
        });


        toolbar.setTitle("ისტორია("+chats.size()+")");




    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Log.d("test","" +  id);
        } else if (id == R.id.nav_gallery) {
            Log.d("test","" +  id);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void showDeleteDialog(long id) {
        this.id = new AtomicLong(id);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        ft.addToBackStack(null);

        DeleteChatDialog newFragment = DeleteChatDialog.newInstance(this);
        newFragment.show(ft, "dialog");


    }

    @Override
    public void updateHistory() {
        List list = presenter.getChats();
        adapter.updateSourse(presenter.getChats());
        toolbar.setTitle("ისტორია("+list.size()+")");
    }

    @Override
    public void deleteAllChats() {
        presenter.deleteAllChats();
        toolbar.setTitle("ისტორია("+0+")");
    }

    @Override
    public void openActivity(Long id, boolean isNew) {
        Intent myIntent = new Intent(MainActivity.this, MessagesActivity.class);
        myIntent.putExtra("chat_id", id);
        myIntent.putExtra("is_new", isNew);
        MainActivity.this.startActivity(myIntent);
    }

    public void deleteChat() {
        presenter.deleteChat(id.get(), true);
    }


    public static class DeleteChatDialog extends DialogFragment {

        private static ContractViews parentActivity;
        public static DeleteChatDialog newInstance(ContractViews parent) {
            parentActivity= parent;
            DeleteChatDialog f = new DeleteChatDialog();
            return f;
        }

        View yes;
        View no;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.delete_chat_pop_up, container, false);
            Activity act = getActivity();
            yes = (Button)v.findViewById(R.id.yes);
            no = (Button)v.findViewById(R.id.no);

//            return super.onCreateView(inflater, container, savedInstanceState);
            return v;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            addListeners();

        }

        public void addListeners() {
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                String newName = et.getText().toString();
//                rp.saveFile(newName, message, path);
                    parentActivity.deleteChat();

                    ((AppCompatActivity)parentActivity).getSupportFragmentManager().popBackStack();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((AppCompatActivity)parentActivity).getSupportFragmentManager().popBackStack();
                }
            });
        }
    }
}
