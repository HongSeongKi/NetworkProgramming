package com.example.hongseonggi.chatting_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class MyGlobals {
    private int page;
    private static MyGlobals instance = null;

    public BufferedReader getNetworkReader() {
        return networkReader;
    }

    public void setNetworkReader(BufferedReader networkReader) {
        this.networkReader = networkReader;
    }

    public BufferedWriter getNetworkWriter() {
        return networkWriter;
    }

    public void setNetworkWriter(BufferedWriter networkWriter) {
        this.networkWriter = networkWriter;
    }

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;

    public static synchronized MyGlobals getInstance(){
        if(null == instance){
            instance = new MyGlobals();
        }

        return instance;
    }
}
