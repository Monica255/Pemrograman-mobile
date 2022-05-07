package com.example.databarang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class edit_data extends AppCompatActivity {
    EditText nama, stock, harga;
    Toolbar toolbar;
    String  nama_gambar,n,s,h;
    ImageView imageView;
    Bitmap bitmap, bitmap2=null;
    Button simpan_data,capture, select;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog progressDialog;
    JsonObjectRequest jsonObjectRequest;
    JSONObject jsonObject;
    String path ="http://192.168.1.20/databarang/img/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        nama=findViewById(R.id.nama);
        stock=findViewById(R.id.stock);
        harga=findViewById(R.id.harga);
        imageView = findViewById(R.id.image_view);

        /*capture = findViewById(R.id.bt_camera);
        select = findViewById(R.id.bt_gallary);*/
        simpan_data = findViewById(R.id.simpan_data);

        n=getIntent().getStringExtra("name");
        s=getIntent().getStringExtra("stock");
        h=getIntent().getStringExtra("price");
        nama.setText(n);
        stock.setText(s);
        harga.setText(h);
        Glide.with(getApplicationContext())
                .load(path+getIntent().getStringExtra("img"))
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        /*select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(edit_data.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckPermission()) {
                    Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(capture, 0);
                }
            }
        });*/

        simpan_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_data();
            }
        });

    }

    private void selectImage() {
        /*Constants.iscamera = true;*/
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        TextView title = new TextView(this);
        title.setText("Add Photo!");
        title.setBackgroundColor(Color.WHITE);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.GRAY);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                edit_data.this);



        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    if (CheckPermission()) {
                        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(capture, 0);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Dexter.withContext(edit_data.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    Intent intent=new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode==1&& resultCode==RESULT_OK&&data!=null){
            Uri filePath =data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filePath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                bitmap2=bitmap;
                imageView.setImageBitmap(bitmap2);
                /*UploadImage(bitmap2);*/
                /*imageStore(bitmap);*/
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }else if (requestCode==0&&resultCode == RESULT_OK&&data!=null) {

            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                bitmap2=bitmap;
                imageView.setImageBitmap(bitmap2);
                /*progressDialog.show();*/
                /*UploadImage(bitmap);*/
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode,resultCode,data);
    }

    void edit_data(){

        if(bitmap2!= null){
            UploadImage(bitmap2);
        }

        String url = "http://192.168.1.20/databarang/update.php";
        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("oke")){
                                AlertDialog.Builder builder= new AlertDialog.Builder(edit_data.this);
                                builder.setTitle("Sukses");
                                builder.setMessage("Data Sukses Di-Update");
                                builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                /*AlertDialog dialog = builder.create();*/
                                AlertDialog.Builder dialog= new AlertDialog.Builder(edit_data.this);
                                dialog.setMessage("Data Sudah diUpdate");
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /*newtext = input.getText().toString();*/
                                        startActivity(new Intent(getApplicationContext(),tampil_data.class));
                                    }
                                });
                                dialog.show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(bitmap2!= null){
                    Map<String,String> form=new HashMap<>();
                    form.put("id", getIntent().getStringExtra("id"));
                    form.put("name",nama.getText().toString());
                    form.put("stock",stock.getText().toString());
                    form.put("price",harga.getText().toString());
                    form.put("img",nama_gambar);
                    return form;
                }else{

                    Map<String,String> form=new HashMap<>();
                    form.put("id", getIntent().getStringExtra("id"));
                    form.put("name",nama.getText().toString());
                    form.put("stock",stock.getText().toString());
                    form.put("price",harga.getText().toString());
                    form.put("img",getIntent().getStringExtra("img").replace(".JPG","").replace(".jpg",""));
                    return form;
                }

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(respon);
    }
    private void UploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String name = String.valueOf(Calendar.getInstance().getTimeInMillis());

        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("image", image);
            nama_gambar = name;
            System.out.println("aa"+name);
            System.out.println("bb"+nama_gambar);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.20/databarang/upload.php", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                Toast.makeText(edit_data.this, "" + message, Toast.LENGTH_SHORT).show();
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(edit_data.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }


            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onBackPressed() {
        if(bitmap2==null&&nama.getText().toString().trim().equals(n)&&
                stock.getText().toString().trim().equals(s)&&
                harga.getText().toString().trim().equals(h)){
            finish();
        }else{
            AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
            alertdialog.setTitle("Warning");
            alertdialog.setMessage("Yakin ingin keluar? \nperubahan tidak akan tersimpan");
            alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(edit_data.this,tampil_data.class);
                    startActivity(intent);
                    edit_data.this.finish();
                }
            });
            alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertdialog.create();
            alertdialog.show();
        }



    }

    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(edit_data.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(edit_data.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(edit_data.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(edit_data.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(edit_data.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(edit_data.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(edit_data.this)
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(edit_data.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(edit_data.this, MainActivity.class));
                                edit_data.this.overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(edit_data.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }

    /*@Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}