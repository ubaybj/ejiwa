package com.puskesmascilandak.e_jiwa.activities.main;

import android.content.Intent;
import android.os.Handler;
import android.widget.ProgressBar;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends Activity {
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    public SplashScreenActivity() {
        super(R.layout.activity_splash_screen);
    }

    @Override
    protected void initOnCreate() {
        ButterKnife.bind(this);
        updateProgress();
    }

    private void updateProgress() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentProgress = progressBar.getProgress();
                progressBar.setProgress(currentProgress + 10);

                if (currentProgress < 100) {
                    updateProgress();
                } else {
                    startLoginActivity();
                }
            }
        }, 300);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
