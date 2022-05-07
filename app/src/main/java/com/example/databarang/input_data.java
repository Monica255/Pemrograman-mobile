package com.example.databarang;

import android.Manifest;
import android.annotation.SuppressLint;
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


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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


public class input_data extends AppCompatActivity {
    EditText nama,stok,harga;
    Button simpan_data,capture, select;
    ImageView imageView;
    Toolbar toolbar;
    Bitmap bitmap,bitmap2;
    String encodedImage;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ProgressDialog progressDialog;
    JsonObjectRequest jsonObjectRequest;
    JSONObject jsonObject;
    String nama_gambar;
    int total,max;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        nama = findViewById(R.id.nama);
        stok = findViewById(R.id.stock);
        harga = findViewById(R.id.harga);
        total= Integer.parseInt(getIntent().getStringExtra("total"));
        max = Integer.parseInt(getIntent().getStringExtra("max"));
        imageView = findViewById(R.id.image_view);
        simpan_data = findViewById(R.id.simpan_data);
        progressDialog = new ProgressDialog(input_data.this);
        progressDialog.setMessage("Image Uploading...");


        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

       /* select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Dexter.withContext(input_data.this)
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
                input_data();
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
                input_data.this);
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
                    Dexter.withContext(input_data.this)
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

                /*UploadImage(bitmap);*/
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

    @Override
    public void onBackPressed() {
        if(bitmap2==null&&nama.getText().toString().trim().equals("")&&
                stok.getText().toString().trim().equals("")&&
                harga.getText().toString().trim().equals("")){
            finish();
        }else{
            AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
            alertdialog.setTitle("Warning");
            alertdialog.setMessage("Yakin ingin keluar? \nperubahan tidak akan tersimpan");
            alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(input_data.this,MainActivity.class);
                    startActivity(intent);
                    input_data.this.finish();
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
    /*private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        byte[]imageByte = stream.toByteArray();
        encodedImage= android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);


    }*/

    /*ActivityResultLauncher<String> mGetContent =registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result!=null){
                        imageView.setImageURI(result);


                    }
                }
            }

    );*/

    void input_data() {
        if (bitmap2 == null || nama.getText().toString().trim().equalsIgnoreCase("") ||
                stok.getText().toString().trim().equalsIgnoreCase("") ||
                harga.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(input_data.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();

        }else  if(total>=max){
            AlertDialog.Builder builder = new AlertDialog.Builder(input_data.this);
            builder.setTitle("Warning!!!");
            builder.setMessage("Jumlah data melebihi kapasitas");
            builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            //Toast.makeText(input_data.this, "Jumlah data melebihi kapasitas", Toast.LENGTH_SHORT).show();
        }else {
            UploadImage(bitmap2);
            String url = "http://192.168.1.20/databarang/tambah.php";

        StringRequest respon = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("oke")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(input_data.this);
                                builder.setTitle("Sukses");
                                builder.setMessage("Data Sukses Di-Input");
                                builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent =new Intent(input_data.this,MainActivity.class);
                                        startActivity(intent);
                                        /*finish();*/
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> form = new HashMap<>();
                form.put("name", nama.getText().toString());
                form.put("stock", stok.getText().toString());
                form.put("price", harga.getText().toString());
                form.put("image", nama_gambar);
                return form;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(respon);

    }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 0: {

                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    progressDialog.show();
                    UploadImage(bitmap);

                }


                //capture
            }
            break;

            case 1: {
                if (resultCode == RESULT_OK) {


                    try {
                        Uri imageUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageView.setImageBitmap(bitmap);
                        progressDialog.show();
                        UploadImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }


    }*/

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
                                Toast.makeText(input_data.this, "" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(input_data.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
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

    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(input_data.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(input_data.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(input_data.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(input_data.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(input_data.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(input_data.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(input_data.this)
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(input_data.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(input_data.this, MainActivity.class));
                                input_data.this.overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(input_data.this,
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
