package com.puskesmascilandak.e_jiwa.activities.main.screening;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.Session;
import com.puskesmascilandak.e_jiwa.activities.InputActivity;
import com.puskesmascilandak.e_jiwa.activities.main.ScreeningActivity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.register.FormPetugasActivity;
import com.puskesmascilandak.e_jiwa.model.User;
import com.puskesmascilandak.e_jiwa.service.UserDbService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends InputActivity {
    @BindView(R.id.input_username) EditText inputUsername;
    @BindView(R.id.input_password) EditText inputPasword;
    @BindView(R.id.teskepribadian_textview) TextView selfAsssestmentTv;

    public LoginActivity() {
        super(R.layout.activity_login);
    }

    @Override
    protected void initOnCreate() {
        setTitle("Login");
        ButterKnife.bind(this);

        Button button = findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        TextView signUpTextView = findViewById(R.id.sign_up_textview);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });

        selfAsssestmentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterPasienActivity();
            }
        });
    }

    private void startRegisterPasienActivity() {
        Intent intent = new Intent(this, InfoSelfassesment.class);
        startActivity(intent);
        finish();
    }

    private void startSignUpActivity() {
        Intent intent = new Intent(this, FormPetugasActivity.class);
        startActivity(intent);
    }

    private void login() {
        if (!validateAllInput()) return;

        String username = getValueFrom(inputUsername);
        String password = getValueFrom(inputPasword);

        UserDbService service = new UserDbService(this);
        User user = service.findBy(username);

        if (user != null) {

            if (user.getPassword().equals(password)) {
                Session session = new Session(this);
                session.setUsername(username);
                startMainActivity();
            } else  {
                showDialog("Gagal", "Wrong Username / Password");
                //showToast("Wrong Username / Password");
            }

        } else {
            showDialog("Gagal", "Tidak Dapat Menemukan User Dengan Username : " + username);
            //showToast("Couldn't Find User With Username : " + username);
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, ScreeningActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateUsername() {
        String username = getValueFrom(inputUsername);

        if (username.isEmpty()) {
            inputUsername.setError("Username Required");
            inputUsername.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        String password = getValueFrom(inputPasword);

        if (password.isEmpty()) {
            inputPasword.setError("Password Required");
            inputPasword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            inputPasword.setError("Password Invalid");
            inputPasword.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected boolean validateAllInput() {
        return validateUsername() && validatePassword();
    }
}
