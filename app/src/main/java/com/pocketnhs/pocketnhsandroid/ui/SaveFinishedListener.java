package com.pocketnhs.pocketnhsandroid.ui;

import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;

/**
 * Created by MacBook Pro on 9/14/2016.
 */

public interface SaveFinishedListener {

        public String MESSAGE_OK = "OK";

        public void saveFinished(String message, NHSOrganisation organisation);


}
