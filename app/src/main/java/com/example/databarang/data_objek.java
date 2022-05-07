package com.example.databarang;

public class data_objek {
    String id_barang="", nama="",stok ="", harga="",img="";

    public data_objek(String id_barang, String nama , String stok, String harga, String img){
        this.id_barang = id_barang;
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
        this.img = img;
    }

    public String getId_barang() {
        return id_barang;
    }

    public String getNama() {
        return nama;
    }

    public String getStok() {
        return stok;
    }

    public String getHarga() {
        return harga;
    }


    public String getImg() {
        return img;
    }
}
