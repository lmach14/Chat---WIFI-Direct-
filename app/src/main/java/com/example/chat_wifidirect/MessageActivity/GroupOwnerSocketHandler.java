package com.example.chat_wifidirect.MessageActivity;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
            if(socket != null)
                socket.close();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void run() {
            Socket client = null;
            try {
                client = socket.accept();
                ChatManager chatManager = new ChatManager(client, handler, socket);
                thread = new Thread(chatManager);
                thread.start();
            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed())
                        socket.close();
//                    if (client != null && !client.isClosed())
//                        client.close();

                } catch (IOException ioe) {

                }
                if(thread != null)
                    thread.interrupt();
                e.printStackTrace();

            }
//        }
    }
}
