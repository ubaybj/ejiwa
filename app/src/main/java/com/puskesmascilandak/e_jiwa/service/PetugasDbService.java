package com.puskesmascilandak.e_jiwa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.puskesmascilandak.e_jiwa.model.Petugas;

public class PetugasDbService extends PersonDbService<Petugas> {

    public PetugasDbService(Context context) {
        super(context, "petugas");
    }

    @Override
    protected ContentValues createContentValuesFrom(Petugas entity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("nama", entity.getNama());
        contentValues.put("tgl_lahir", entity.getTglLahir());
        contentValues.put("alamat", entity.getAlamat());
        contentValues.put("no_telp", entity.getNoTelp());
        contentValues.put("no_ktp", entity.getNoKtp());
        contentValues.put("kecamatan", entity.getKecamatan());

        return contentValues;
    }

    @Override
    protected Petugas fetchRow(Cursor cursor) {
        Petugas petugas = new Petugas();

        petugas.setId(getIntFrom(cursor, "_id"));
        petugas.setNama(getStringFrom(cursor, "nama"));
        petugas.setTglLahir(getStringFrom(cursor, "tgl_lahir"));
        petugas.setAlamat(getStringFrom(cursor, "alamat"));
        petugas.setNoTelp(getStringFrom(cursor, "no_telp"));
        petugas.setNoKtp(getStringFrom(cursor, "no_ktp"));
        petugas.setKecamatan(getStringFrom(cursor, "kecamatan"));

        return petugas;
    }
}
