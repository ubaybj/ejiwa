package com.puskesmascilandak.e_jiwa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.puskesmascilandak.e_jiwa.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseServiceImpl<E extends AbstractEntity> implements DatabaseService<E> {
    protected SQLiteDatabase database;
    private DatabaseHelper helper;
    protected final String tableName;
    protected final Context context;

    public DatabaseServiceImpl(Context context, String tableName) {
        helper = new DatabaseHelper(context);
        this.tableName = tableName;
        this.context = context;
    }

    protected void openReadAble() {
        database = helper.getReadableDatabase();
    }

    protected void openWriteAble() {
        database = helper.getWritableDatabase();
    }

    protected void close() {
        helper.close();
    }

    @Override
    public List<E> getAll() {
        openReadAble();
        String sql = "SELECT *FROM "+tableName;
        Cursor cursor = database.rawQuery(sql, null);

        List<E> entities = new ArrayList<>();
        while (cursor.moveToNext()) {
            E entity = fetchRow(cursor);
            entities.add(entity);
        }

        cursor.close();
        close();
        return entities;
    }

    @Override
    public E findBy(long id) {
        String sql = "SELECT *FROM "+tableName +" WHERE _id=?";

        openReadAble();
        Cursor cursor = database.rawQuery(sql, new String[] { Long.toString(id) });

        E entity = null;
        while (cursor.moveToNext()) {
            entity = fetchRow(cursor);
        }

        cursor.close();
        close();
        return entity;
    }

    @Override
    public void simpan(E entity) {
        openWriteAble();
        ContentValues contentValues = createContentValuesFrom(entity);
        long newId = database.insert(tableName, null, contentValues);
        entity.setId(newId);
        close();
    }

    @Override
    public void perbaharui(E entity) {
        openWriteAble();
        ContentValues contentValues = createContentValuesFrom(entity);
        database.update(tableName, contentValues, "_id=?",
                new String[] { Long.toString(entity.getId()) });
        close();
    }

    @Override
    public void delete(E entity) {
        openWriteAble();
        database.delete(tableName, "_id=?",
                new String[] { Long.toString(entity.getId()) });
        close();
    }

    public void deleteAll() {
        openWriteAble();
        database.delete(tableName, null, null);
        close();
    }

    protected abstract ContentValues createContentValuesFrom(E entity);

    protected abstract E fetchRow(Cursor cursor);

    protected int getIntFrom(Cursor cursor, String columnName) {
        int indexColumn = cursor.getColumnIndex(columnName);
        return cursor.getInt(indexColumn);
    }

    protected String getStringFrom(Cursor cursor, String columnName) {
        int indexColumn = cursor.getColumnIndex(columnName);
        return cursor.getString(indexColumn);
    }
}
