package com.puskesmascilandak.e_jiwa.activities.main.screening.register;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.model.Person;
import com.puskesmascilandak.e_jiwa.model.Petugas;
import com.puskesmascilandak.e_jiwa.service.LocationDbService;
import com.puskesmascilandak.e_jiwa.service.PetugasDbService;

import java.util.List;

import butterknife.BindView;

public class FormPetugasActivity extends FormPersonActivity {
    private Petugas petugas;
    @BindView(R.id.kecamatan_sp) Spinner kecamatanSp;

    public FormPetugasActivity() {
        super(R.layout.activity_form_petugas);
    }

    private void loadKecamatan() {
        LocationDbService service = new LocationDbService(this);
        List<String> kecamatanList = service.getAllKecamatan();

        ArrayAdapter<String> kecamatanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, kecamatanList);
        kecamatanSp.setAdapter(kecamatanAdapter);
    }
    @Override
    protected void initOnCreate() {
        super.initOnCreate();
        initUpNavigation();

        Button clearInputBtn = findViewById(R.id.clear_input_btn);
        clearInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });

        Button nextStepBtn = findViewById(R.id.next_step_btn);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterUserActivity();
            }
        });

        loadKecamatan();
    }

    private void initUpNavigation() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected boolean validateNoKtp() {
        if (!super.validateNoKtp()) return false;

        try {
            String noKtp = getValueFrom(inputNoKtp);
            PetugasDbService service = new PetugasDbService(this);
            petugas = service.findBy(noKtp);

            if (petugas != null) {
                showDialog("Gagal", "Sudah Ada Data Petugas Dengan No. Ktp : "+noKtp);
                return false;
            }

            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            showDialog("Gagal", "Tidak Dapat Terhubung Ke Database");
            return false;
        }
    }

    @Override
    protected void initData(Person person) {
        super.initData(person);
        ((Petugas) person).setKecamatan(kecamatanSp.getSelectedItem().toString());
    }

    private void startRegisterUserActivity() {
        if (!validateAllInput()) return;

        petugas = new Petugas();
        initData(petugas);

        Intent intent = new Intent(this, FormUserActivity.class);
        intent.putExtra("action", "sign_up");
        intent.putExtra("petugas", petugas);
        startActivity(intent);
        finish();
    }
}
