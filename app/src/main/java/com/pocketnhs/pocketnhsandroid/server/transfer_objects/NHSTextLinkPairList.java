package com.pocketnhs.pocketnhsandroid.server.transfer_objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class NHSTextLinkPairList {
    public List<NHSTextLinkPair> list;
    public String mListURL;
    public String mListText;

    public NHSTextLinkPairList(){
        list = new ArrayList<NHSTextLinkPair>();
    }

    public void addPair(String text, String url) {
        NHSTextLinkPair pair = new NHSTextLinkPair();
        pair.setLink(url);
        pair.setText(text);
        list.add(pair);

    }
}
