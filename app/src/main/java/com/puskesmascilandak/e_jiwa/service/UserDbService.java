package com.puskesmascilandak.e_jiwa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.puskesmascilandak.e_jiwa.model.Petugas;
import com.puskesmascilandak.e_jiwa.model.User;

public class UserDbService extends DatabaseServiceImpl<User> {

    public UserDbService(Context context) {
        super(context, "user");
    }

    public User findBy(String username) {
        openReadAble();
        String sql = "SELECT *FROM "+tableName + " WHERE username=?";
        Cursor cursor = database.rawQuery(sql, new String[] { username });

        User user = null;
        while (cursor.moveToNext()) {
            user = fetchRow(cursor);
        }

        cursor.close();
        close();

        return user;
    }

    @Override
    protected User fetchRow(Cursor cursor) {
        User user = new User();
        int idPetugas = getIntFrom(cursor, "idpetugas");
        Petugas petugas = new PetugasDbService(context).findBy(idPetugas);

        user.setId(getIntFrom(cursor, "_id"));
        user.setPassword(getStringFrom(cursor, "password"));
        user.setUsername(getStringFrom(cursor, "username"));
        user.setPetugas(petugas);

        return user;
    }

    @Override
    protected ContentValues createContentValuesFrom(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("idpetugas", user.getPetugas().getId());

        return contentValues;
    }
}
