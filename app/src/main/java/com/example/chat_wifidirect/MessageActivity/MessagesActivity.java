package com.example.chat_wifidirect.MessageActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class MessagesActivity extends AppCompatActivity implements MessageContract.View{
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
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 999;
    public static final int MESSAGE_READ = 0x400 + 1;
    public static final int MY_HANDLE = 0x400 + 2;
    WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiP2pDnsSdServiceRequest wifiP2pServiceRequest;
    public static int SERVER_PORT = 4545;
    private WiFiDirectBroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private TextView textView;
    private WifiP2pDnsSdServiceInfo serviceInfo;
    private AtomicBoolean isConnected = new AtomicBoolean(false);
    private WifiDevice device;
    private ChatManager chatManager;
    private TextView deviceName;
    private boolean forChat = false;
    Thread thread;


    /*****************************************************************
     *used material
     *https://android.googlesource.com/platform/development/+/master/samples/WiFiDirectServiceDiscovery?autodive=0%2F%2F
     *****************************************************************/

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
            forChat = true;
            setContentView(R.layout.loading_peer);
            deviceName = findViewById(R.id.connected_device);
            findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(device != null) {
                        connect(device.device.deviceAddress);
                    }
                }
            });
            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            init();

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
        if(forChat) {
            onStop();
        }

        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
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



    public void appendStatus(String status) {
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }


    private void init() {


        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        addIntentFilters();
        /*add service info*/
        Map record = new HashMap();
        record.put("listenport", String.valueOf(SERVER_PORT));
        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
        record.put("available", "visible");
        serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);
        addLocalService(serviceInfo);
        addListenerForFoundService();
        addServiceRequest();

    }


    /*add intent filters*/
    private void addIntentFilters() {
        intentFilter = new IntentFilter();
        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }
    }

    /*add local service*/
    private void addLocalService(final WifiP2pDnsSdServiceInfo serviceInfo) {
        manager.clearLocalServices(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i) {
                        Log.d("addLocalService_fail",getDeviceStatus(i));
                    }
                });
            }

            @Override
            public void onFailure(int i) {
                Log.d("clearLocalServices_fail",getDeviceStatus(i));
            }
        });

    }


    /*add Listeners for incoming service*/
    private void addListenerForFoundService() {
        manager.setDnsSdResponseListeners(channel,
                new WifiP2pManager.DnsSdServiceResponseListener() {
                    @Override
                    public void onDnsSdServiceAvailable(String s, String s1, WifiP2pDevice wifiP2pDevice) {
                        if(wifiP2pDevice!=null) {
                            WifiDevice wifiDevice = new WifiDevice();
                            wifiDevice.device.deviceName = wifiP2pDevice.deviceName == "" ? "Unknown" : wifiP2pDevice.deviceName;
                            wifiDevice.device.deviceAddress = wifiP2pDevice.deviceAddress;
                            wifiDevice.device.primaryDeviceType = wifiP2pDevice.primaryDeviceType;
                            wifiDevice.instanceName = s;
                            wifiDevice.serviceRegistrationType = s1;
                            if (device == null) {
                                device = wifiDevice;
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                deviceName.setText(wifiDevice.device.deviceName );
                            } else {
                                if (device.device.deviceName != wifiDevice.device.deviceName) {
                                    Log.d("sameDevice_error", device.device.deviceName + " => " + wifiDevice.device.deviceName);
                                }


                            }
                        }

                    }
                }, new WifiP2pManager.DnsSdTxtRecordListener() {
                    @Override
                    public void onDnsSdTxtRecordAvailable(String s, Map<String, String> map, WifiP2pDevice wifiP2pDevice) {

                    }
                }
        );
        wifiP2pServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
    }

    /*request services*/
    private void addServiceRequest() {
        manager.clearServiceRequests(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                manager.addServiceRequest(channel, wifiP2pServiceRequest, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        manager.discoverServices(channel, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailure(int i) {
                                Log.d("discoverServices_fail",getDeviceStatus(i));
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i) {
                        Log.d("addServiceRequest_fail",getDeviceStatus(i));
                    }
                });
            }

            @Override
            public void onFailure(int i) {
                Log.d("clearServiceReques_fail",getDeviceStatus(i));
            }
        });
    }

    /*remove group*/
    public void removeGroup() {
        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                if(wifiP2pGroup != null) {
                    if(manager != null && channel != null) {
                        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                isConnected.set(false);
                            }

                            @Override
                            public void onFailure(int i) {
                                Log.d("removeGroup_fail",isConnected + " => " + getDeviceStatus(i));
                                removeGroup();
                            }
                        });
                    }
                }
            }
        });


    }

    public static String getDeviceStatus(int statusCode) {
        switch (statusCode){
            case WifiP2pManager.BUSY:
                return "busy";
            case WifiP2pManager.ERROR:
                return "error";
            case WifiP2pManager.P2P_UNSUPPORTED:
                return "p2p unsupported";
            default:return "Unknown";
        }
    }

    public boolean isManagerNull() {
        return manager == null;
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void peerIsDesconected() {
        onBackPressed();
    }

    public void afterConnectionAvailable() {

        Log.d("Test","ტესტ");

        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                if(wifiP2pGroup != null && device == null) {
                    if(wifiP2pGroup.getClientList().toArray().length == 0)
                        return;
                    WifiP2pDevice wifiP2pDevice = (WifiP2pDevice)(wifiP2pGroup.getClientList().toArray()[0]);
                    device = new WifiDevice();
                    device.device.deviceName = wifiP2pDevice.deviceName == "" ? "Unknown" : wifiP2pDevice.deviceName;;
                    device.device.deviceAddress = wifiP2pDevice.deviceAddress;
                }
            }
        });

        manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {


            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                thread = null;

                if (wifiP2pInfo.isGroupOwner) {
                    try {
                        thread = new GroupOwnerSocketHandler(handler);
                        thread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        onBackPressed();
                        return;
                    }

                } else {
                    thread = new ClientSocketHandler(
                            handler,
                            wifiP2pInfo.groupOwnerAddress);
                    thread.start();
                }

            }
        });

    }

    private void connectWifiDirect(final String deviceAddress) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        if (wifiP2pServiceRequest != null)
            manager.removeServiceRequest(channel, wifiP2pServiceRequest,
                    new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i) {
                            Log.d("removeServiceRequ_fail",getDeviceStatus(i));
                        }
                    });
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onFailure(int errorCode) {
                Log.d("connect_fail",getDeviceStatus(errorCode));
            }
        });

    }

    public void connect(final String deviceAddress) {
        if(isConnected.get()) {
            manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    connectWifiDirect(deviceAddress);
                }

                @Override
                public void onFailure(int i) {
                    Log.d("cancelConnect_fail", getDeviceStatus(i));
                }
            });
        } else {
            connectWifiDirect(deviceAddress);
        }
    }

    public void disconnect() {
        if(isConnected.get()) {
            manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    isConnected.set(false);

                }

                @Override
                public void onFailure(int i) {
                    Log.d("cancelConnect_fail", getDeviceStatus(i));
                    removeGroup();
                    manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            isConnected.set(false);

                        }

                        @Override
                        public void onFailure(int i) {
                            Log.d("cancelConnect2_fail", getDeviceStatus(i));
                        }
                    });
                }
            });
        }
    }

    public void removeRequests() {
        if(serviceInfo != null) {
            manager.removeLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i) {
                    Log.d("removeLocalService_fail", getDeviceStatus(i));
                }
            });
        }
        if(wifiP2pServiceRequest != null) {
            manager.removeServiceRequest(channel, wifiP2pServiceRequest, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i) {
                    Log.d("removeServiceRequ_fail", getDeviceStatus(i));
                }
            });
        }
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) message.obj;
                    String readMessage = new String(readBuf, 0, message.arg1);
                    presenter.postMessage(chat_id,readMessage,getDate(),false);
                    break;
                case MY_HANDLE:
                    Object obj = message.obj;
                    chatManager = ((ChatManager) obj);
                    isConnected.set(true);
                    if(device == null) {
                        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
                            @Override
                            public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
                                if(wifiP2pGroup != null && device == null) {
                                    if(wifiP2pGroup.getClientList().toArray().length == 0)
                                        return;
                                    WifiP2pDevice wifiP2pDevice = (WifiP2pDevice)(wifiP2pGroup.getClientList().toArray()[0]);
                                    device = new WifiDevice();
                                    device.device.deviceName = wifiP2pDevice.deviceName == "" ? "Unknown" : wifiP2pDevice.deviceName;;
                                    device.device.deviceAddress = wifiP2pDevice.deviceAddress;
                                }
                            }
                        });
                    }
                    updateView(-1, true, device.device.deviceName);

            }
            return true;
        }
    });

    @Override
    protected void onStop() {
        if(forChat) {
            removeRequests();
            disconnect();
            removeGroup();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        onStop();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(this);
        try {
            registerReceiver(receiver, intentFilter);
        } catch (Exception e) {

        }

    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }

    }

}
