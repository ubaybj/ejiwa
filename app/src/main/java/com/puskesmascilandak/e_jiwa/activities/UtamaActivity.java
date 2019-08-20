package com.puskesmascilandak.e_jiwa.activities;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.Session;
import com.puskesmascilandak.e_jiwa.activities.main.InformationActivity;
import com.puskesmascilandak.e_jiwa.activities.main.ScreeningActivity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.LoginActivity;


public class UtamaActivity extends Activity {


    public UtamaActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void initOnCreate() {
        //inisial tombol
        ImageButton infoBtn = findViewById(R.id.btn_info);
        ImageButton screenBtn = findViewById(R.id.btn_screen);


        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
                startActivity(intent);
            }
        });



        // function tombol
        screenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = new Session(UtamaActivity.this);

                Class activityClass;
                if (session.getUser() == null) {
                    activityClass = LoginActivity.class;
                } else {
                    activityClass = ScreeningActivity.class;
                }

                Intent intent = new Intent(UtamaActivity.this, activityClass);
                startActivity(intent);
            }
        });

    }
}
