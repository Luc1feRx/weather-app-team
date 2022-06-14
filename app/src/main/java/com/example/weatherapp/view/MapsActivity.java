package com.example.weatherapp.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ConnectivityManager manager;
    private NetworkInfo networkInfo;
    private Geocoder geocoder;
    private double selectLat, selectLng;
    private List<Address> addresses;
    private String selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                CheckConnection();
                if(networkInfo.isConnected() && networkInfo.isAvailable()){
                    selectLat = latLng.latitude;
                    selectLng = latLng.longitude;

                    GetAddress(selectLat, selectLng);
                }else{
                    Toast.makeText(MapsActivity.this, "Please Check Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CheckConnection(){
        manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
    }

    private void GetAddress(double mLat, double mLng){
        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        if(mLat != 0){
            try {
                addresses = geocoder.getFromLocation(mLat, mLng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null){
                String mAddress = addresses.get(0).getAddressLine(0);
                Log.d("TAG", mAddress);
                String city = addresses.get(0).getLocality();

                selectedAddress = mAddress;

                if(mAddress != null){
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(mLat, mLng);
                    markerOptions.position(latLng).title(selectedAddress);
                    mMap.addMarker(markerOptions).showInfoWindow();
                }else{
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }else{
//                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }else{
//            Toast.makeText(this, "Latlng is null", Toast.LENGTH_SHORT).show();
        }
    }
}