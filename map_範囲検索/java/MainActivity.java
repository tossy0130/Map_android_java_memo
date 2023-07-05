package com.example.map_kensaku_app_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPostalCode;
    private Button buttonSearch;
    private ListView listViewLocations;
    private LocationAdapter locationAdapter;

    private List<LocationData> locationDataList;

    private List<LocationData> items;
    private int kensaku_i;
    private RadioButton RadioButtonB2;
    private ImageButton ImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPostalCode = findViewById(R.id.editTextPostalCode);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewLocations = findViewById(R.id.listViewLocations);


        locationAdapter = new LocationAdapter(this);
        listViewLocations.setAdapter(locationAdapter);

        // === エディットテキスト　数字入力設定
        editTextPostalCode.setInputType(InputType.TYPE_CLASS_NUMBER);

        /**
         *    // CSVファイルからデータを読み込む
         */
        locationDataList = loadLocationData();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // === 住所（入力された住所を取得）
                String postalCode = editTextPostalCode.getText().toString().trim();
                System.out.println("onclick ::: postalCode ::: 値 :::" + postalCode);

                if (!TextUtils.isEmpty(postalCode)) {
                    new GeocodeAsyncTask().execute(postalCode);
                } else {
                    Toast.makeText(MainActivity.this, "郵便番号を入力してください", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**
         *  リストビュー
         */
        listViewLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // クリックした位置の情報を取得する
                LocationData clickedLocation = (LocationData) locationAdapter.getItem(position);

                Double ido = clickedLocation.getLatitude(); // 緯度
                Double keido = clickedLocation.getLongitude(); // 経度取得

                // === マップへ飛ばす
                openMapActivity(ido, keido);

               //Toast.makeText(MainActivity.this, "クリックOK" + clickedLocation.getFacilityName(), Toast.LENGTH_SHORT).show();

            }
        });



        /**
         *  ========= ラジオボタン （検索範囲を、何キロにするかをきめる　デフォルト 3km） =========
         */

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroupB);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.RadioButtonB2);

        // === 初期値　取得
        RadioButtonB2 = findViewById(R.id.RadioButtonB2);
        String tmp_radio = (String) RadioButtonB2.getText();
        kensaku_i = Str_replace_int(tmp_radio, "km");

        // ラジオグループのチェック状態が変更された時に呼び出されるコールバックリスナーを登録します
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);

                String tmp_radio = (String) radioButton.getText();
                kensaku_i = Str_replace_int(tmp_radio, "km");

            }
        });

        // 指定した ID のラジオボタンをチェックします
        radioButton2.setChecked(true);

    }

    private List<LocationData> loadLocationData() {
        List<LocationData> dataList = new ArrayList<>();

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.location_data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            Integer count = 1;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String facilityName = data[0];
                    String zyuusyo = data[1];
                    double latitude = Double.parseDouble(data[2]); // 緯度
                    double longitude = Double.parseDouble(data[3]); // 経度

                    System.out.println("loadLocationData => facilityName:::" + count + "行データ:::" + facilityName + ":::latitude:::" +
                            latitude + ":::latitude:::" + latitude);

                    // === 行数カウント
                    count++;

                    LocationData locationData = new LocationData(facilityName, latitude, longitude, zyuusyo);
                    dataList.add(locationData);
                }
            }

            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private class GeocodeAsyncTask extends AsyncTask<String, Void, LatLng> {

        @Override
        protected LatLng doInBackground(String... params) {
            String postalCode = params[0];
            double latitude = 0.0, longitude = 0.0;

            System.out.println("doInBackground ::: postalCode 値 :::" + postalCode);

            // === 郵便番号を座標へ変換
            LatLng location = getLocationFromAddress(postalCode);
            System.out.println("doInBackgroundlocation ::: 値 :::" + location);

            String location_tmp = location.toString();
            String start = "(";
            String end = ",";

            String start_02 = ",";
            String end_02 = ")";

            String location_lat = Substring(location_tmp, start, end);
            String location_lon = Substring(location_tmp, start_02, end_02);

            double location_lat_d = Double.parseDouble(location_lat);
            double location_lon_d = Double.parseDouble(location_lon);

            latitude = location_lat_d;  // 緯度
            longitude = location_lon_d; // 経度


            return new LatLng(latitude, longitude);
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            if (latLng == null) {
                Toast.makeText(MainActivity.this, "該当する場所が見つかりませんでした", Toast.LENGTH_SHORT).show();
            } else {
                List<LocationData> filteredList = new ArrayList<>();

                Location location1 = new Location("");
                location1.setLatitude(latLng.latitude);
                location1.setLongitude(latLng.longitude);

                System.out.println("onPostExecute ::: ********* location1 ********* latLng.latitude:::値:::" + latLng.latitude);
                System.out.println("onPostExecute ::: ********* location1 ********* latLng.longitude:::値:::" + latLng.longitude);

                for (LocationData data : locationDataList) {
                    Location location2 = new Location("");
                    location2.setLatitude(data.getLatitude());
                    location2.setLongitude(data.getLongitude());

                    System.out.println("onPostExecute :::location2 data.getLatitude:::値:::" + data.getLatitude());
                    System.out.println("onPostExecute :::location2 data.getLongitude:::値:::" + data.getLongitude());

                    System.out.println("onPostExecute ::: ********* location2 ********* :::値:::" + location2);

                    float distance = location1.distanceTo(location2) / 1000; // メートルをキロメートルに変換

                    System.out.println("onPostExecute distance 値:::" + distance);

                    /**
                     *   kensaku_i => デフォルト値 ３キロ
                     *   === distance <= 3    ３キロ以内
                     */
                    if (distance <= kensaku_i) {
                        filteredList.add(data);
                    }
                }

                if (filteredList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "半径3km以内の施設が見つかりませんでした", Toast.LENGTH_SHORT).show();
                } else {
                    locationAdapter.setLocationDataList(filteredList);
                    locationAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     *   住所から座標を取得
     */
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

    /**
     *    開始文字と、終了文字を指定して、その間の文字列を取得
     *
     */
    public static String Substring(String all_str, String start, String end) {

        int startIndex = all_str.indexOf(start);
        System.out.println("startIndex:::" + startIndex); // 5

        int endIndex = all_str.indexOf(end, startIndex + start.length());

        System.out.println("endIndex:::" + endIndex); // 12
        System.out.println("start.length():::" + start.length()); // 1

        return all_str.substring(startIndex + start.length(), endIndex);

    }

    private void openMapActivity(double latitude, double longitude) {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

    /**
     *  文字列を空白に置換して、integer型に変換する。
     */
    public int Str_replace_int(String str, String cut) {

        String result_str = str.replace(cut, "");

        int result_num = Integer.parseInt(result_str);
        return result_num;

    }


}