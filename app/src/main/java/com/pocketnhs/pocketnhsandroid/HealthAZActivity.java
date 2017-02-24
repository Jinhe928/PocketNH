package com.pocketnhs.pocketnhsandroid;

import android.support.v4.app.Fragment;

/**
 * Created by MacBook Pro on 24.8.2016..
 */
public class HealthAZActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return HealthAZFragment.newInstance();
    }
}
