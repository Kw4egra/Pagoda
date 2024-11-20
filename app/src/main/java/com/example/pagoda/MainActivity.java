package com.example.pagoda;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private WeatherAPI weatherAPI = new WeatherAPI();
    private ViewLocation location = new ViewLocation();
    private SwitchTheme switchTheme = new SwitchTheme();
    private TextView WeatherView;
    private AutoCompleteTextView CurrentCity;
    private Button GetCurrentPosition;
    private Button CInFandFInC;
    private Button SwitchTheme;

    public String TakeData(int timestamp) {
        Date date = new Date((TimeUnit.SECONDS.toMillis(timestamp*1000)));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date).split(" ")[1];
        return formattedDate;
    }
    private void printWeather(JSONObject weatherData) throws JSONException {
        WeatherView.setText(null);
        String dt = "";
        CurrentCity.setText(weatherData.getJSONObject("city").getString("name"));
        String sunrise = TakeData(weatherData.getJSONObject("city").getInt("sunrise"));
        String sunset = TakeData(weatherData.getJSONObject("city").getInt("sunset"));

        JSONArray dailyForecast = weatherData.getJSONArray("list");
        for (int i = 0; i <= dailyForecast.length()-1; i++) {
            String currentDT = (dailyForecast.getJSONObject(i).getString("dt_txt").split(" "))[0];
            if (!dt.equals(currentDT)) {
                dt = currentDT;
                JSONObject dayForecast = dailyForecast.getJSONObject(i);
                Double temp = dayForecast.getJSONObject("main").getDouble("temp");
                Double tempFeels = dayForecast.getJSONObject("main").getDouble("feels_like");

                WeatherView.append("Дата: " + dt.split(" ")[0] +
                        "\nТемпература: " +String.valueOf(temp) + " °C" + ", ощущается как " + tempFeels + " °C\n" +
                        "Восход: " + sunrise + "\n" +
                        "Закат: " + sunset + "\n");
            }
        }
    }
    public void CInFandFInConClick(View view) {
        String[] WeatherInfo = WeatherView.getText().toString().split(" ");
        for (int i = 0; i < WeatherInfo.length; i++) {
            if (WeatherInfo[i] == "°C") {
                WeatherInfo[i] = "°F";
                WeatherInfo[i-1] = String.valueOf(Double.valueOf(WeatherInfo[i-1]) * 9/5 + 32);
            } else if (WeatherInfo[i] == "°F") {
                WeatherInfo[i] = "°F";
                WeatherInfo[i-1] = String.valueOf((Double.valueOf(WeatherInfo[i-1])-32) * 5/9);
            }
        }
    }

    public void onBtnGetCurrentPositionClick(View view) throws JSONException{
        final JSONObject[] weatherData = new JSONObject[1];
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();
        Thread thread = new Thread(() -> {
            try {
               weatherData[0] = weatherAPI.get7DayForecast(null, lat, lon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (weatherData[0] != null) {
            printWeather(weatherData[0]);
        }
    }
    public void SwitchThemeOnClick(View view)
    {
        switchTheme.switchTheme();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location.getLocationPermission(this);
        location.getLastLocation(this);
        CurrentCity = findViewById(R.id.CurrentCity);
        String[] cities = {"Moscow", "Paris", "London", "Berlin"};
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cities);
        CurrentCity.setAdapter(adapter);
        GetCurrentPosition = findViewById(R.id.GetCurrentPosition);
        SwitchTheme = findViewById(R.id.SwitchTheme);
        WeatherView = findViewById(R.id.WeatherView);
        CInFandFInC = findViewById(R.id.CInFandFInC);
    }
}
