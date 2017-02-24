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

import com.pocketnhs.pocketnhsandroid.QuizView;
import com.pocketnhs.pocketnhsandroid.QuizzesListActivity;
import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSQuizDetails;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by MacBook Pro on 8/24/2016.
 */

public class QuizesLinksAdapter extends RecyclerView.Adapter<QuizesLinksAdapter.MyViewHolder> {

    private List<NHSQuizDetails> mList;
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
            Typeface latoBold = Typeface.createFromAsset(text.getContext().getAssets(), "fonts/Lato-Bold.ttf");
            Typeface lato = Typeface.createFromAsset(text.getContext().getAssets(), "fonts/Lato-Regular.ttf");
            text.setTypeface(lato);
            title.setTypeface(latoBold);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    NHSQuizDetails pair = mList.get(pos);
                    //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pair.mLink));

                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity.get())
                            .toBundle();
                    Intent myIntent = new Intent(mActivity.get(), QuizView.class);
                    ApplicationState.sLastQuizURL = pair.mHTMLCode;
                    mActivity.get().startActivity(myIntent, bundle);

                }
            });

        }
    }

    public QuizesLinksAdapter(List<NHSQuizDetails> list, Context context, Activity activity) {
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NHSQuizDetails pair = mList.get(position);
        holder.title.setText(pair.mTitle);
        holder.text.setText(pair.mText);
        Resources resources = mContext.getResources();
        final int resourceId = resources.getIdentifier(pair.mImage, "drawable",
                mContext.getPackageName());
        holder.imageView.setImageResource(resourceId);

        holder.url = pair.mHTMLCode;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
