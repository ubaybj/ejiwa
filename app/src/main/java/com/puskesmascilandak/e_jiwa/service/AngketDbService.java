package com.puskesmascilandak.e_jiwa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.puskesmascilandak.e_jiwa.model.Angket;

public class AngketDbService extends DatabaseServiceImpl<Angket> {
    public AngketDbService(Context context) {
        super(context, "angket");
    }

    @Override
    protected ContentValues createContentValuesFrom(Angket entity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("question", entity.getQuestion());

        return contentValues;
    }

    @Override
    protected Angket fetchRow(Cursor cursor) {
        Angket angket = new Angket();

        angket.setId(getIntFrom(cursor, "_id"));
        angket.setQuestion(getStringFrom(cursor, "question"));

        return angket;
    }
}
