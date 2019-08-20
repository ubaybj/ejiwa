package com.puskesmascilandak.e_jiwa.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.Session;
import com.puskesmascilandak.e_jiwa.activities.Activity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.HistoryCheckUpActivity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.LoginActivity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.register.FormPasienActivity;
import com.puskesmascilandak.e_jiwa.adapter.MainMenuAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScreeningActivity extends Activity {
    private MainMenuAdapter adapter;

    public ScreeningActivity() {
        super(R.layout.activity_screening);
    }

    @Override
    protected void initOnCreate() {
        setTitle("Screening E-Jiwa");
        adapter = new MainMenuAdapter(this);
        adapter.addAll(createMenus());
        adapter.notifyDataSetChanged();

        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                startChosenActivityOn(position);
            }
        });

    }

    private List<MainMenuAdapter.Menu> createMenus() {
        MainMenuAdapter.Menu menuCheckUp =
                createMenu(R.mipmap.ic_check_up, "Check UP", "Mulai Check Up Kejiwaan Pasien");
        MainMenuAdapter.Menu menuHistoryCheckUp =
                createMenu(R.mipmap.ic_history, "Riwayat Check Up", "Melihat Data Check Up Tersimpan");
        MainMenuAdapter.Menu menuLogOut =
                createMenu(R.mipmap.ic_log_out, "Log Out", "Log Out Dari Aplikasi");
        MainMenuAdapter.Menu menuKeluar =
                createMenu(R.mipmap.ic_exit, "Keluar", "Keluar Dari Aplikasi");



        List<MainMenuAdapter.Menu> menus = new ArrayList<>();

        menus.add(menuCheckUp);
        menus.add(menuHistoryCheckUp);
        menus.add(menuLogOut);
        menus.add(menuKeluar);


        return menus;
    }

    private MainMenuAdapter.Menu createMenu(int drawable, String title, String description) {
        MainMenuAdapter.Menu menu = new MainMenuAdapter.Menu();

        menu.setDrawable(drawable);
        menu.setTitle(title);
        menu.setDescription(description);

        return menu;
    }

    private void startChosenActivityOn(int position) {
        MainMenuAdapter.Menu menu = adapter.getItem(position);

        if (menu != null) {
            switch (menu.getTitle()) {
                case "Check UP" : startRegisterPasienActivity(); break;
                case "Riwayat Check Up" : startHistoryCheckUpActivity(); break;
                case "Log Out" : startLoginActivity(); break;
                case "Keluar" : finish();
            }
        }
    }

    private void startRegisterPasienActivity() {
        Intent intent = new Intent(this, FormPasienActivity.class);
        startActivity(intent);
    }

    private void startHistoryCheckUpActivity() {
        Intent intent = new Intent(this, HistoryCheckUpActivity.class);
        startActivity(intent);
    }


    private void startLoginActivity() {
        Session session = new Session(this);
        session.quit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
