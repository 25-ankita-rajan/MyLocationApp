package com.example.mylocationapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mylocationapp.Common.Common;
import com.example.mylocationapp.Model.WeatherResult;
import com.example.mylocationapp.retrofit.IOpenWeatherMap;
import com.example.mylocationapp.retrofit.RetrofitClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CityFragment extends Fragment {

    ImageView img_weather;
    TextView txt_city_name,txt_humidity,txt_pressure,txt_sunrise,txt_sunset,txt_temperature,txt_description,txt_date_time,txt_wind,txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;
    AutocompleteSupportFragment autocomplete_fragment;
    String cityName;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    private static final int AUTOCOMPLETE_REQUEST_CODE =1 ;
    //private final String apiKey = "AIzaSyC4tg9mjagnFLUOp9KxH3HWxHHoiXlGeK0";
    AutocompleteSupportFragment autocompleteSupportFragment;
    PlacesClient placesClient;

    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
            Place.Field.NAME, Place.Field.LAT_LNG);
    LatLng latLng;
    Location location;

    static CityFragment instance;

    public static CityFragment getInstance() {
        if (instance == null) {
            instance = new CityFragment();
        }
        return instance;
    }

    public CityFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_fragment, container, false);

        img_weather =(ImageView) rootView.findViewById(R.id.image_weather);
        txt_city_name = (TextView) rootView.findViewById(R.id.txt_city_name);
        txt_humidity = (TextView) rootView.findViewById(R.id.txt_humidity);
        txt_pressure = (TextView) rootView.findViewById(R.id.txt_pressure);
        txt_sunrise = (TextView) rootView.findViewById(R.id.txt_sunrise);
        txt_sunset = (TextView) rootView.findViewById(R.id.txt_sunset);
        txt_temperature = (TextView) rootView.findViewById(R.id.txt_temperature);
        txt_description = (TextView) rootView.findViewById(R.id.txt_description);
        txt_date_time = (TextView) rootView.findViewById(R.id.txt_date_time);
        txt_wind = (TextView) rootView.findViewById(R.id.txt_wind);
        txt_geo_coord = (TextView) rootView.findViewById(R.id.txt_geo_coord);
        weather_panel = (LinearLayout)rootView.findViewById(R.id.weather_panel);
        loading = (ProgressBar) rootView.findViewById(R.id.loading);
       // autocomplete_fragment = (AutocompleteSupportFragment) rootView.findViewById(R.id.autocomplete_fragment);
        initPlaces();
        setAutocompleteSupportFragment();

      //  getWeatherInformation();

        return rootView;

    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByCityName(cityName, Common.APP_ID, "metric")

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>(){
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {

                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/")
                                .append(weatherResult.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_weather);

                        txt_city_name.setText(weatherResult.getName());
                        txt_description.setText(new StringBuilder("Weather in: ")
                                .append(weatherResult.getName()).toString());
                        txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" % ").toString());
                        txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());

                        //display panel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);

                    }
                } ,new  Consumer<Throwable>(){
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(),""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("tag","ERROR----: "+throwable.getMessage());
                    }
                })

        ) ;
    }

    private void initPlaces() {
        if(!Places.isInitialized())
        {Places.initialize(getActivity(), getResources().getString(R.string.google_maps_key));}
        placesClient = Places.createClient(getContext());

    }

    private void setAutocompleteSupportFragment() {

        autocompleteSupportFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(placeFields);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
//                latLng = place.getLatLng();
//                location.setLatitude(latLng.latitude);
//                location.setLongitude(latLng.longitude);
//                Common.current_location = location;
                cityName = place.getName();
                getWeatherInformation();

                Log.d("Place ", " " + latLng);
            }

            @Override
            public void onError(@NonNull Status status) {

                Toast.makeText(getActivity(), "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //  @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(autocompleteSupportFragment != null && !getActivity().isFinishing()){
//            getActivity().getSupportFragmentManager().remove(autocompleteSupportFragment).commit;
//        }
//
//    }
//

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
     //AUTOCOMPLETE_REQUEST_CODE is just a unique constant, define it
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                // when resultcode is RESULT_OK
                //mAddressEditText.setText(place.getName());
                // Notice this line, update your editText up here
                Toast.makeText(getActivity(), "Error: " + place.getName(), Toast.LENGTH_LONG).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                // Handle error
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                // Handle results if canceled
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autocompleteSupportFragment != null) {
            getChildFragmentManager().beginTransaction().remove(autocompleteSupportFragment).commitAllowingStateLoss();
        }

    }
}