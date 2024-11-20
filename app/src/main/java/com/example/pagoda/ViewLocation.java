package com.example.pagoda;

import android.content.pm.PackageManager;
import android.Manifest;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class ViewLocation {
    private FusedLocationProviderClient fusedLocationClient;
    public double getLatitude() {
        return latitude;
    }
    private double latitude;
    public double getLongitude() {
        return longitude;
    }
    private double longitude;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted = false;
    public void getLocationPermission(MainActivity ma) {
        if (ContextCompat.checkSelfPermission(ma.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(ma,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void getLastLocation(MainActivity ma) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ma);
        if (ContextCompat.checkSelfPermission(ma, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(ma, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    });
        }
        else {

        }
    }
}
