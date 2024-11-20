package com.example.pagoda;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class WeatherAPI extends AppCompatActivity{
    private static final String API_KEY = "dd5a7135fc2fd77bbb726c57f87431e3";
    private static final String URL = "https://api.openweathermap.org/data/2.5/forecast?";

    static LocalDate today = LocalDate.now();
    static ZonedDateTime startOfDayUTC = today.atStartOfDay(ZoneOffset.UTC);
    static long unixTimestamp = startOfDayUTC.toEpochSecond();
    private static final String DT = String.valueOf(unixTimestamp);
    private OkHttpClient client;

    public WeatherAPI() {
        client = new OkHttpClient();
    }

    public JSONObject get7DayForecast(String cityName, Double latitude, Double longitude) {
        JSONObject Weather = new JSONObject();
        if (cityName != null) {
            String url = URL + "q=" + cityName + "&appid=" + API_KEY + "&lang=ru";
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    JSONObject weatherData = new JSONObject(responseData);
                    latitude = weatherData.getJSONObject("coord").getDouble("lat");
                    longitude = weatherData.getJSONObject("coord").getDouble("lon");
                    //return new JSONObject(responseData);
                    return null;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //lat={lat}&lon={lon}&dt={time}&appid={API key}
        else if (latitude != null && longitude != null) {
                String url = URL + "lat=" + String.format("%.4f", latitude) + "&lon=" + String.format("%.4f", longitude)  +  "&appid=" + API_KEY + "&lang=ru&units=metric";

                Request request = new Request.Builder().url(url).build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Weather = new JSONObject(responseData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
        } else {
            return null;
        }
        return Weather;
    }
}
