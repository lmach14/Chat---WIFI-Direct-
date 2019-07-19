package com.example.chat_wifidirect;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.chat_wifidirect.Contracts.MainPageContract;
import com.example.chat_wifidirect.Dialogs.DeleteChatDialog;
import com.example.chat_wifidirect.Presenters.MainPagePresenter;
import com.example.chat_wifidirect.RecyclerView.MainPageAdapterListener;
import com.example.chat_wifidirect.RecyclerView.MainPageRecyclerViewAdapter;
import com.example.chat_wifidirect.data.ChatEntity;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainPageContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        RecyclerView recyclerView = findViewById(R.id.main_page_recycler);

        MainPageContract.Presenter presenter = new MainPagePresenter(this);

        ChatEntity c1 = new ChatEntity("luka", "14/12/1223",12);
        ChatEntity c2 = new ChatEntity("gela", "14/12/1223",12);
        ChatEntity c3 = new ChatEntity("bubu", "14/12/1223",15);
        ChatEntity c4 = new ChatEntity("shonzo", "14/12/1223",2);

        ((MainPagePresenter) presenter).insertChat(c1);
        ((MainPagePresenter) presenter).insertChat(c2);
        ((MainPagePresenter) presenter).insertChat(c3);
        ((MainPagePresenter) presenter).insertChat(c4);

        List x = presenter.getChats();
        MainPageRecyclerViewAdapter adapter = new MainPageRecyclerViewAdapter(presenter.getChats());

        adapter.setChatListener(new MainPageAdapterListener(presenter));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    public void showDeleteDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        ft.addToBackStack(null);

        DeleteChatDialog newFragment = DeleteChatDialog.newInstance();
        newFragment.show(ft, "dialog");
    }
}
