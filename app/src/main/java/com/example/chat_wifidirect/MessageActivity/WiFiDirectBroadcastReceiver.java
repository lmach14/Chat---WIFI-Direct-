package com.example.chat_wifidirect.MessageActivity;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    MessagesActivity activity;

    public WiFiDirectBroadcastReceiver(MessagesActivity activity) {
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (activity.isManagerNull()) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                activity.afterConnectionAvailable();
            } else {
                if(activity.isConnected())
                    activity.peerIsDesconected();
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
                .equals(action)) {
            WifiP2pDeviceList device = (WifiP2pDeviceList) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);

        }

    }
}
