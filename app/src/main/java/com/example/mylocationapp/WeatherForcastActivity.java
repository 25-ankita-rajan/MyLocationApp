package com.example.mylocationapp;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.mylocationapp.Adapter.ViewPagerAdapter;
import com.example.mylocationapp.Common.Common;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class WeatherForcastActivity extends AppCompatActivity{// implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    ViewPager viewPager;
    TabLayout tabs;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;

    private static final int PERMISSIONS_REQUEST_LOCATION_CODE =1 ;
    private static final String TAG = "tag" ;
    public static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forcast);
        Toast.makeText(WeatherForcastActivity.this, "toast from weatherForcast !", Toast.LENGTH_LONG).show();
        //SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(TodayWeatherFragment.getInstance(),"Today");
        viewPagerAdapter.addFragment(ForecastFragment.getInstance(),"5 Days");
        viewPagerAdapter.addFragment(CityFragment.getInstance(),"City");


        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//               fusedLocationProviderClient = new FusedLocationProviderClient(this);

               Bundle extras = getIntent().getExtras();
               if(extras!= null)
               {
                   mLocation = extras.getParcelable("location");
               }
        Common.current_location = mLocation;


    }

//    private void showCurrentLocation() {
////        buildLocationCallback();
////        buildLocationRequest();
////        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
////        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//            //  permission has not been granted.
//            Log.d("tag", "showCurrentLocation() if block......Location permission has NOT been granted. Requesting permission.");
//            requestLocationPermission();
//            Log.d("tag", "showCurrentLocation() after requestPermission() call");
//            showCurrentLocationPreview();
//
//        } else {
//
//            // Camera permissions is already available, show the camera preview.
//            Log.i("tag", "showCurrentLocation() else block......Location permission has already been granted. Displaying location.");
//            showCurrentLocationPreview();
//        }
//    }
//    private void requestLocationPermission() {
//        Log.i("tag", "requestLocationPermission().....Location permission has NOT been granted. Requesting permission.");
//
//        // BEGIN_INCLUDE(location_permission_request)
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) ||
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            // Provide an additional rationale to the user if the permission was not granted
//            // and the user would benefit from additional context for the use of the permission.
//            // For example if the user has previously denied the permission.
//            Log.d("tag", "requestLocationPermission() if block...Displaying camera permission rationale to provide additional context.");
//            new AlertDialog.Builder(this)
//                    .setTitle("Location Permission Needed")
//                    .setMessage("This app needs the Location permission, please accept to use location functionality")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //Prompt the user once explanation has been shown
//                            ActivityCompat.requestPermissions(getParent(),
//                                    PERMISSIONS_LOCATION,
//                                    PERMISSIONS_REQUEST_LOCATION_CODE);
//
//                        }
//                    }).create().show();
//        } else {
//            Log.d("tag", "requestLocationPermission() else block...Displaying camera permission rationale to provide additional context.");
//
//            // Camera permission has not been granted yet. Request it directly.
//            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION,
//                    PERMISSIONS_REQUEST_LOCATION_CODE);
//        }
//        // END_INCLUDE(camera_permission_request)
//
//    }
//    PendingResult<LocationSettingsResult> result;
//    private void showCurrentLocationPreview() {
//       // mMap.clear();
//        Log.d("tag", "showCurrentLocationPreview() ");
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
////        mLocationRequest = LocationRequest.create();
////        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////        mLocationRequest.setInterval(30 * 1000);
////        mLocationRequest.setFastestInterval(5 * 1000);
//
//        buildLocationRequest();
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                //Location Permission already granted
//                Log.d("tag", "showCurrentLocationPreview() if block");
//                fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
//                Log.d("tag", "requestLocationUpdates() after");
//
//                Log.d("tag", "showCurrentLocationPreview() if block.....setMyLocationEnabled() before");
//                //mMap.setMyLocationEnabled(true);
//                Log.d("tag", "showCurrentLocationPreview() if block.....setMyLocationEnabled() after");
//
//            } else {
//                //Request Location Permission
//                Log.d("tag", "showCurrentLocationPreview() else block");
//                requestLocationPermission();
//            }
//        }
//        else {
//            fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
//            //mMap.setMyLocationEnabled(true);
//        }
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                //final LocationSettingsStates state = result.getLocationSettingsStates();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        Log.d("tag","setResultCallback()....case success");
//                        // All location settings are satisfied. The client can initialize location
//                        // requests here.
//                        //...
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.d("tag","setResultCallback()....case RESOLUTION_REQUIRED");
//
//                        // Location settings are not satisfied. But could be fixed by showing the user
//                        // a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            Log.d("tag","setResultCallback()....case RESOLUTION_REQUIRED..... try block");
//
//                            status.startResolutionForResult(WeatherForcastActivity.this, PERMISSIONS_REQUEST_LOCATION_CODE);
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.d("tag","setResultCallback()....case SETTINGS_CHANGE_UNAVAILABLE");
//
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        //...
//                        break;
//                }
//            }
//        });
//    }
//    Location mLastLocation;
//
//    LocationCallback mLocationCallback = new LocationCallback() {
//
//
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            List<Location> locationList = locationResult.getLocations();
//            if (locationList.size() > 0) {
//                //The last location in the list is the newest
//                Location location = locationList.get(locationList.size() - 1);
//                Log.i("tag", "onLocationResult()....Location: " + location.getLatitude() + " " + location.getLongitude());
//                mLastLocation = location;
//                Common.current_location = mLastLocation;
////                if (mCurrLocationMarker != null) {
////                    mCurrLocationMarker.remove();
////                }
////
////                //Place current location marker
////                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
////                MarkerOptions markerOptions = new MarkerOptions();
////                markerOptions.position(latLng);
////                markerOptions.title("Current Position");
////                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
////                mCurrLocationMarker = mMap.addMarker(markerOptions);
////
////                //move map camera
////                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,  15));
////                mMap.getUiSettings().setZoomControlsEnabled(true);  // +/-
////                mMap.setMyLocationEnabled(true);
//            }
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("onActivityResult()", Integer.toString(resultCode));
//        Log.d("tag", "onActivityResult() ");
//
//        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
//        switch (requestCode)
//        {
//            case PERMISSIONS_REQUEST_LOCATION_CODE:
//                switch (resultCode)
//                {
//                    case Activity.RESULT_OK:
//                    {
//                        // All required changes were successfully made
//                        Toast.makeText(WeatherForcastActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
//                        break;
//                    }
//                    case Activity.RESULT_CANCELED:
//                    {
//                        // The user was asked to change settings, but chose not to
//                        Toast.makeText(WeatherForcastActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
//                        break;
//                    }
//                    default:
//                    {
//                        break;
//                    }
//                }
//                break;
//        }
//    }
//
//
//    private void buildLocationRequest() {
//        locationRequest = new LocationRequest();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setSmallestDisplacement(10.0f);
//
//    }
//
////    private void buildLocationCallback() {
////        locationCallback = new LocationCallback(){
////            @Override
////            public void onLocationResult(LocationResult locationResult) {
////                super.onLocationResult(locationResult);
////                List<Location> locationList = locationResult.getLocations();
////                if (locationList.size() > 0) {
////                    //The last location in the list is the newest
////                    Location location = locationList.get(locationList.size() - 1);
////                    Log.i("tag", "WeatherForecast...onLocationResult()....Location: " + location.getLatitude() + " " + location.getLongitude());
////                    Common.current_location = location;
////                }
////                //viewPager = (ViewPager) findViewById(R.id.view_pager);
////                //setupViewPager(viewPager);
//////                tabLayout = (TabLayout)findViewById(R.id.tabs);
//////                tabLayout.setupWithViewPager(viewPager);
////                Log.d("location","location:"+locationResult.getLastLocation().getLatitude()+" , "+locationResult.getLastLocation().getLongitude());
////
////            }
////        };
////    }
//    @Override
//    protected void onStart() {
//        mGoogleApiClient.connect();
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        if (fusedLocationProviderClient != null) {
////            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
////        }
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        showCurrentLocation();
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
}
