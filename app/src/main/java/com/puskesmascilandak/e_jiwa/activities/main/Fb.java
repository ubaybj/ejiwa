package com.puskesmascilandak.e_jiwa.activities.main;


import android.webkit.WebView;
import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;

public class Fb extends Activity {

    public Fb() {
        super(R.layout.activity_fb);
    }

    @Override
    protected void initOnCreate() {
        WebView myWebfb = findViewById(R.id.webviewfb);
        myWebfb.loadUrl("https://www.facebook.com/puskesmas.kecamatancilandak.9");

    }
}

