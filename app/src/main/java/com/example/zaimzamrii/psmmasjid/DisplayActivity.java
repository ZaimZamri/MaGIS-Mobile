package com.example.zaimzamrii.psmmasjid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DisplayActivity extends AppCompatActivity {

    String getPostKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

         getPostKey = getIntent().getExtras().get("uid").toString();
        Toast.makeText(DisplayActivity.this, getPostKey, Toast.LENGTH_SHORT).show();
    }
}
