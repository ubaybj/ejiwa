package com.puskesmascilandak.e_jiwa.activities.main.screening;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;

public class InfoSelfassesment extends Activity {
    public InfoSelfassesment() {
        super(R.layout.activity_assesmentinfo);
    }


    @Override
    protected void initOnCreate() {
        ImageButton infomulai = findViewById(R.id.mulai);

        infomulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelfAssesmentActivity.class);
                startActivity(intent);
            }
        });
    }
}
