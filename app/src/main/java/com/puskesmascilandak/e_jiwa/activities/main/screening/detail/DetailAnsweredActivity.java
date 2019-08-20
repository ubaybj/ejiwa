package com.puskesmascilandak.e_jiwa.activities.main.screening.detail;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBar;
import android.widget.ListView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;
import com.puskesmascilandak.e_jiwa.adapter.AnsweredItemAdapter;
import com.puskesmascilandak.e_jiwa.model.CheckUp;
import com.puskesmascilandak.e_jiwa.model.DetailCheckUp;
import com.puskesmascilandak.e_jiwa.service.DetailCheckUpDbService;
import com.puskesmascilandak.e_jiwa.util.PopupUtil;

import java.io.Serializable;
import java.util.List;

public class DetailAnsweredActivity extends Activity {

    public DetailAnsweredActivity() {
        super(R.layout.activity_detail_answered);
    }

    @Override
    protected void initOnCreate() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle("Detil Jawaban Pertanyaan");

        final AnsweredItemAdapter adapter = new AnsweredItemAdapter(DetailAnsweredActivity.this);
        ListView listView = findViewById(R.id.list_answered);
        listView.setAdapter(adapter);

        PopupUtil.showLoading(this, "", "Memuat...");
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Serializable serializable = getIntent().getSerializableExtra("check_up");

                if (serializable != null) {
                    CheckUp checkUp = (CheckUp) serializable;
                    DetailCheckUpDbService service = new DetailCheckUpDbService(DetailAnsweredActivity.this);
                    List<DetailCheckUp> detailCheckUps = service.findBy(checkUp);
                    adapter.addAll(detailCheckUps);
                    adapter.notifyDataSetChanged();
                    PopupUtil.dismissDialog();
                }
            }
        });
    }
}
