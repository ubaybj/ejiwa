package com.puskesmascilandak.e_jiwa.activities.main;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;

public class Ig extends Activity {

    public Ig() {
        super(R.layout.activity_ig);
    }

    @Override
    protected void initOnCreate() {

        Uri uri = Uri.parse("http://instagram.com/_u/puskesmaskecamatancilandak");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/puskesmaskecamatancilandak")));
        }

    }
}

