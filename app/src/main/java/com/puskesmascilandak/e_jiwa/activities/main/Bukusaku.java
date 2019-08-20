package com.puskesmascilandak.e_jiwa.activities.main;

import android.webkit.WebView;
import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;

public class Bukusaku extends Activity {

    public Bukusaku () { super(R.layout.activity_bukusaku);
    }

    @Override
    protected void initOnCreate() {
        WebView myWebViewbukusaku = findViewById(R.id.webviewbukusaku);
        myWebViewbukusaku.loadUrl("http://online.anyflip.com/bqpy/hsrx/");

    }
}

