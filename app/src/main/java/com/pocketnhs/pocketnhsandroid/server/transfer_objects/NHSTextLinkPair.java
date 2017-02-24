package com.pocketnhs.pocketnhsandroid.server.transfer_objects;

/**
 * Created by MacBook Pro on 8/24/2016.
 */
public class NHSTextLinkPair {
    public String mText;
    public String mLink;

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }
}
