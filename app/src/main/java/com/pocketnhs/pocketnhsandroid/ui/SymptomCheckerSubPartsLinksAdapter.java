package com.pocketnhs.pocketnhsandroid.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.MainActivity;
import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.SymptomCheckerGenderConditionsActivity;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;

import java.lang.ref.WeakReference;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class SymptomCheckerSubPartsLinksAdapter extends RecyclerView.Adapter<SymptomCheckerSubPartsLinksAdapter.MyViewHolder> {

    private final WeakReference<Activity> mActivity;
    private NHSTextLinkPairList list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public String url;

        public MyViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.linkText);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NHSTextLinkPair pair = list.list.get(pos);
                    ApplicationState.sLastGenderConditionsUrl = pair.mLink;

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity.get())
                            .toBundle();
                    Intent myIntent = new Intent(mActivity.get(), SymptomCheckerGenderConditionsActivity.class);
                    mActivity.get().startActivity(myIntent, bundle);

                }
            });

        }
    }

    public SymptomCheckerSubPartsLinksAdapter(NHSTextLinkPairList list, WeakReference<Activity> activityWeakReference) {
        this.list = list;
        this.mActivity = activityWeakReference;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NHSTextLinkPair pair = list.list.get(position);
        holder.text.setText(pair.getText());
        Log.d("bind", "text:" + pair.getText());
        holder.url = pair.getLink();
    }

    @Override
    public int getItemCount() {
        Log.e("links adapter", "size:" + list.list.size());
        return list.list.size();
    }

}
