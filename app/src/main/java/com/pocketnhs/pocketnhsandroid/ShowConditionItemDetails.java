package com.pocketnhs.pocketnhsandroid;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationUtils;
import com.pocketnhs.pocketnhsandroid.offline_library.LibraryDataManager;
import com.pocketnhs.pocketnhsandroid.server.DataReadyListener;
import com.pocketnhs.pocketnhsandroid.server.ServerDataManager;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetConditionDetailsProtocol;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSCondition;

import org.w3c.dom.Text;

public class ShowConditionItemDetails extends AppCompatActivity implements DataReadyListener {

    TextView mTextViewText;
    TextView mTextViewSummary;
    TextView mTextViewTitle;

    Toolbar toolbar;

    NHSCondition mCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_condition_item_details);

        String url = ApplicationState.sLastConditionURL;
        if (url != null) {
            ServerDataManager.getInstance().getConditionDetails(url, this);
        }
        setupToolbar();
        setupTextViews();
    }

    private void setupTextViews() {


        mTextViewTitle = (TextView) findViewById(R.id.tvTitle);
        mTextViewText = (TextView) findViewById(R.id.tvDetails);
        mTextViewSummary = (TextView) findViewById(R.id.tvSummary);
    }

    private void setupToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("Health A-Z");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ShowConditionItemDetails.this)
                        .toBundle();
                Intent intent = new Intent(ShowConditionItemDetails.this,InfoActivity.class);
                startActivity(intent,bundle);
            }
        });

        //toolbar.setSubtitle("");
        //toolbar.setTitle("");
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else{
                    finish();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.health_a_z_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean isFaved = false;
        if (mCondition != null) {
             isFaved = LibraryDataManager.getInstance().isSavedToFavorites(mCondition);
        }
        MenuItem menuItemFav = menu.findItem(R.id.favorite);
        if (isFaved) {
            menuItemFav.setVisible(false);
        }else{
            menuItemFav.setVisible(true);
        }

        MenuItem menuItemUnFav = menu.findItem(R.id.unfavorite);
        if (isFaved) {
            menuItemUnFav.setVisible(true);
        } else{
            menuItemUnFav.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favorite:
                favoriteCondition();
                return true;
            case R.id.unfavorite:
                unfavoriteCondition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void favoriteCondition() {
        if (mCondition != null) {
            boolean success = LibraryDataManager.getInstance().toggleConditionSavedToFavorites(mCondition, true);
            if (success){
                invalidateOptionsMenu();
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Couldn't save",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void unfavoriteCondition() {
        if (mCondition != null) {
            boolean success = LibraryDataManager.getInstance().toggleConditionSavedToFavorites(mCondition,true);
            if (success){
                invalidateOptionsMenu();
                Toast.makeText(this,"Removed",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Couldn't remove from library",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void DataReady(String message, String requestCode, int dataIndex) {
        if (requestCode.equals(GetConditionDetailsProtocol.getInstance().getProtocolRequestCode())) {
            handleGetConditionDetailsMessage(message);
        }
    }

    private void handleGetConditionDetailsMessage(String message) {
        if (message.equals(DataReadyListener.MESSAGE_OK)) {
            mCondition = ServerDataManager.getInstance().mConditionDetails;

            if (mCondition != null) {
                mCondition.mText = ApplicationUtils.cleanText(mCondition.mText);
                mTextViewText.setText(Html.fromHtml(mCondition.mText));
               mTextViewText.setMovementMethod(CustomLinkMovementMethod.getInstance(getApplicationContext(), this));
                mTextViewTitle.setText(mCondition.mTitle);
                mTextViewSummary.setText(mCondition.mSummary);
            }
        } else {
            Toast.makeText(this, "Couldn't fetch details", Toast.LENGTH_LONG).show();
        }

    }
}
