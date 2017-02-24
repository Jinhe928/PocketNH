package com.pocketnhs.pocketnhsandroid.ui;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.MainActivity;
import com.pocketnhs.pocketnhsandroid.QuizView;
import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.offline_library.ShowGPActivity;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class GPOrganisationsAdapter extends RecyclerView.Adapter<GPOrganisationsAdapter.MyViewHolder> {

        private List<NHSOrganisation> mList;
        private Context mContext;
        private WeakReference<Activity> mActivity;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView text;
            public TextView title;
            public ImageView imageView;
            public String url;

            public MyViewHolder(View view) {
                super(view);
                text = (TextView) view.findViewById(R.id.quizItemSubtitle);
                title = (TextView) view.findViewById(R.id.quizItemTitle);
                imageView = (ImageView) view.findViewById(R.id.quizItemImage);
                Typeface latoBold = Typeface.createFromAsset(text.getContext().getAssets(),  "fonts/Lato-Bold.ttf");
                Typeface lato = Typeface.createFromAsset(text.getContext().getAssets(),  "fonts/Lato-Regular.ttf");
                text.setTypeface(lato);
                title.setTypeface(latoBold);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        NHSOrganisation pair = mList.get(pos);
                        try {
                            //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));
                            Toast.makeText(mContext,"Todo: show detailed View", Toast.LENGTH_SHORT).show();
                            /*Intent myIntent = new Intent(mActivity.get(), QuizView.class);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ApplicationState.sLastURL = pair.mLinkPairSelf.getLink();
                            mContext.startActivity(myIntent);
                            */
                        } catch (ActivityNotFoundException e) {

                        }
                    }
                });

            }
        }

        public GPOrganisationsAdapter(List<NHSOrganisation> list, Context context, Activity activity) {
            this.mContext = context;
            this.mActivity = new WeakReference<Activity>(activity);
            this.mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_row_quizzes, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            NHSOrganisation pair = mList.get(position);
            holder.title.setText(pair.mTitle);
            holder.text.setText(pair.mTelephone);
            Resources resources = mContext.getResources();
            /*final int resourceId = resources.getIdentifier(pair.mImage, "drawable",
                    mContext.getPackageName());
            holder.imageView.setImageResource(resourceId);
            */
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ApplicationState.sLastOrganisation = mList.get(position);
                    ApplicationState.sIsShowingOrganization = true;
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity.get())
                            .toBundle();
                    Intent intent = new Intent(mActivity.get(), ShowGPActivity.class);
                    mActivity.get().startActivity(intent,bundle);
                }
            });
            if (pair.mLinkPairSelf!=null) {
                holder.url = pair.mLinkPairSelf.getLink();
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

}
