package com.example.map_test_003;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double radius = 10000; // 半径（メートル）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String address = getIntent().getStringExtra("address");

        // 住所を座標に変換
        LatLng location = getLocationFromAddress(address);
        if (location != null) {
            // 地図の中心を設定
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

            // 円を描画
            mMap.addCircle(new CircleOptions()
                    .center(location)
                    .radius(radius)
                    .strokeWidth(2)
                    .strokeColor(0xFF0000FF)
                    .fillColor(0x220000FF));
        } else {
            // 住所が無効な場合の処理
        }
    }

    // 住所から座標を取得
    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> addressList;
        LatLng location = null;

        try {
            addressList = coder.getFromLocationName(strAddress, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                location = new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

}