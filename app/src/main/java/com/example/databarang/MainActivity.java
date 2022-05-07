package com.example.databarang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout input_data, tampil_data,keluar;
    TextView total;
    int max=22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input_data = findViewById(R.id.input_data);
        tampil_data = findViewById(R.id.tampil_data);
        total=findViewById(R.id.total);
        total_data();
        /*keluar=findViewById(R.id.keluar);*/
        /*getSupportActionBar().setTitle(null);*/

        BottomNavigationView bottomNavigationView= findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.mainmenu);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainmenu:

                        return true;
                    case R.id.profilemenu:
                        Intent intent = new Intent(getApplicationContext(),profile.class);
                        intent.putExtra("total",total.getText().toString());
                        intent.putExtra("max",String.valueOf(max));
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        input_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),input_data.class);
                intent.putExtra("total",total.getText().toString());
                intent.putExtra("max",String.valueOf(max));
                startActivity(intent);
            }
        });

        tampil_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),tampil_data.class));
            }
        });

        /*keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Toast.makeText(getApplicationContext(), "Terima Kasih",Toast.LENGTH_SHORT).show();
            }
        });*/

    }
    void total_data(){
        final int[] x = new int[1];
        ArrayList<data_objek>list=new ArrayList<>();
        String url = "http://192.168.1.20/databarang/tampil.php";
        StringRequest request= new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject getData= jsonArray.getJSONObject(i);
                                String id_barang = getData.getString("id");
                                String nama_barang = getData.getString("name");

                                String stok_barang = getData.getString("stock");
                                String harga_barang = getData.getString("price");
                                String img = getData.getString("img");
                                list.add(new data_objek(id_barang,nama_barang,stok_barang,harga_barang,img));
                                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                            }

                            x[0] = list.size();
                            total.setText(String.valueOf(x[0]));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}