package com.example.map_test_app_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText addressInput;
    private Button showMapButton;

    private double radius = 5000; // 半径（メートル）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *  画面遷移なし
         */


        // === 画面遷移　なし
        addressInput = findViewById(R.id.address_input);
        showMapButton = findViewById(R.id.show_map_button);


        /**
         *  画面遷移　なし　（ボタンクリック）
         */
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });


        /**
         *  Map フラグメント　セット
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

    }

    /**
     *   マップをオブジェクトに格納
     */
    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
    }

    /**
     *  マップ　表示ロジック
     */
    private void showMap(){
        String address = addressInput.getText().toString();

        System.out.println("showMap() ===> address:::" + address);

        // === 住所を、座標（経度・緯度に変換）
        LatLng location = getLocationFromAddress(address);

        if(location != null) {
            // 地図の中心を設定
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));

            // === 円を描画
            mMap.addCircle(new CircleOptions()
                    .center(location)
                    .radius(radius)
                    .strokeWidth(1)
                    .strokeColor(0xFF0000FF)
                    .fillColor(0x220000FF));
        } else {
            // 住所が無効な住所　or 空
            Toast.makeText(this, "住所が正しくありません。", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *  住所から座標を取得する
     */
    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> addressList;
        LatLng location = null;

        try {
            addressList = coder.getFromLocationName(strAddress, 1);

            if(addressList != null && !addressList.isEmpty()) {
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