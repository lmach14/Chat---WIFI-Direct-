package com.example.chat_wifidirect.MessageActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Build;
import android.os.Bundle;


import com.example.chat_wifidirect.Contracts.MessageContract;
import com.example.chat_wifidirect.MainActivity;
import com.example.chat_wifidirect.Models.MessageModel;
import com.example.chat_wifidirect.Presenters.MessagePagePresenter;
import com.example.chat_wifidirect.RecyclerView.MessageRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Delete;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat_wifidirect.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity implements MessageContract.View, Handler.Callback, MessageTarget,
        WifiP2pManager.ConnectionInfoListener , DeviceClickListener{
    private Long chat_id;
    private RecyclerView recyclerView;
    private MessageRecyclerViewAdapter adapter;
    private MessagePagePresenter presenter;
    private ImageView delete;
    private ImageView back;
    private TextView name;
    private TextView date;
    private ImageView send_message;
    private TextInputEditText input_text = null;
    /**********************************************/

    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_wifidemotest";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";
    public static final int MESSAGE_READ = 0x400 + 1;
    public static final int MY_HANDLE = 0x400 + 2;
    private WifiP2pManager manager;
    static final int SERVER_PORT = 8888;
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pDnsSdServiceRequest serviceRequest;
    private Handler handler = new Handler(this);
    private String DeviceName;

    private ChatManager chatManager;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /*****************************************************************
     *used material
     *https://android.googlesource.com/platform/development/+/master/samples/WiFiDirectServiceDiscovery?autodive=0%2F%2F
     */

    private void updateView(long id, boolean is_new, String newChatName) {
        setContentView(R.layout.activity_messages);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        delete = findViewById(R.id.delete_message);
        back = findViewById(R.id.back);
        name = findViewById(R.id.chat_name);
        date = findViewById(R.id.chat_date);
        send_message = findViewById(R.id.send_message);
        input_text = findViewById(R.id.input_message);

        this.chat_id = id;
        if (!is_new) {
            input_text.setVisibility(View.GONE);
            send_message.setVisibility(View.GONE);
        }


        recyclerView = findViewById(R.id.message_page_recycler);
        presenter = new MessagePagePresenter(this);

//        String chatName = b.getString("newChatName");
        String chatName = newChatName;
        if (chatName != null) {


            insertHeader(chatName, getDate());
            this.chat_id = presenter.insertChat(chatName,getDate(), 0);
        }


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
                if (input_text.getTextSize() > 0 && !input_text.getText().toString().trim().isEmpty()) {
                    String input = input_text.getText().toString();
                    input_text.getText().clear();

                    presenter.postMessage(chat_id, input, getDate(),true);
                    chatManager.write(input);


                }
            }
        });


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        if (b == null) {
            setContentView(R.layout.loading_peer);
            addBroadcastListener();
            turnOn();

        } else {

            updateView(b.getLong("chat_id"), b.getBoolean("is_new") , null);


        }
    }







    private String getDate() {
        String date = Calendar.getInstance().getTime().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input_d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date input_date = null;
        try {
            input_date = input_d.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output.format(input_date);
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
        if (input_text != null)
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


    private void addBroadcastListener() {
        init();
//        turnOn();

    }

    private void turnOn() {
       
    }


    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel F name";
            String description = "This is a description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( "com.example.TestP2P.notification" ,name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter
                .addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter
                .addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

        startRegistrationAndDiscovery();
    }

    private void startRegistrationAndDiscovery() {
        Map<String, String> record = new HashMap<>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");
        WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
        manager.addLocalService(channel, service, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus("Added Local Service");
            }

            @Override
            public void onFailure(int error) {
                appendStatus("Failed to add a service");
            }
        });
        discoverService();
    }


    private void discoverService() {
        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */
        manager.setDnsSdResponseListeners(channel,
                new WifiP2pManager.DnsSdServiceResponseListener() {
                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {
                        if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {
                            WiFiP2pService service = new WiFiP2pService();
                            service.device = srcDevice;
                            service.instanceName = instanceName;
                            service.serviceRegistrationType = registrationType;
                            DeviceName = srcDevice.deviceName;
                            if(DeviceName == null || DeviceName.equals("")) {
                                appendStatus(DeviceName);
                                DeviceName = "UNKNOWN";
                            }
                            connectP2p(service);
                        }
                    }
                }, new WifiP2pManager.DnsSdTxtRecordListener() {
                    /**
                     * A new TXT record is available. Pick up the advertised
                     * buddy name.
                     */
                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomainName, Map<String, String> record,
                            WifiP2pDevice device) {
                        DeviceName = device.deviceName;
                        Log.d("Tag",
                                device.deviceName + " is "
                                        + record.get(TXTRECORD_PROP_AVAILABLE));
                    }
                });
        // After attaching listeners, create a service request and initiate
        // discovery.
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel, serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        appendStatus("Added service discovery request");
                    }
                    @Override
                    public void onFailure(int arg0) {
                        appendStatus("Failed adding service discovery request");
                    }
                });
        manager.discoverServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus("Service discovery initiated");
            }
            @Override
            public void onFailure(int arg0) {
                appendStatus("Service discovery failed");
            }
        });
    }



    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
        Thread handler = null;
        /*
         * The group owner accepts connections using a server socket and then spawns a
         * client socket for every client. This is handled by {@code
         * GroupOwnerSocketHandler}
         */
        if (p2pInfo.isGroupOwner) {
            Log.d("TAG", "Connected as group owner");
            try {
                handler = new GroupOwnerSocketHandler(
                        ((MessageTarget) this).getHandler());
                handler.start();
            } catch (IOException e) {
                Log.d("TAG",
                        "Failed to create a server thread - " + e.getMessage());
                return;
            }
        } else {
            Log.d("TAG", "Connected as peer");
            handler = new ClientSocketHandler(
                    ((MessageTarget) this).getHandler(),
                    p2pInfo.groupOwnerAddress);
            handler.start();
        }


    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.d("TAG", readMessage);
                presenter.postMessage(chat_id,readMessage,getDate(),false);
//                (chatFragment).pushMessage("Buddy: " + readMessage);
                break;
            case MY_HANDLE:
                Object obj = msg.obj;
                chatManager = (ChatManager) obj;
                updateView(-1, true, DeviceName);
//                Log.d("TAG", );
//                (chatFragment).setChatManager((ChatManager) obj);
        }
        return true;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }


    public void appendStatus(String status) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectP2p(WiFiP2pService service) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = service.device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        if (serviceRequest != null)
            manager.removeServiceRequest(channel, serviceRequest,
                    new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onFailure(int arg0) {
                        }
                    });
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                appendStatus("Connecting to service");
            }
            @Override
            public void onFailure(int errorCode) {
                appendStatus("Failed connecting to service");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


}
