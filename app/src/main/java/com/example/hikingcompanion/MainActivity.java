package com.example.hikingcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationinfo(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {

                updateLocationinfo(lastKnownLocation);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();

        }


    }

    public void startListening() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }


    }


    public void updateLocationinfo(Location location) {

        TextView latitudeTextView = findViewById(R.id.latitudeTextView);
        TextView longitudeTextView = findViewById(R.id.LongitudeTextView);
        TextView accuracyTextView = findViewById(R.id.altitudeTextView);
        TextView altitudeTextView = findViewById(R.id.altitudeTextView);
        TextView addressTextView = findViewById(R.id.addressTextaView);

        latitudeTextView.setText("Latitude: " + Double.toString(location.getLatitude()));
        longitudeTextView.setText("Longitude: " + Double.toString(location.getLongitude()));
        accuracyTextView.setText("Accuracy of GPS: " + Double.toString(location.getAccuracy()));
        altitudeTextView.setText("Altitude: " + Double.toString(location.getAltitude()));


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "could not find address!";

        try {
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0) {

                 address = "Adddress: ";

                if (listAddresses.get(0).getSubThoroughfare() != null) {

                    address += listAddresses.get(0).getThoroughfare() ;

                }
                if (listAddresses.get(0).getLocality() != null) {

                    address += listAddresses.get(0).getLocality() + "\n";

                }

                if (listAddresses.get(0).getPostalCode() != null) {

                    address += listAddresses.get(0).getPostalCode() + "\n";

                }


                if (listAddresses.get(0).getAdminArea() != null) {

                    address += listAddresses.get(0).getAdminArea();

                }

            }



        }



        catch (Exception e) {

            e.printStackTrace();
        }

        addressTextView.setText(address);


    }
}
