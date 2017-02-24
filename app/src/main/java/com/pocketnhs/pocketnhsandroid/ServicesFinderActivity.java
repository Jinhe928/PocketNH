package com.pocketnhs.pocketnhsandroid;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pocketnhs.pocketnhsandroid.globals.Analytics;
import com.pocketnhs.pocketnhsandroid.globals.ApplicationConsts;
import com.pocketnhs.pocketnhsandroid.offline_library.LibraryDataManager;
import com.pocketnhs.pocketnhsandroid.server.DataReadyListener;
import com.pocketnhs.pocketnhsandroid.server.GeocoderService;
import com.pocketnhs.pocketnhsandroid.server.ServerDataManager;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetOrganizationDetailsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetOrganizationsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetServiceDetailsProtocol;
import com.pocketnhs.pocketnhsandroid.server.api_protocols.GetServicesProtocol;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSOrganisation;
import com.pocketnhs.pocketnhsandroid.server.transfer_objects.NHSService;
import com.pocketnhs.pocketnhsandroid.ui.SaveFinishedListener;

import java.io.IOException;
import java.util.List;

public class ServicesFinderActivity extends AppCompatActivity implements DataReadyListener, SaveFinishedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private boolean bMapReady;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private Button mBtnGP;
    private Button mBtnA_and_E;
    private Button mBtnDentists;
    private Button mBtnGetLocation;
    private Button mBtnCall;
    private Button mBtnDirections;
    private ImageButton mBtnAddFavorite;
    private LatLngBounds mServiceBoundsAandE;

    private String mCurrentOrganizationType;

    private AddressResultReceiver mResultsReceiver;
    private int mServicesDetailsSpawned;

    private NHSOrganisation mSelectedOrganisation;
    private NHSService mSelectedService;

    private LinearLayout mInfoBar;
    private TextView mInfoTitle;
    private TextView mInfoAdrressLine1;
    private TextView mInfoAdrressLine2;
    private TextView mInfoAdrressLine3;
    private TextView mInfoAdrressLine4;
    private TextView mInfoAdrressLinePostCode;
    private EditText mSearchEditText;
    private Marker myGPMarker;

    private String mLastPostcode;
    ProgressDialog mProgressDialog;
    private RatingBar mInfoRatingBar;
    private TextView mInfoNumberOfRatings;
    private View mSaveToFavLayout;
    private View mBottomButtons;
    private TextView mInfoMessage;

    @Override
    public void saveFinished(String message, final NHSOrganisation organisation) {
        if (message.equals(SaveFinishedListener.MESSAGE_OK)) {
            organisation.mMarker.setIcon(getMarkerDescriptor(true));
            if (myGPMarker != null) {
                myGPMarker.setIcon(getMarkerDescriptor(false));
            }
            ValueAnimator ani = ValueAnimator.ofFloat(0.1f, 1); //change for (0,1) if you want a fade in
            ani.setDuration(1000);
            ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    organisation.mMarker.setAlpha((float) animation.getAnimatedValue());

                    if ( (Float)animation.getAnimatedValue() == 1.0f)
                    {
                        myGPMarker = organisation.mMarker;
                        mBtnAddFavorite.setImageResource(R.drawable.saved_heart);
                    }
                }
            });
            ani.start();


        } else {
            Toast.makeText(this, "Couldn't save to My GP", Toast.LENGTH_SHORT).show();
        }

    }

    private BitmapDescriptor getMarkerDescriptor(boolean isMyGP) {
        float colorCode = BitmapDescriptorFactory.HUE_RED;
        if (isMyGP) {
            colorCode = BitmapDescriptorFactory.HUE_GREEN;
        }
        return BitmapDescriptorFactory.defaultMarker(colorCode);
    }


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(GeocoderService.Constants.RESULT_DATA_KEY);


            // Show a toast message if an address was found.
            if (resultCode == GeocoderService.Constants.SUCCESS_RESULT && mAddressOutput!=null && (!mAddressOutput.equalsIgnoreCase("null"))) {

                String postcode = mAddressOutput.replace(" ", "%20");
                mLastPostcode = postcode;
                mProgressDialog = showProgressDialog();
                if (isOrganisationType(mCurrentOrganizationType)) {
                    ServerDataManager.getInstance().getOrganizationsByPostcode(
                            mCurrentOrganizationType, postcode, ApplicationConsts.DEFAULT_RANGE, ServicesFinderActivity.this);
                } else {
                    ServerDataManager.getInstance().getAandEServicesByPostcode(
                            mCurrentOrganizationType, postcode, ApplicationConsts.DEFAULT_RANGE, ServicesFinderActivity.this);
                }
            } else {
                Toast.makeText(ServicesFinderActivity.this, "Couldn't get location ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean isOrganisationType(String mCurrentOrganizationType) {
        if (mCurrentOrganizationType == GetOrganizationsProtocol.ORGANISATION_TYPE_DENTISTS
                || mCurrentOrganizationType == GetOrganizationsProtocol.ORGANISATION_TYPE_GP_PRACTICE) {
            return true;
        } else {
            return false;
        }


    }

    @Override
    public void DataReady(String message, String requestCode, int dataIndex) {
        if (requestCode.equals(GetOrganizationsProtocol.getInstance().getProtocolRequestCode())) {
            handleGetOrganisations(message);
        } else if (requestCode.equals(GetServicesProtocol.getInstance().getProtocolRequestCode())) {
            handleGetServices(message);
        } else if (requestCode.equals(GetServiceDetailsProtocol.getInstance().getProtocolRequestCode())) {
            handleGetServiceDetail(message, dataIndex);
        } else if (requestCode.equals(GetOrganizationDetailsProtocol.getInstance().getProtocolRequestCode())) {
            if (ServerDataManager.getInstance().mDeliverer == null) {
                handleGetOrganisationDetail(message, dataIndex);
            } else {
                handleGetDelivererDetails(message, dataIndex);
            }
        }

    }

    private void handleGetDelivererDetails(String message, int dataIndex) {
        if (ServerDataManager.getInstance().mDeliverer != null) {
            mSelectedService.mRatingValue = ServerDataManager.getInstance().mDeliverer.mRatingValue;
            mSelectedService.mNumberOfRatings = ServerDataManager.getInstance().mDeliverer.mNumberOfRatings;
            if (mSelectedService != null) {
                mInfoRatingBar.setRating(mSelectedService.mRatingValue);
                String ratingsText = mSelectedService.mNumberOfRatings + " ratings";
                if (mSelectedService.mNumberOfRatings == 1) {
                    ratingsText = mSelectedService.mNumberOfRatings + " rating";
                }
                mInfoNumberOfRatings.setText(ratingsText);
                mInfoRatingBar.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_finder);
        setupToolbar();
        setupGoogleApiClient();
        setupGoogleMap();
        setupButtons();
        setupSearchEditText();
        setupInfoBar();
        setupInitialState();
    }

    private void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.title_view);
        tvTitle.setText("Services Finder");
        ImageView ivInfo = (ImageView) findViewById(R.id.info_toolbar_icon);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Analytics.LogInfoStartEvent();
                Intent intent = new Intent(ServicesFinderActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        //toolbar.setSubtitle("");
        //toolbar.setTitle("");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });


    }


    private void setupSearchEditText() {

        mSearchEditText = (EditText) findViewById(R.id.search_postcode);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    mLastPostcode = textView.getText().toString();
                    mLastPostcode = mLastPostcode.trim();
                    mLastPostcode = mLastPostcode.replace(" ", "%20");
                    if (mProgressDialog == null || mProgressDialog.isShowing() == false) {
                        mProgressDialog = showProgressDialog();
                        if (isOrganisationType(mCurrentOrganizationType)) {
                            ServerDataManager.getInstance().getOrganizationsByPostcode(
                                    mCurrentOrganizationType, mLastPostcode, ApplicationConsts.DEFAULT_RANGE,
                                    ServicesFinderActivity.this);
                        } else {
                            ServerDataManager.getInstance().getAandEServicesByPostcode(
                                    mCurrentOrganizationType, mLastPostcode, ApplicationConsts.DEFAULT_RANGE,
                                    ServicesFinderActivity.this);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private ProgressDialog showProgressDialog() {
        return ProgressDialog.show(ServicesFinderActivity.this, "NHS", "fetching data...", true);
    }

    private void setupInfoBar() {
        mInfoMessage = (TextView) findViewById(R.id.info_message);
        mInfoBar = (LinearLayout) findViewById(R.id.info_bar);
        mInfoTitle = (TextView) findViewById(R.id.info_title);
        mInfoRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mInfoNumberOfRatings = (TextView) findViewById(R.id.number_of_ratings);
        mInfoAdrressLine1 = (TextView) findViewById(R.id.address_1);
        mInfoAdrressLine2 = (TextView) findViewById(R.id.address_2);
        mInfoAdrressLine3 = (TextView) findViewById(R.id.address_3);
        mInfoAdrressLine4 = (TextView) findViewById(R.id.address_4);
        mInfoAdrressLinePostCode = (TextView) findViewById(R.id.address_postcode);
        mSaveToFavLayout = findViewById(R.id.info_bar_fav_button);
    }

    private void handleGetOrganisationDetail(String message, int dataIndex) {

        if (mSelectedOrganisation != null) {
            mInfoRatingBar.setRating(mSelectedOrganisation.mRatingValue);
            String ratingsText = mSelectedOrganisation.mNumberOfRatings + " ratings";
            if (mSelectedOrganisation.mNumberOfRatings == 1) {
                ratingsText = mSelectedOrganisation.mNumberOfRatings + " rating";
            }
            mInfoNumberOfRatings.setText(ratingsText);
            mInfoRatingBar.setVisibility(View.VISIBLE);
        }
    }


    private void handleGetServiceDetail(String message, int dataIndex) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        // Log.d("ServiceFinder", "spawned:" + mServicesDetailsSpawned);
        mServicesDetailsSpawned--;
        if (message.equals(DataReadyListener.MESSAGE_OK)) {

            NHSService service = ServerDataManager.getInstance().mServices.get(dataIndex);
            BitmapDescriptor descriptor = getMarkerDescriptor(false);
            /*if (LibraryDataManager.getInstance().isSavedToFavorites(service)) {
                colorCode = BitmapDescriptorFactory.HUE_GREEN;
                descriptor = BitmapDescriptorFactory.defaultMarker(colorCode);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(service.mLatitude),
                                Double.parseDouble(service.mLongitude)))
                        .icon(descriptor)
                        .zIndex(2.0f)
                        .title(service.mName));
                marker.setTag(dataIndex);
                service.mMarker = marker;

            } else {
            */

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(service.mLatitude),
                            Double.parseDouble(service.mLongitude)))
                    .icon(descriptor)
                    .title(service.mName));
            marker.setTag(dataIndex);
            service.mMarker = marker;
            if (mServiceBoundsAandE == null) {
                mServiceBoundsAandE = getNullBounds(service);
            } else {
                mServiceBoundsAandE = mServiceBoundsAandE.including(marker.getPosition());
            }
        } else

        {
            Toast.makeText(ServicesFinderActivity.this, "Couldn't get service details", Toast.LENGTH_SHORT).show();
        }
        // Log.d("serviceFinder", "remaining:" + mServicesDetailsSpawned);
        if (mServicesDetailsSpawned == 0) {
            //Log.d("serviceFinder", "centering");
            centerMapOnBounds(mServiceBoundsAandE);
        }

    }

    private void handleGetServices(String message) {

        if (message.equals(DataReadyListener.MESSAGE_OK)) {
            mMap.clear();
            mServiceBoundsAandE = null;
            List<NHSService> list = getServerServicesList(mCurrentOrganizationType);
            if (list != null) {
                mServicesDetailsSpawned = list.size();
                for (int i = 0; i < list.size(); i++) {
                    NHSService service = list.get(i);
                    ServerDataManager.getInstance().getServiceDetails(i, this);
                }
            }
            mMap.setOnMarkerClickListener(this);
            /*if (mLastPostcode != null && mLastPostcode.length() > 0) {
                centerMapOnPostcode(mLastPostcode);
            }*/
            closeSoftKeyBoard();

        } else {
            mProgressDialog.dismiss();
            Toast.makeText(this, "Couldn't get location ", Toast.LENGTH_SHORT).show();
        }
    }

    /*private class extremeCoordinates{
        public Double mMaxLongitude = - 90.0;
        public Double mMinLongitude =  90.0;
        public Double mMaxLatitude = - 180.0;
        public Double mMinLatitude = 180.0;

        public void update(String lat, String lng){
            try {
                Double latitude = Double.parseDouble(lat);
                Double longitude = Double.parseDouble(lat);
                if (mMaxLatitude < latitude){
                    mMaxLatitude = latitude;
                }
                if (mMinLatitude > latitude){
                    mMinLatitude = latitude;
                }
                if (mMaxLongitude < longitude){
                    mMaxLongitude = longitude;
                }
                if (mMinLongitude  > longitude){
                    mMinLongitude = longitude;
                }

            }catch (NumberFormatException ex){
                Log.e("Service Finder", "couldn't update location, number format exception: "
                + lat + " : " + lng);
            }

        }
    }*/
    private void handleGetOrganisations(String message) {
        LatLngBounds bounds = null;
        mProgressDialog.dismiss();
        if (message.equals(DataReadyListener.MESSAGE_OK)) {
            mMap.clear();
            List<NHSOrganisation> list = getServerOrganisationList(mCurrentOrganizationType);
            if (list != null) {

                for (int i = 0; i < list.size(); i++) {
                    NHSOrganisation organisation = list.get(i);
                    if (i == 0) {
                        bounds = getNullBounds(organisation);
                    }
                    if (LibraryDataManager.getInstance().isSavedToFavorites(organisation)) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(organisation.mLatitude),
                                        Double.parseDouble(organisation.mLongitude)))
                                .icon(getMarkerDescriptor(true))
                                .zIndex(2.0f)
                                .title(organisation.mName));
                        marker.setTag(i);
                        organisation.mMarker = marker;
                        myGPMarker = marker;
                        bounds = bounds.including(marker.getPosition());

                    } else {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(organisation.mLatitude),
                                        Double.parseDouble(organisation.mLongitude)))
                                .icon(getMarkerDescriptor(false))
                                .title(organisation.mName));
                        marker.setTag(i);
                        organisation.mMarker = marker;
                        bounds = bounds.including(marker.getPosition());
                    }

                }
            }
            mMap.setOnMarkerClickListener(this);
            /*if (mLastPostcode != null && mLastPostcode.length() > 0) {
                centerMapOnPostcode(mLastPostcode);
            }*/
            centerMapOnBounds(bounds);
            closeSoftKeyBoard();


        } else {
            Toast.makeText(this, "Couldn't get location ", Toast.LENGTH_SHORT).show();
        }
    }

    private void centerMapOnBounds(LatLngBounds bounds) {
        if (bounds != null) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLngBounds(bounds, 30);
            //CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
            mMap.moveCamera(center);
            //mMap.animateCamera(zoom);

        }
    }

    private LatLngBounds getNullBounds(NHSOrganisation organisation) {
        LatLngBounds bounds = null;
        try {
            Double latitude = Double.parseDouble(organisation.mLatitude);
            Double longitude = Double.parseDouble(organisation.mLongitude);
            LatLng latLng = new LatLng(latitude, longitude);
            bounds = LatLngBounds.builder().include(latLng).build();

        } catch (NumberFormatException ex) {
            Log.e("Service Finder", "couldn't update location, number format exception: "
                    + organisation.mLatitude + " : " + organisation.mLongitude);
            LatLng latLng = new LatLng(0.0, 0.0);
            bounds = new LatLngBounds(latLng, latLng);
        }
        return bounds;
    }

    private LatLngBounds getNullBounds(NHSService service) {
        LatLngBounds bounds = null;
        try {
            Double latitude = Double.parseDouble(service.mLatitude);
            Double longitude = Double.parseDouble(service.mLongitude);
            LatLng latLng = new LatLng(latitude, longitude);
            bounds = LatLngBounds.builder().include(latLng).build();

        } catch (NumberFormatException ex) {
            Log.e("Service Finder", "couldn't update location, number format exception: "
                    + service.mLatitude + " : " + service.mLongitude);
            LatLng latLng = new LatLng(0.0, 0.0);
            bounds = new LatLngBounds(latLng, latLng);
        }
        return bounds;
    }


    private void closeSoftKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void centerMapOnPostcode(String lastPostcode) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(lastPostcode + ", UK", 1);
            if (addresses != null && !addresses.isEmpty()) {
                moveCameraToAddress(addresses);
            } else {
                List<Address> addresses2 = geocoder.getFromLocationName(trimPostCode(lastPostcode) + ", UK", 1);
                if (addresses2 != null && !addresses2.isEmpty()) {
                    moveCameraToAddress(addresses2);
                } else {
                    // Display appropriate message when Geocoder services are not available
                    Toast.makeText(this, "Unable to geocode postcode: " + lastPostcode, Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            // handle exception
        }

    }

    private String trimPostCode(String postcode) {
        if (postcode.contains("%")) {
            return postcode.substring(0, postcode.indexOf("%"));
        } else {
            return postcode;
        }
    }

    private void moveCameraToAddress(List<Address> addresses) {
        Address address = addresses.get(0);
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(address.getLatitude(), address.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Integer index = (Integer) marker.getTag();
        if (isOrganisationType(mCurrentOrganizationType)) {
            List<NHSOrganisation> list = getServerOrganisationList(mCurrentOrganizationType);
            if (list != null && index < list.size()) {
                mSelectedOrganisation = list.get(index);
                refreshInfoBarOrganisation();
            }
        } else {
            List<NHSService> list = getServerServicesList(mCurrentOrganizationType);
            if (list != null && index < list.size()) {
                mSelectedService = list.get(index);
                refreshInfoBarService();
            }
        }
        setSaveToFavVisibility();
        mBottomButtons.setVisibility(View.VISIBLE);
        mInfoMessage.setVisibility(View.GONE);
        return false;
    }

    private void setSaveToFavVisibility() {
        if (mCurrentOrganizationType.equals(GetOrganizationsProtocol.ORGANISATION_TYPE_GP_PRACTICE)) {
            mSaveToFavLayout.setVisibility(View.VISIBLE);
        } else {
            mSaveToFavLayout.setVisibility(View.GONE);
        }
    }

    private void refreshInfoBarOrganisation() {
        if (mSelectedOrganisation != null) {
            mInfoBar.setVisibility(View.VISIBLE);

            mInfoTitle.setText(mSelectedOrganisation.mTitle);
            mInfoRatingBar.setVisibility(View.INVISIBLE);
            if (LibraryDataManager.getInstance().isSavedToFavorites(mSelectedOrganisation)) {
                mBtnAddFavorite.setImageResource(R.drawable.saved_heart);
            } else {
                mBtnAddFavorite.setImageResource(R.drawable.save_heart);
            }
            ServerDataManager.getInstance().getOrganisationDetails(mSelectedOrganisation, this);
            // address
            if (mSelectedOrganisation.mAddress != null) {
                int index = 0;
                if (index < mSelectedOrganisation.mAddress.size()) {
                    mInfoAdrressLine1.setText(mSelectedOrganisation.mAddress.get(index));
                }
                ;
                index = 1;
                if (index < mSelectedOrganisation.mAddress.size()) {
                    mInfoAdrressLine2.setText(mSelectedOrganisation.mAddress.get(index));
                }
                ;
                index = 2;
                if (index < mSelectedOrganisation.mAddress.size()) {
                    mInfoAdrressLine3.setText(mSelectedOrganisation.mAddress.get(index));
                }
                ;
                index = 3;
                if (index < mSelectedOrganisation.mAddress.size()) {
                    mInfoAdrressLine4.setText(mSelectedOrganisation.mAddress.get(index));
                }
                ;
            }

            mInfoAdrressLinePostCode.setText(mSelectedOrganisation.mPostCode);
        }
    }

    private void refreshInfoBarService() {
        if (mSelectedService != null) {

            mInfoBar.setVisibility(View.VISIBLE);
            mInfoTitle.setText(mSelectedService.mTitle);
            ServerDataManager.getInstance().getDelivererDetails(mSelectedService.mDelivererURL, this);
            // address
            if (mSelectedService.mAddress != null) {
                int index = 0;
                if (index < mSelectedService.mAddress.size()) {
                    mInfoAdrressLine1.setText(mSelectedService.mAddress.get(index));
                }
                ;
                index = 1;
                if (index < mSelectedService.mAddress.size()) {
                    mInfoAdrressLine2.setText(mSelectedService.mAddress.get(index));
                }
                ;
                index = 2;
                if (index < mSelectedService.mAddress.size()) {
                    mInfoAdrressLine3.setText(mSelectedService.mAddress.get(index));
                }
                ;
                index = 3;
                if (index < mSelectedService.mAddress.size()) {
                    mInfoAdrressLine4.setText(mSelectedService.mAddress.get(index));
                }
                ;
            }

            if (mSelectedService.mPostCode != null) {
                mInfoAdrressLinePostCode.setText(mSelectedService.mPostCode);
            }
        }
    }


    private List<NHSOrganisation> getServerOrganisationList(String currentOrganizationType) {
        if (currentOrganizationType.equals(GetOrganizationsProtocol.ORGANISATION_TYPE_GP_PRACTICE)
                || currentOrganizationType.equals(GetOrganizationsProtocol.ORGANISATION_TYPE_DENTISTS)) {
            return ServerDataManager.getInstance().mOrganizations;
        } else {
            return null;
        }
    }

    private List<NHSService> getServerServicesList(String currentOrganizationType) {
        if (currentOrganizationType.equals(GetServicesProtocol.ORGANISATION_TYPE_A_AND_E_PRACTICE)) {
            return ServerDataManager.getInstance().mServices;
        } else {
            return null;
        }
    }

    private void setupInitialState() {
        mBottomButtons.setVisibility(View.INVISIBLE);
        mSaveToFavLayout.setVisibility(View.INVISIBLE);
        mCurrentOrganizationType = GetOrganizationsProtocol.ORGANISATION_TYPE_GP_PRACTICE;
    }

    protected void startIntentService() {
        mResultsReceiver = new AddressResultReceiver(new Handler());
        Intent intent = new Intent(this, GeocoderService.class);
        intent.putExtra(GeocoderService.Constants.RECEIVER, mResultsReceiver);
        intent.putExtra(GeocoderService.Constants.LOCATION_DATA_EXTRA, mLastLocation);
        //intent.putExtra(GeocoderService.Constants.LOCATION_DATA_EXTRA, new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
        startService(intent);
    }

    private void setupButtons() {

        mBottomButtons = findViewById(R.id.bottom_buttons);

        mBtnGP = (Button) findViewById(R.id.btn_gp);
        mBtnGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentOrganizationType = GetOrganizationsProtocol.ORGANISATION_TYPE_GP_PRACTICE;
                clearMapState();
                setSelectedButtonBackgroundGP();
            }

            private void setSelectedButtonBackgroundGP() {
                mBtnGP.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
                mBtnA_and_E.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.black));
                mBtnDentists.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.black));
                mBtnGP.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.blueLineColor));
                mBtnA_and_E.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
                mBtnDentists.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
            }
        });


        mBtnA_and_E = (Button) findViewById(R.id.btn_a_and_e);
        mBtnA_and_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentOrganizationType = GetServicesProtocol.ORGANISATION_TYPE_A_AND_E_PRACTICE;
                clearMapState();
                setSelectedButtonBackgroundAandE();
            }

            private void setSelectedButtonBackgroundAandE() {
                mBtnGP.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.black));
                mBtnA_and_E.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
                mBtnDentists.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.black));
                mBtnGP.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
                mBtnA_and_E.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.blueLineColor));
                mBtnDentists.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
            }
        });


        mBtnDentists = (Button) findViewById(R.id.btn_dentists);
        mBtnDentists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentOrganizationType = GetOrganizationsProtocol.ORGANISATION_TYPE_DENTISTS;
                clearMapState();
                setSelectedButtonBackgroundDentists();
            }

            private void setSelectedButtonBackgroundDentists() {
                mBtnGP.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.black));
                mBtnA_and_E.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.black));
                mBtnDentists.setTextColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
                mBtnGP.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
                mBtnA_and_E.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.white));
                mBtnDentists.setBackgroundColor(ContextCompat.getColor(ServicesFinderActivity.this, R.color.blueLineColor));
            }
        });


        mBtnGetLocation = (Button) findViewById(R.id.btn_get_location);
        mBtnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bMapReady == true) {
                    clearPostcode();
                    if (ActivityCompat.checkSelfPermission(ServicesFinderActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(ServicesFinderActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    if (mLastLocation != null) {
                        startIntentService();
                    }
                }
            }
        });


        mBtnCall = (Button) findViewById(R.id.btn_call);
        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telNumber = getTelNumber();
                if (telNumber != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + telNumber));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(ServicesFinderActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                if (isOrganisationType(mCurrentOrganizationType)) {
                    if (mSelectedOrganisation != null) {
                        telNumber = mSelectedOrganisation.mTelephone;
                    }
                } else {
                    if (mSelectedService != null) {
                        telNumber = mSelectedService.mTelephone;
                    }
                }
                return telNumber;
            }
        });


        mBtnDirections = (Button) findViewById(R.id.btn_directions);
        mBtnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOrganisationType(mCurrentOrganizationType)) {
                    if (mSelectedOrganisation != null) {
                        Intent navigateIntent = new Intent(Intent.ACTION_VIEW);
                        navigateIntent.setData(Uri.parse("google.navigation:q="
                                + mSelectedOrganisation.mLatitude + ","
                                + mSelectedOrganisation.mLongitude));
                        navigateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(navigateIntent);
                    } else {
                        Toast.makeText(ServicesFinderActivity.this, "Couldn't start navigation", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (mSelectedService != null) {
                        Intent navigateIntent = new Intent(Intent.ACTION_VIEW);
                        navigateIntent.setData(Uri.parse("google.navigation:q="
                                + mSelectedService.mLatitude + ","
                                + mSelectedService.mLongitude));
                        navigateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(navigateIntent);
                    } else {
                        Toast.makeText(ServicesFinderActivity.this, "Couldn't start navigation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        mBtnAddFavorite = (ImageButton) findViewById(R.id.add_favorite);
        mBtnAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOrganisationType(mCurrentOrganizationType)) {
                    if (mSelectedOrganisation != null) {
                        LibraryDataManager.getInstance().toggleSavedToFavorites(mSelectedOrganisation, ServicesFinderActivity.this);
                    }
                    if (LibraryDataManager.getInstance().isSavedToFavorites(mSelectedOrganisation)) {
                        mBtnAddFavorite.setImageResource(R.drawable.saved_heart);
                        if (mSelectedOrganisation.mMarker != null) {
                            float colorCode = BitmapDescriptorFactory.HUE_GREEN;
                            BitmapDescriptor descriptor = BitmapDescriptorFactory.defaultMarker(colorCode);
                            mSelectedOrganisation.mMarker.setIcon(descriptor);
                        }

                    } else {
                        mBtnAddFavorite.setImageResource(R.drawable.save_heart);
                    }
                }

                /*else {
                    if (mSelectedService != null) {
                        LibraryDataManager.getInstance().toggleSavedToFavorites(mSelectedService);
                    }
                    if (LibraryDataManager.getInstance().isSavedToFavorites(mSelectedService)) {
                        mBtnAddFavorite.setImageResource(R.drawable.saved_heart);
                    } else {
                        mBtnAddFavorite.setImageResource(R.drawable.save_heart);
                    }

                }*/
            }
        });

    }

    private void clearMapState() {
        mMap.clear();
        mSearchEditText.setText("");
        mInfoBar.setVisibility(View.INVISIBLE);
        mInfoMessage.setVisibility(View.VISIBLE);
        mBottomButtons.setVisibility(View.INVISIBLE);
    }

    private void clearPostcode() {
        mLastPostcode = "";
    }

    //setupGoogleApiClient();

    private void setupGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    private void setupGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        bMapReady = true;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng london = new LatLng(51.5, -0.12);
        final float CITY_LEVEL_ZOOM = 10.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(london, CITY_LEVEL_ZOOM));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng newLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Couldn't get location", Toast.LENGTH_LONG).show();

    }
}
