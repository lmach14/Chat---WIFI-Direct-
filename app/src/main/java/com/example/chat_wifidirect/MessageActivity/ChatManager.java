package com.example.chat_wifidirect.MessageActivity;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class ChatManager implements Runnable {
    private Socket socket = null;
    private Handler handler;
    private ServerSocket socketServer;


    public ChatManager(Socket socket, Handler handler, ServerSocket socketServer) {
        this.socket = socket;
        this.handler = handler;
        this.socketServer = socketServer;
    }
    private InputStream iStream;
    private OutputStream oStream;
    @Override
    public void run() {
        try {
            iStream = socket.getInputStream();
            oStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(MessagesActivity.MY_HANDLE, this).sendToTarget();
            while (true) {
                try {
                    if(Thread.currentThread().isInterrupted())
                        break;
                    bytes = iStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    handler.obtainMessage(MessagesActivity.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {

                    Log.e("iStream_error", "disconnected", e);
//                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                iStream.close();
                oStream.close();
                socket.close();
                if(socketServer != null)
                    socketServer.close();
                iStream = null;
                oStream = null;
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void write(String msg) {
        if(oStream == null)
            return;
        final byte[] buffer = msg.getBytes();
        Thread thread = new Thread() {
            public void run() {
                try {
                    oStream.write(buffer);
                } catch (IOException e) {
                    Log.e("oStream_error", "Exception during write", e);
                }
            }
        };
        thread.start();
    }

    public void close() {
        try {
            Thread.currentThread().interrupt();
            iStream.close();
            oStream.close();
            socket.close();
            iStream = null;
            oStream = null;
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
