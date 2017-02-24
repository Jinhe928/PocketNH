package com.pocketnhs.pocketnhsandroid.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.CommonHQDetailsTextActivity_Depth4;
import com.pocketnhs.pocketnhsandroid.CommonHQShowArticleActivity_Depth5;
import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class CommonHealthQuestionsDetailsAdapter_Depth3 extends RecyclerView.Adapter<CommonHealthQuestionsDetailsAdapter_Depth3.MyViewHolder> {

    private List<NHSTextLinkPair> list;
    private Context mContext;
    private WeakReference<Activity> mActivity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;


        public MyViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.linkText);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    String url = list.get(pos).mLink;
                    if (isCategory(url)) {
                        ApplicationState.lastQuestionsTextUrl = list.get(pos).mLink;
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity.get())
                                .toBundle();
                        Intent intent = new Intent(mActivity.get(), CommonHQDetailsTextActivity_Depth4.class);
                        mActivity.get().startActivity(intent, bundle);
                    }else if (isArticle(url)){
                        ApplicationState.sLastCHQArticleURL = list.get(pos).mLink;
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity.get())
                                .toBundle();
                        Intent intent = new Intent(mActivity.get(), CommonHQShowArticleActivity_Depth5.class);
                        mActivity.get().startActivity(intent,bundle);
                    }

                }

                private boolean isArticle(String url) {
                    if (url.contains("chq/articles/")){
                        return true;
                    }else{
                        return false;
                    }
                }

                private boolean isCategory(String url) {
                    if (url.contains("chq/categories/")){
                        return true;
                    }else{
                        return false;
                    }
                }
            });

        }
    }

    public CommonHealthQuestionsDetailsAdapter_Depth3(List<NHSTextLinkPair> list, Context context, WeakReference<Activity> activity) {
        this.mContext = context;
        this.mActivity = activity;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(list.get(position).mText);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
