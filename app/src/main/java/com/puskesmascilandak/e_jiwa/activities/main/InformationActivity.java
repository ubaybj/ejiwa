package com.puskesmascilandak.e_jiwa.activities.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;

public class InformationActivity extends Activity {

    public InformationActivity()  {
        super(R.layout.activity_information);
    }

    @Override
    protected void initOnCreate() {
        TextView bukusaku = findViewById(R.id.textbukusaku);
        TextView videoejiwa = findViewById(R.id.videoejiwa);
        ImageButton fb      = findViewById(R.id. fb);
        ImageButton ig = findViewById(R.id.ig);
        ImageButton yt = findViewById(R.id.yt);

        videoejiwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Videoejiwa.class);
                startActivity(intent);
            }
        });

        bukusaku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Bukusaku.class);
                startActivity(intent);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Fb.class);
                startActivity(intent);
            }
        });

        yt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Yt.class);
                startActivity(intent);
            }
        });

        ig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Ig.class);
                startActivity(intent);
            }
        });

    }
}

