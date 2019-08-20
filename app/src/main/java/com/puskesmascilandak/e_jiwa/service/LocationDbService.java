package com.puskesmascilandak.e_jiwa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.puskesmascilandak.e_jiwa.model.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationDbService {
    private DatabaseHelper databaseHelper;
    private Context context;
    private SQLiteDatabase database;

    public LocationDbService(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void insert(long kelurahanId, String keluarahan,
                       long kecamatanId, String kecamatan,
                       long kotaId, String kota) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("kelurahan_id", kelurahanId);
        contentValues.put("kecamatan_id", kecamatanId);
        contentValues.put("kota_id", kotaId);
        contentValues.put("kelurahan", keluarahan);
        contentValues.put("kecamatan", kecamatan);
        contentValues.put("kota", kota);

        database.insert("location", null, contentValues);
    }

    private Location fetchRow(Cursor cursor) {
        Location location = new Location();
        location.setKelurahanId(cursor.getLong(0));
        location.setKecamatanId(cursor.getLong(1));
        location.setKotaId(cursor.getLong(2));
        location.setKelurahan(cursor.getString(3));
        location.setKecamatan(cursor.getString(4));
        location.setKota(cursor.getString(5));

        return location;
    }

    public List<String> getKota() {
        String sql = "SELECT DISTINCT kota FROM location ORDER BY kota ASC";
        Cursor cursor = database.rawQuery(sql, null);

        List<String> locationList = new ArrayList<>();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            locationList.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return locationList;
    }

    public List<String> getAllKecamatan() {
        String sql = "SELECT DISTINCT kecamatan FROM location ORDER BY kecamatan";

        Cursor cursor = database.rawQuery(sql, null);

        List<String> locationList = new ArrayList<>();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            locationList.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        return locationList;
    }

    public List<String> getKecamatan(String kota) {
        String sql = "SELECT DISTINCT kecamatan FROM location WHERE kota=? ORDER BY kecamatan ASC";
        Cursor cursor = database.rawQuery(sql, new String[]{kota});

        List<String> locationList = new ArrayList<>();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            locationList.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return locationList;
    }

    public List<String> getKelurahan(String kecamatan) {
        String sql = "SELECT DISTINCT kelurahan FROM location WHERE kecamatan=? ORDER BY kelurahan ASC";
        Cursor cursor = database.rawQuery(sql, new String[]{kecamatan});

        List<String> locationList = new ArrayList<>();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            locationList.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return locationList;
    }
}
