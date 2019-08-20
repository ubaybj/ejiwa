package com.puskesmascilandak.e_jiwa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.puskesmascilandak.e_jiwa.model.CheckUp;
import com.puskesmascilandak.e_jiwa.model.Pasien;
import com.puskesmascilandak.e_jiwa.model.Petugas;

public class CheckUpDbService extends DatabaseServiceImpl<CheckUp> {

    public CheckUpDbService(Context context) {
        super(context, "check_up");
    }

    @Override
    protected ContentValues createContentValuesFrom(CheckUp entity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("tgl_check_up", entity.getTglCheckUp());
        contentValues.put("idpasien", entity.getPasien().getId());
        contentValues.put("idpetugas", entity.getPetugas().getId());
        contentValues.put("score", entity.getScore());
        contentValues.put("warna", entity.getWarna());
        contentValues.put("keterangan", entity.getKeterangan());
        return contentValues;
    }

    @Override
    protected CheckUp fetchRow(Cursor cursor) {
        CheckUp checkUp = new CheckUp();

        checkUp.setId(getIntFrom(cursor, "_id"));
        checkUp.setKeterangan(getStringFrom(cursor, "keterangan"));
        checkUp.setScore(getIntFrom(cursor, "score"));
        checkUp.setWarna(getStringFrom(cursor, "warna"));

        checkUp.setTglCheckUp(getStringFrom(cursor, "tgl_check_up"));

        int idPetugas = getIntFrom(cursor, "idpetugas");
        Petugas petugas = new PetugasDbService(context).findBy(idPetugas);
        checkUp.setPetugas(petugas);

        int idPasien = getIntFrom(cursor, "idpasien");
        Pasien pasien = new PasienDbService(context).findBy(idPasien);
        checkUp.setPasien(pasien);

        return checkUp;
    }
}
