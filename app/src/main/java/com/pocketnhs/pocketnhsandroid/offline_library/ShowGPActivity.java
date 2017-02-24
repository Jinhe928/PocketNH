package com.pocketnhs.pocketnhsandroid.offline_library;

import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pocketnhs.pocketnhsandroid.InfoActivity;
import com.pocketnhs.pocketnhsandroid.QuizzesListActivity;
import com.pocketnhs.pocketnhsandroid.R;
import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationState;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;

import java.io.File;

public class ShowGPActivity extends AppCompatActivity {

    NHSOrganisation mOrganisation;
    NHSService mService;
    boolean mShowingOrganisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gp);
        mShowingOrganisation = ApplicationState.isShowingOrganization();
        if (mShowingOrganisation){
            mOrganisation = ApplicationState.sLastOrganisation;
        }else{
            mService = ApplicationState.sLastService;
        }
        setupImageView();
        setupToolbar();
        setupTextViews();
        setupRatings();
        setupAddressTextViews();
        setupButtons();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("My GP");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ShowGPActivity.this)
                        .toBundle();
                Intent intent = new Intent(ShowGPActivity.this,InfoActivity.class);
                startActivity(intent,bundle);
            }
        });

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


    private void setupRatings() {
        RatingBar ratingBar = (RatingBar)findViewById(R.id.rating_bar);
        ratingBar.setRating(getRating());

        TextView numberOfRatings = (TextView) findViewById(R.id.number_of_ratings);
        numberOfRatings.setText(getNumberOfRatings());
    }

    private float getRating() {
        if (mShowingOrganisation){
            return mOrganisation.mRatingValue;
        } else{
            return mService.mRatingValue;
        }
    }

    private String getNumberOfRatings() {
        if (mShowingOrganisation){
            return "Number of ratings: "+mOrganisation.mNumberOfRatings;
        }else{
            return "Number of ratings: "+mService.mNumberOfRatings;
        }
    }

    private void setupTextViews() {
        TextView title = (TextView) findViewById(R.id.info_title);
        title.setText(getTitleText());
    }

    private String getTitleText() {
        if (mShowingOrganisation){
            return mOrganisation.mTitle ;
        } else{
            return mService.mTitle;
        }
    }

    private void setupAddressTextViews() {
        TextView addressLine0 = (TextView) findViewById(R.id.address_1);
        addressLine0.setText(getAddressLine(0));

        TextView addressLine1 = (TextView) findViewById(R.id.address_2);
        addressLine1.setText(getAddressLine(1));

        TextView addressLine2 = (TextView) findViewById(R.id.address_3);
        addressLine2.setText(getAddressLine(2));

    }

    private String getAddressLine(int i) {
        if (mShowingOrganisation){
            if (mOrganisation.mAddress !=null && mOrganisation.mAddress.size() > i) {
                return mOrganisation.mAddress.get(i);
            }
        }else {
            if (mService.mAddress !=null && mOrganisation.mAddress.size() > i) {
                return mService.mAddress.get(i);
            }
        }
        return "";
    }

    private void setupButtons(){

        Button mBtnCall = (Button) findViewById(R.id.btn_call);
        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telNumber = getTelNumber();
                    if (telNumber != null) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + telNumber));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (ActivityCompat.checkSelfPermission(ShowGPActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }

            }

            private String getTelNumber() {
                String telNumber = null;
                if (mShowingOrganisation) {
                    if (mOrganisation != null) {
                        telNumber = mOrganisation.mTelephone;
                    }
                } else{
                    if (mService != null) {
                         telNumber = mService.mTelephone;
                    }
                }
                return  telNumber;
            }
        });


        Button mBtnDirections = (Button) findViewById(R.id.btn_directions);
        mBtnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShowingOrganisation) {
                    if (mOrganisation != null) {
                        Intent navigateIntent = new Intent(Intent.ACTION_VIEW);
                        navigateIntent.setData(Uri.parse("google.navigation:q="
                                + mOrganisation.mLatitude + ","
                                + mOrganisation.mLongitude));
                        navigateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(navigateIntent);
                    } else {
                        Toast.makeText(ShowGPActivity.this, "Couldn't start navigation", Toast.LENGTH_LONG).show();
                    }
                } else{
                    if (mService != null) {
                        Intent navigateIntent = new Intent(Intent.ACTION_VIEW);
                        navigateIntent.setData(Uri.parse("google.navigation:q="
                                + mService.mLatitude + ","
                                + mService.mLongitude));
                        navigateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(navigateIntent);
                    } else {
                        Toast.makeText(ShowGPActivity.this, "Couldn't start navigation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void setupImageView() {
        String fileName = "";
        if ( mOrganisation != null){
            fileName = mOrganisation.mImageFilePath;
        }else if (mService != null){
            fileName = mService.mImageFilePath;
        }
        File imgFile = new File(ApplicationState.getAppContext().getFilesDir(),fileName);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            final ImageView staticMapImage = (ImageView) findViewById(R.id.static_map);
            staticMapImage.setImageBitmap(myBitmap);

            staticMapImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog settingsDialog = new Dialog(ShowGPActivity.this);

                    LayoutInflater inflater = getLayoutInflater();
                    View newView = (View) inflater.inflate(R.layout.image_layout, null);

                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    settingsDialog.setContentView(newView);

                    ImageView iv = (ImageView) newView.findViewById(R.id.popup_image_view);
                    Bitmap bm = ((BitmapDrawable) staticMapImage.getDrawable()).getBitmap();
                    iv.setImageBitmap(bm);

                    settingsDialog.show();

                }
            });

        }

    }
}
