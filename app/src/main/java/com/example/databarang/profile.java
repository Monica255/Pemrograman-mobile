package com.example.databarang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class profile extends AppCompatActivity {
    ImageView map;
    int total,max=0;
    ProgressBar progressBar;
    TextView persentase,capacity;
    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigationView= findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.profilemenu);
        total= Integer.parseInt(getIntent().getStringExtra("total"));
        max = Integer.parseInt(getIntent().getStringExtra("max"));
        progressBar = findViewById(R.id.progress);
        persentase = findViewById(R.id.persentase);
        capacity = findViewById(R.id.capacity);
        int color = Color.parseColor("#6FCECE");
        value=progress_count(total,max);
        if(value>75){
            progressBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }else {
            progressBar.getProgressDrawable().setColorFilter(
                    color, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        progressBar.setProgress(value);
        persentase.setText(value+"%");
        capacity.setText(total+"/"+max);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainmenu:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profilemenu:

                        return true;

                }
                return false;
            }
        });


    }

    int progress_count(int x, int y){
        float a=(float) x/y;
        return Math.round(a*100);
    }
}