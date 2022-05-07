package com.example.databarang;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
/*import android.widget.SearchView;*/
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;




public class tampil_data extends AppCompatActivity {
    Toolbar toolbar;
    ArrayList<data_objek> list;
    ListView listView;
    SearchView sv;
    Adapter arrayAdapter;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_data);
        toolbar=findViewById(R.id.toolbar);
        listView=findViewById(R.id.listview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        tampil_data();



    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    void tampil_data(){
        list=new ArrayList<>();
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

                            Adapter adapter = new Adapter(tampil_data.this,list);
                            listView.setAdapter(adapter);
                            sv= (androidx.appcompat.widget.SearchView) findViewById(R.id.search);
                            sv.setIconifiedByDefault(false);
                            sv.setSubmitButtonEnabled(true);
                            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }
                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    ArrayList<data_objek> filtered = new ArrayList<data_objek>();
                                    for (data_objek brg : list) {
                                        if ((brg.getNama().toLowerCase().trim().contains(newText)))
                                        {
                                            filtered.add(brg);
                                        }
                                    }
                                    Adapter adapter1 = new Adapter(tampil_data.this,filtered);
                                    listView.setAdapter(adapter1);
                                    return false;
                                }
                            });




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




    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

class Adapter extends ArrayAdapter<data_objek>  {

    Context context;
    LayoutInflater inflater;
    ArrayList<data_objek> model;
    ArrayList<data_objek> barangFiltered;
    public Adapter(Context context, ArrayList<data_objek>model){
        super(context,0,model);
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.model=model;
    }


    @Override
    public int getCount() {
        return model.size();
    }

    /*@Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }*/

    TextView nama,stok,harga;
    Button lihat,edit,hapus;
    ImageView img;



    @Override
    public View getView(final int position, View contextView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.activity_list_data,parent,false);
        String path ="http://192.168.1.20/databarang/img/";


        edit=view.findViewById(R.id.edit);
        hapus=view.findViewById(R.id.hapus);

        nama=view.findViewById(R.id.nama2);
        stok=view.findViewById(R.id.stock2);
        harga=view.findViewById(R.id.harga2);
        img = view.findViewById(R.id.img_barang);


        nama.setText(model.get(position).getNama());
        stok.setText(model.get(position).getStok());
        harga.setText(model.get(position).getHarga());
        Glide.with(context.getApplicationContext())
                .load(path+model.get(position).getImg())
                .into(img);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,lihat_data.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",model.get(position).getNama());
                intent.putExtra("stock",model.get(position).getStok());
                intent.putExtra("price",model.get(position).getHarga());
                intent.putExtra("img",model.get(position).getImg());
                context.startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,edit_data.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",model.get(position).getId_barang());
                intent.putExtra("name",model.get(position).getNama());
                intent.putExtra("stock",model.get(position).getStok());
                intent.putExtra("price",model.get(position).getHarga());
                intent.putExtra("img",model.get(position).getImg());

                System.out.println(model.get(position).getId_barang());
                System.out.println(model.get(position).getNama());
                System.out.println(model.get(position).getStok());
                System.out.println(model.get(position).getHarga());
                System.out.println(model.get(position).getImg());
                context.startActivity(intent);
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                confirmDialog(position);

            }
        });

        return view;
    }

    /*@Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final String text = charSequence.toString().toLowerCase();
                ArrayList<data_objek> list2;
                if (text.isEmpty()) {
                    list2 = model; //"entitySongs" is your original arraylist
                }else {
                    ArrayList<data_objek> listbarang = new ArrayList<>();


                    for (data_objek brg : model) {
                        if ((brg.getNama().toLowerCase().trim().contains(text)))
                        {
                            listbarang.add(new data_objek(brg.getId_barang(), brg.getNama(), brg.getStok(),brg.getHarga(), brg.getImg()));
                        }
                    }

                    list2 = listbarang;
                }

                FilterResults results = new FilterResults();
                results.values = list2;
                results.count = getCount();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                barangFiltered = (ArrayList<data_objek>) filterResults.values; // //"songFiltered" is your filtered arraylist
                notifyDataSetChanged();

            }
        };
    }*/

    private void confirmDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setIcon(R.drawable.warning)
                .setMessage("Yakin akan menghapus barang "+ model.get(position).getNama()+" ?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code
                        hapus_data(model.get(position).getId_barang(),model.get(position).getImg());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
    void hapus_data(String kode_barang, String img){

        String url = "http://192.168.1.20/databarang/hapus.php?kode_barang="+kode_barang;
        StringRequest stringRequest= new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("oke")){
                                Toast.makeText(context, "Data Berhasil Di-Hapus", Toast.LENGTH_SHORT).show();
                                
                            }
                        } catch (JSONException e) {
                            System.out.println("error");
                            e.printStackTrace();
                        }
                        //Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context,MainActivity.class);
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}

