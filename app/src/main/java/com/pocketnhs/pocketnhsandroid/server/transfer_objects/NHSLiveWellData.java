package com.pocketnhs.pocketnhsandroid.server.transfer_objects;

/**
 * Created by MacBook Pro on 26.8.2016..
 */
public class NHSLiveWellData {

    public String mTitle;
    public String mTitleLink;

    public String mText;

    public String mTitleLink1;
    public String mLink1;
    public String mTitleLink2;
    public String mLink2;
    public String mTitleLink3;
    public String mLink3;

    public String mImageName;

    public NHSLiveWellData(String mTitle, String mTitleLink, String mText, String mTitleLink1, String mLink1, String mTitleLink2, String mLink2, String mTitleLink3, String mLink3, String mImageName) {
        this.mTitle = mTitle;
        this.mTitleLink = mTitleLink;
        this.mText = mText;
        this.mTitleLink1 = mTitleLink1;
        this.mLink1 = mLink1;
        this.mTitleLink2 = mTitleLink2;
        this.mLink2 = mLink2;
        this.mTitleLink3 = mTitleLink3;
        this.mLink3 = mLink3;
        this.mImageName = mImageName;
    }
}
