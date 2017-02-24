package com.pocketnhs.pocketnhsandroid.ui;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.ShowConditionItemDetails;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;

import java.lang.ref.WeakReference;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class AtoZLinksAdapter extends RecyclerView.Adapter<AtoZLinksAdapter.MyViewHolder> {

        private NHSTextLinkPairList mList;
        private Context mContext;
        private WeakReference<Activity> mActivity;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView text;
            public String url;

            public MyViewHolder(View view) {
                super(view);
                text = (TextView) view.findViewById(R.id.linkText);
                Typeface latoBold = Typeface.createFromAsset(text.getContext().getAssets(),  "fonts/Lato-Bold.ttf");
                text.setTypeface(latoBold);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        NHSTextLinkPair pair = mList.list.get(pos);
                        try {
                            //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));
                            //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity.get())
                                    .toBundle();
                            Intent myIntent = new Intent(mActivity.get(), ShowConditionItemDetails.class);
                            ApplicationState.sLastConditionURL = pair.getLink();
                            mContext.startActivity(myIntent,bundle);
                        } catch (ActivityNotFoundException e) {

                        }
                    }
                });

            }
        }

        public AtoZLinksAdapter(NHSTextLinkPairList list, Context context, Activity activity) {
            this.mContext = context;
            this.mActivity = new WeakReference<Activity>(activity);
            this.mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_row_a_to_z, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            NHSTextLinkPair pair = mList.list.get(position);
            holder.text.setText(pair.getText());
            Log.d("bind","text:" + pair.getText());
            holder.url = pair.getLink();
        }

        @Override
        public int getItemCount() {
            return mList.list.size();
        }

}
