package com.example.chat_wifidirect.MessageActivity;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class ClientSocketHandler extends Thread {
    private Handler handler;
    private ChatManager chat;
    private InetAddress mAddress;
    private Thread thread;

    public ClientSocketHandler(Handler handler, InetAddress groupOwnerAddress) {
        this.handler = handler;
        this.mAddress = groupOwnerAddress;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
                    MessagesActivity.SERVER_PORT), 5000);
            chat = new ChatManager(socket, handler, null);
            thread = new Thread(chat);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if(socket != null)
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if(thread != null)
                thread.interrupt();
        }
    }

}
