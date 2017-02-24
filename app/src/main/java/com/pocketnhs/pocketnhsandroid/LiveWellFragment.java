package com.pocketnhs.pocketnhsandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.server.LiveWellData;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSLiveWellData;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSLiveWellDataList;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPair;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSTextLinkPairList;
import com.pocketnhs.pocketnhsandroid.ui.AtoZLinksAdapter;
import com.pocketnhs.pocketnhsandroid.ui.LiveWellLinksAdapter;

/**
 * Created by MacBook Pro on 26.8.2016..
 */
public class LiveWellFragment extends Fragment {

    RecyclerView mRecyclerView;
    LiveWellLinksAdapter mAdapter;
    NHSLiveWellDataList mLiveWellList;


    public static LiveWellFragment newInstance() {
        return new LiveWellFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live_well,container,false);
        setupToolbar(v);
        setupRecyclerView(v);


        return v;
    }

    private void setupToolbar(View v) {

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) v.findViewById(R.id.title_view);
        tvTitle.setText("Live Well");
        ImageView ivInfo = (ImageView) v.findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                        .toBundle();
                Intent intent = new Intent(getActivity(),InfoActivity.class);
                startActivity(intent,bundle);
            }
        });

        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().finishAfterTransition();
                } else{
                    getActivity().finish();
                }
            }
        });


    }


    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.live_well_recycler_view);
        setupLiveWellList();
        mAdapter = new LiveWellLinksAdapter(mLiveWellList, getContext(), getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setupLiveWellList() {

        mLiveWellList = new NHSLiveWellDataList();

        mLiveWellList.list.add(LiveWellData.d1);
        mLiveWellList.list.add(LiveWellData.d2);
        mLiveWellList.list.add(LiveWellData.d3);
        mLiveWellList.list.add(LiveWellData.d4);
        mLiveWellList.list.add(LiveWellData.d5);
        mLiveWellList.list.add(LiveWellData.d6);
    }




}

