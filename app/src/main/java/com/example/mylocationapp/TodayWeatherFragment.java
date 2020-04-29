package com.example.mylocationapp;

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
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TodayWeatherFragment extends Fragment {
    ImageView img_weather;
    TextView txt_city_name,txt_humidity,txt_pressure,txt_sunrise,txt_sunset,txt_temperature,txt_description,txt_date_time,txt_wind,txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static TodayWeatherFragment instance=null;
    public static TodayWeatherFragment getInstance(){
        if(instance==null)
            instance = new TodayWeatherFragment();
        return instance;
    }

    public TodayWeatherFragment(){
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.today_weather_fragment,container,false);

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


        getWeatherInformation();
        return rootView;

    }
    private void getWeatherInformation(){
      //  Retrofit retrofit = RetrofitClient.getInstance();
        //mService = retrofit.create(IOpenWeatherMap.class);
//        Call<WeatherResult> call = mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
//                String.valueOf(Common.current_location.getLongitude()),
//                Common.APP_ID,
//                "metric");
//        call.enqueue(new Callback<WeatherResult>() {
//            @Override
//            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
//                if(response.code() == 200){
//                    WeatherResult weatherResult = response.body();
//                    assert weatherResult != null;
//
//                    Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
//                        .append(weatherResult.getWeather().get(0).getIcon())
//                        .append(".png").toString()).into(img_weather);
//
//                        txt_city_name.setText(weatherResult.getName());
//                        txt_description.setText(new StringBuilder("Weather in: ")
//                        .append(weatherResult.getName()).toString());
//                        txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
//                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
//                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
//                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" % ").toString());
//                        txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
//                        txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
//                        txt_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());
//
//                        //display panel
//                        weather_panel.setVisibility(View.VISIBLE);
//                        loading.setVisibility(View.GONE);
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<WeatherResult> call, Throwable t) {
//                Toast.makeText(getActivity(),""+t.getMessage(),Toast.LENGTH_SHORT).show();
//
//            }
//        });


        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")

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

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}