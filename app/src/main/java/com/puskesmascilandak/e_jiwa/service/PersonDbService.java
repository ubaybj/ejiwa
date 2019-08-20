package com.puskesmascilandak.e_jiwa.service;

import android.content.Context;
import android.database.Cursor;

import com.puskesmascilandak.e_jiwa.model.Person;

public abstract class PersonDbService<P extends Person> extends DatabaseServiceImpl<P>{
    public PersonDbService(Context context, String tableName) {
        super(context, tableName);
    }

    public P findBy(String noKtp) {
        String sql = "SELECT *FROM "+tableName+
                " WHERE no_ktp=?";

        P person = null;
        openReadAble();
        Cursor cursor = database.rawQuery(sql, new String[] {noKtp});

        while (cursor.moveToNext()) {
            person = fetchRow(cursor);
        }

        close();
        return person;
    }
}
