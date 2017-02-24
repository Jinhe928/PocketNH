package com.pocketnhs.pocketnhsandroid.server;

/**
 * Created by MacBook Pro on 6/14/2016.
 */

public interface DataReadyListener {

    public String MESSAGE_OK = "OK";

    public void DataReady(String message, String requestCode, int dataIndex);
}

