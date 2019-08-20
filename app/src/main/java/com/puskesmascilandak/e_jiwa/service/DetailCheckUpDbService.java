package com.puskesmascilandak.e_jiwa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.puskesmascilandak.e_jiwa.model.Angket;
import com.puskesmascilandak.e_jiwa.model.CheckUp;
import com.puskesmascilandak.e_jiwa.model.DetailCheckUp;

import java.util.ArrayList;
import java.util.List;

public class DetailCheckUpDbService extends DatabaseServiceImpl<DetailCheckUp> {
    public DetailCheckUpDbService(Context context) {
        super(context, "detail_check_up");
    }

    public List<DetailCheckUp> findBy(CheckUp checkUp) {
        openReadAble();

        List<DetailCheckUp> detailCheckUps = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE idcheck_up=?";
        Cursor cursor = database.rawQuery(sql, new String[]{Long.toString(checkUp.getId())});

        while (cursor.moveToNext()) {
            DetailCheckUp detailCheckUp = fetchRow(cursor);
            detailCheckUps.add(detailCheckUp);
        }

        cursor.close();
        close();

        return detailCheckUps;
    }

    @Override
    protected ContentValues createContentValuesFrom(DetailCheckUp entity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("idcheck_up", entity.getCheckUp().getId());
        contentValues.put("idangket", entity.getAngket().getId());
        contentValues.put("answer", entity.getAnswer());
        return contentValues;
    }

    @Override
    protected DetailCheckUp fetchRow(Cursor cursor) {
        DetailCheckUp detailCheckUp = new DetailCheckUp();

        detailCheckUp.setId(getIntFrom(cursor, "_id"));
        detailCheckUp.setAnswer(getStringFrom(cursor, "answer"));

        int idCheckUp = getIntFrom(cursor, "idcheck_up");
        CheckUp checkUp = new CheckUpDbService(context).findBy(idCheckUp);
        detailCheckUp.setCheckUp(checkUp);

        int idAngket = getIntFrom(cursor, "idangket");
        Angket angket = new AngketDbService(context).findBy(idAngket);
        detailCheckUp.setAngket(angket);

        return detailCheckUp;
    }
}
