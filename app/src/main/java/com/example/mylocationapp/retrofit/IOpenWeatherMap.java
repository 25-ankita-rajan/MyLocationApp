package com.example.mylocationapp.retrofit;

import com.example.mylocationapp.Model.WeatherForecastResult;
import com.example.mylocationapp.Model.WeatherResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit) ;

    @GET("forecast")
    Observable<WeatherForecastResult> geForecasttWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit) ;
    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit) ;
}

//https://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=63b9444e23d3ad95bb3984ff24dc8948