package com.puskesmascilandak.e_jiwa.activities.main;

import android.webkit.WebView;
import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;

public class Videoejiwa extends Activity {

    public Videoejiwa () { super(R.layout.activity_videoejiwa);
    }

    @Override
    protected void initOnCreate() {
        WebView myWebViewvideoejiwa = findViewById(R.id.webviewvideoejiwa);
        myWebViewvideoejiwa.loadUrl("https://youtu.be/virKdl9thas");

    }
}