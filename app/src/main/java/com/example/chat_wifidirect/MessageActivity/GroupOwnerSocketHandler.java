package com.example.chat_wifidirect.MessageActivity;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class GroupOwnerSocketHandler extends Thread {

    ServerSocket socket = null;
    private Handler handler;
    private Thread thread;
    public GroupOwnerSocketHandler(Handler handler) throws IOException {
        try {
            socket = new ServerSocket(MessagesActivity.SERVER_PORT);
            this.handler = handler;
        } catch (IOException e) {
            socket.close();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void run() {
            try {
                ChatManager chatManager = new ChatManager(socket.accept(), handler);
                thread = new Thread(chatManager);
                thread.start();
            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed())
                        socket.close();
                } catch (IOException ioe) {

                }
                if(thread != null)
                    thread.interrupt();
                e.printStackTrace();

            }
//        }
    }
}
