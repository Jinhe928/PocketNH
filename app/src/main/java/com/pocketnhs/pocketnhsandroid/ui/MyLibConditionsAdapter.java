package com.pocketnhs.pocketnhsandroid.ui;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
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
import com.pocketnhs.pocketnhsandroid.offline_library.LibraryDataManager;
import com.pocketnhs.pocketnhsandroid.offline_library.ShowMyLibArticleActivity;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCondition;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class MyLibConditionsAdapter extends RecyclerView.Adapter<MyLibConditionsAdapter.MyViewHolder> {

        private List<NHSCondition> mList;
        private Context mContext;
        private WeakReference<Activity> mActivity;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            //public TextView text;
            public TextView title;
            public ImageView imageView;
            public ImageView imageViewType;
            public String url;

            public MyViewHolder(View view) {
                super(view);
                //text = (TextView) view.findViewById(R.id.itemSubtitle);
                title = (TextView) view.findViewById(R.id.itemTitle);
                imageViewType = (ImageView) view.findViewById(R.id.itemImageType);
                imageView = (ImageView) view.findViewById(R.id.itemImage);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeleteDialog(getAdapterPosition());
                    }

                    private void showDeleteDialog(final int adapterPosition) {
                        new AlertDialog.Builder(mActivity.get())
                                .setTitle("Remove from library")
                                .setMessage("Are you sure you want to remove the article from library?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        NHSCondition condition = mList.get(adapterPosition);
                                        LibraryDataManager.getInstance().removeFromLibrary(condition);
                                        mList.remove(adapterPosition);
                                        MyLibConditionsAdapter.this.notifyDataSetChanged();

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
                Typeface latoBold = Typeface.createFromAsset(title.getContext().getAssets(),  "fonts/Lato-Bold.ttf");
                Typeface lato = Typeface.createFromAsset(title.getContext().getAssets(),  "fonts/Lato-Regular.ttf");
                //text.setTypeface(lato);
                title.setTypeface(latoBold);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        NHSCondition condition = mList.get(pos);
                        try {
                            ApplicationState.sLastLibraryCondition = condition;
                            //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));
                            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity.get())
                                    .toBundle();
                            Intent myIntent = new Intent(mActivity.get(), ShowMyLibArticleActivity.class);
                            //ApplicationState.sLastURL = pair.mHTMLCode;
                            mActivity.get().startActivity(myIntent, bundle);
                        } catch (ActivityNotFoundException e) {

                        }
                    }
                });
            }
        }

        public MyLibConditionsAdapter(List<NHSCondition> list, Context context, Activity activity) {
            this.mContext = context;
            this.mActivity = new WeakReference<Activity>(activity);
            this.mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_row_library, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            NHSCondition pair = mList.get(position);
            holder.title.setText(pair.mTitle);
            if (pair.type == null){
                holder.imageViewType.setImageResource(R.drawable.health_a_z_saved);
            } else if (pair.type.equals("SC")){
                holder.imageViewType.setImageResource(R.drawable.symptom_checker_saved);
            } else if (pair.type.equals("CHQ")){
                holder.imageViewType.setImageResource(R.drawable.chk_saved);
            } else if (pair.type.equals("AZ")){
                holder.imageViewType.setImageResource(R.drawable.health_a_z_saved);
            }
            //holder.text.setText(pair.mSummary);
            Resources resources = mContext.getResources();

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

}
