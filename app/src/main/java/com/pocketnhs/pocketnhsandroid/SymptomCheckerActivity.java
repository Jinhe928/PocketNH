package com.pocketnhs.pocketnhsandroid;

import android.support.v4.app.Fragment;

/**
 * Created by MacBook Pro on 23.8.2016..
 */
public class SymptomCheckerActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SymptomCheckerFragment.newInstance();
    }
}
