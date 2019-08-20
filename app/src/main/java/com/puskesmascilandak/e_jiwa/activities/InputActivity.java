package com.puskesmascilandak.e_jiwa.activities;

import android.widget.EditText;

public abstract class InputActivity extends Activity {
    public InputActivity(int layoutId) {
        super(layoutId);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected abstract boolean validateAllInput();

    protected String getValueFrom(EditText editText) {
        if (editText == null) return "";
        else return editText.getText().toString();
    }
}
