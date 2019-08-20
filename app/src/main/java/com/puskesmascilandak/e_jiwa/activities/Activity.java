package com.puskesmascilandak.e_jiwa.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.puskesmascilandak.e_jiwa.util.DialogHelper;

public abstract class Activity extends AppCompatActivity {
    private final int layoutId;
    private Bundle savedInstance;

    public Activity(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        this.savedInstance = savedInstanceState;

        initOnCreate();
    }

    protected abstract void initOnCreate();

    public void showDialog(String title, String message) {
        DialogHelper.showDialog(this, title, message);
    }

    public void showLongTimeToast(String message) {
        DialogHelper.showLongTimeToast(this, message);
    }

    public void showShortTimeToast(String message) {
        DialogHelper.showShortTimeToast(this, message);
    }

    protected Bundle getSavedInstance() {
        return savedInstance;
    }
}
