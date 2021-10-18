package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TouristInfoActivity extends AppCompatActivity {
    private Button mBtnBack;
    private Button mBtnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_info);
        mBtnBack = (Button) findViewById(R.id.btn_tourist_info_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TouristInfoActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        mBtnSearch = (Button) findViewById(R.id.btn_tourist_info_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TouristInfoActivity.this,TouristInfoResActivity.class);
                startActivity(intent);
            }
        });
    }
}