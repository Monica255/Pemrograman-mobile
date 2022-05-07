package com.example.databarang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class lihat_data extends AppCompatActivity {
    TextView nama,stock,harga;
    ImageView img;
    Toolbar toolbar;
    String path ="http://192.168.1.20/databarang/img/";
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        nama=findViewById(R.id.nama);
        stock=findViewById(R.id.stock);
        harga=findViewById(R.id.harga);
        img = findViewById(R.id.img_barang);
        //ambil data intent
        nama.setText(getIntent().getStringExtra("name"));
        stock.setText(getIntent().getStringExtra("stock"));
        harga.setText(getIntent().getStringExtra("price"));
        Glide.with(getApplicationContext())
                .load(path+getIntent().getStringExtra("img"))
                .into(img);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
