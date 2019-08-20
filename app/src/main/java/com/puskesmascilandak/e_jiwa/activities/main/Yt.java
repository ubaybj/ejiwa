package com.puskesmascilandak.e_jiwa.activities.main;


import android.webkit.WebView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;

public class Yt extends Activity {

    public Yt() {
        super(R.layout.activity_yt);
    }

    @Override
    protected void initOnCreate() {
        WebView myWebfb = findViewById(R.id.webviewyt);
        myWebfb.loadUrl("https://www.youtube.com/channel/UCJdsNCciW3jxXe253XNty2A");

    }
}

