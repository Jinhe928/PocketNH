package com.pocketnhs.pocketnhsandroid.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.LiveWellWebViewArticleActivity;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSLiveWellData;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSLiveWellDataList;

import java.lang.ref.WeakReference;

/**
 * Created by MacBook Pro on 26.8.2016..
 */
public class LiveWellLinksAdapter extends RecyclerView.Adapter<LiveWellLinksAdapter.LiveWellViewHolder> {

    private NHSLiveWellDataList mList;
    private Context mContext;
    private WeakReference<Activity> mActivity;

    public class LiveWellViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public String titleUrl;

        public TextView subText;

        public TextView linkTitle_1;
        public TextView linkTitle_2;
        public TextView linkTitle_3;


        public String linkUrl_1;
        public String linkUrl_2;
        public String linkUrl_3;

        public ImageView imageView;

        public LiveWellViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.itemTitle1);

            subText = (TextView) view.findViewById(R.id.itemSubtitle);

            imageView  = (ImageView) view.findViewById(R.id.itemImage);

            linkTitle_1 = (TextView) view.findViewById(R.id.itemTitle2);
            linkTitle_2 = (TextView) view.findViewById(R.id.itemTitle3);
            linkTitle_3 = (TextView) view.findViewById(R.id.itemTitle4);

            Typeface latoBold = Typeface.createFromAsset(title.getContext().getAssets(),  "fonts/Lato-Bold.ttf");
            Typeface latoRegular = Typeface.createFromAsset(title.getContext().getAssets(),  "fonts/Lato-Regular.ttf");
            title.setTypeface(latoBold);
            subText.setTypeface(latoRegular);
            linkTitle_1.setTypeface(latoRegular);
            linkTitle_2.setTypeface(latoRegular);
            linkTitle_3.setTypeface(latoRegular);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NHSLiveWellData data = mList.list.get(pos);
                    try {
                        //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));
                        //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Intent myIntent = new Intent(mActivity.get(), LiveWellWebViewArticleActivity.class);
                        ApplicationState.sLastLiveWellURL = data.mTitleLink;
                        mContext.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            });

            linkTitle_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NHSLiveWellData data = mList.list.get(pos);
                    try {
                        //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));
                        //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Intent myIntent = new Intent(mActivity.get(), LiveWellWebViewArticleActivity.class);
                        ApplicationState.sLastLiveWellURL = data.mLink1;
                        mContext.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            });

            linkTitle_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NHSLiveWellData data = mList.list.get(pos);
                    try {
                        //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));
                        //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Intent myIntent = new Intent(mActivity.get(), LiveWellWebViewArticleActivity.class);
                        ApplicationState.sLastLiveWellURL = data.mLink2;
                        mContext.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            });

            linkTitle_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NHSLiveWellData data = mList.list.get(pos);
                    try {
                        //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));
                        //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        Intent myIntent = new Intent(mActivity.get(), LiveWellWebViewArticleActivity.class);
                        ApplicationState.sLastLiveWellURL = data.mLink3;
                        mContext.startActivity(myIntent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            });
        }
    }


    public LiveWellLinksAdapter(NHSLiveWellDataList list, Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = new WeakReference<Activity>(activity);
        this.mList = list;
    }

    @Override
    public LiveWellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row_live_well, parent, false);

        return new LiveWellViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LiveWellViewHolder holder, int position) {
        NHSLiveWellData data = mList.list.get(position);
        holder.title.setText(data.mTitle);
        holder.subText.setText(data.mText);
        holder.linkTitle_1.setText(data.mTitleLink1);
        holder.linkTitle_2.setText(data.mTitleLink2);
        holder.linkTitle_3.setText(data.mTitleLink3);
        Log.d("bind","Title1:" + data.mTitle);
        holder.titleUrl = data.mLink1;
        holder.linkUrl_1 = data.mLink1;
        holder.linkUrl_2 = data.mLink2;
        holder.linkUrl_3 = data.mLink3;
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(data.mImageName, "drawable",
                mContext.getPackageName());
        holder.imageView.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return mList.list.size();
    }

}
