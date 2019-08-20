package com.puskesmascilandak.e_jiwa.activities.main.screening.register;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.InputActivity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.LoginActivity;
import com.puskesmascilandak.e_jiwa.model.Petugas;
import com.puskesmascilandak.e_jiwa.model.User;
import com.puskesmascilandak.e_jiwa.service.PetugasDbService;
import com.puskesmascilandak.e_jiwa.service.UserDbService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormUserActivity extends InputActivity {
    private Petugas petugas;
    @BindView(R.id.input_username) EditText inputUsername;
    @BindView(R.id.input_password) EditText inputPassword;
    @BindView(R.id.input_retype_password) EditText inputRetypePassword;

    public FormUserActivity() {
        super(R.layout.activity_form_user);
    }

    @Override
    protected void initOnCreate() {
        ButterKnife.bind(this);

        Button cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });

        Button registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanUser();
            }
        });

        petugas = (Petugas) getIntent().getSerializableExtra("petugas");
    }

    private boolean isDataPetugasSaved() {
        try {
            PetugasDbService service = new PetugasDbService(this);
            service.simpan(petugas);
            return true;
        } catch (SQLiteException e) {
            showLongTimeToast("Gagal Menyimpan Data Petugas");
            return false;
        }
    }

    private void simpanUser() {
        if (!validateAllInput()) return;

        User user = new User();
        initData(user);

        UserDbService service = new UserDbService(this);
        try {
            service.simpan(user);
            startLoginActivity();
            finish();
        } catch (SQLiteException e) {
            deletePetugas();
            e.printStackTrace();
            showLongTimeToast("Tidak Dapat Menyimpan Data User");
        }
    }

    private void deletePetugas() {
        PetugasDbService service = new PetugasDbService(this);
        try {
            service.delete(petugas);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    private void initData(User user) {
        user.setPetugas(petugas);
        user.setUsername(getValueFrom(inputUsername));
        user.setPassword(getValueFrom(inputPassword));
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean validateUsername() {
        String username = getValueFrom(inputUsername);

        if (username.isEmpty()) {
            inputUsername.setError("Username Masih Kosong");
            return false;
        }

        UserDbService service = new UserDbService(this);
        User user = service.findBy(username);
        if (user != null) {
            inputUsername.setError("Username Sudah Ada Yang Menggunakan");
            inputUsername.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        String password = getValueFrom(inputPassword);

        if (password.isEmpty()) {
            inputPassword.setError("Password Masih Kosong");
            return false;
        }

        if (password.length() < 6) {
            inputPassword.setError("Jumlah Karakter Password Minimal 6");
            return false;
        }

        return true;
    }

    private boolean validateRetypePassword() {
        String retypePassword = getValueFrom(inputRetypePassword);

        if (retypePassword.isEmpty()) {
            inputRetypePassword.setError("Ketik Ulang Password Anda");
            return false;
        }

        String password = getValueFrom(inputPassword);
        boolean matched = password.equals(retypePassword);
        if (!matched) {
            inputRetypePassword.setError("Password Yang Anda Ketik Tidak Cocok");
            return false;
        }

        return true;
    }

    @Override
    protected boolean validateAllInput() {
        boolean inputValid = validateUsername() &
                validatePassword() &
                validateRetypePassword();

        if (petugas == null) {
            showLongTimeToast("Tidak Ada Data Petugas");
            return false;
        }

        return inputValid && isDataPetugasSaved();
    }
}
