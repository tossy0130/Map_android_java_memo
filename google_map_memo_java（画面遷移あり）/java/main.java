package com.example.map_test_003;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText addressInput;
    private Button showMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 画面遷移 あり
         */

        addressInput = findViewById(R.id.address_input);
        showMapButton = findViewById(R.id.show_map_button);

        /**
         * ボタン クリック
         */
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString();
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("address", address);
                startActivity(intent);
            }
        });

    }
}