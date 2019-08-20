package com.puskesmascilandak.e_jiwa.activities.main.screening;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.Session;
import com.puskesmascilandak.e_jiwa.activities.InputActivity;
import com.puskesmascilandak.e_jiwa.model.Pasien;
import com.puskesmascilandak.e_jiwa.model.Person;
import com.puskesmascilandak.e_jiwa.model.Petugas;
import com.puskesmascilandak.e_jiwa.model.User;
import com.puskesmascilandak.e_jiwa.service.LocationDbService;
import com.puskesmascilandak.e_jiwa.service.PasienDbService;
import com.puskesmascilandak.e_jiwa.util.DialogHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelfAssesmentActivity extends InputActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.sp_kota) Spinner kotaSpinner;
    @BindView(R.id.sp_kecamatan) Spinner kecamatanSpinner;
    @BindView(R.id.sp_kelurahan) Spinner kelurahanSpinner;

    @BindView(R.id.input_nama_lengkap)
    protected EditText inputNamaLengkap;
    @BindView(R.id.input_tgl_lahir)
    protected EditText inputTglLahir;
    @BindView(R.id.input_alamat)
    protected EditText inputAlamat;
    @BindView(R.id.input_no_telp)
    protected EditText inputNoTelp;
    @BindView(R.id.input_no_ktp)
    protected EditText inputNoKtp;
    @BindView(R.id.pria_rb)
    protected RadioButton priaRb;
    @BindView(R.id.wanita_rb)
    protected RadioButton wanitaRb;

    private ArrayAdapter kotaAdapter;
    private ArrayAdapter kecamatanAdapter;
    private ArrayAdapter kelurahanAdapter;

    private List<String> kotaList;
    private List<String> kecamatanList;
    private List<String> kelurahanList;

    public SelfAssesmentActivity() {
        super(R.layout.activity_self_assesment);
    }

    protected void showDatePickerDialog() {
        DialogHelper.showDatePickerDialog(this, this, null);
    }


    private void initData(Pasien pasien) {
        pasien.setNama(getValueFrom(inputNamaLengkap));
        pasien.setTglLahir(getValueFrom(inputTglLahir));
        pasien.setNoTelp(getValueFrom(inputNoTelp));
        pasien.setNoKtp(getValueFrom(inputNoKtp));
        pasien.setAlamat(getValueFrom(inputAlamat));

        if (priaRb.isChecked()) pasien.setGender("Pria");
        else pasien.setGender("Wanita");

        pasien.setKota(kotaSpinner.getSelectedItem().toString());
        pasien.setKecamatan(kecamatanSpinner.getSelectedItem().toString());
        pasien.setKelurahan(kelurahanSpinner.getSelectedItem().toString());
        pasien.setStatus(0);
    }

    @OnClick(R.id.start_btn)
    void start() {


        if (!validateAllInput()) return;

        Log.d("SELF", "CLICKED");

        Pasien pasien = new Pasien();
        initData(pasien);
        PasienDbService service = new PasienDbService(this);

        try {
            service.simpan(pasien);
            startCheckUpActivity(pasien);
            Log.d("SELF", "SUCCEED MOVED");
        } catch (SQLiteException e) {
            Toast.makeText(this,"Gagal Menyimpan Data Pasien",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startCheckUpActivity(Pasien pasien) {
        Intent intent = new Intent(this, CheckUpActivity.class);

        intent.putExtra("pasienId", pasien.getId());
        startActivity(intent);
        finish();
    }

    private void initSpinners() {
        kotaSpinner.setOnItemSelectedListener(this);
        kecamatanSpinner.setOnItemSelectedListener(this);

        LocationDbService locationDbService = new LocationDbService(this);
        kotaList = locationDbService.getKota();
        locationDbService.close();

        kecamatanList = new ArrayList<>();
        kelurahanList = new ArrayList<>();

        kotaAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, kotaList);
        kecamatanAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, kecamatanList);
        kelurahanAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, kelurahanList);

        kotaSpinner.setAdapter(kotaAdapter);
        kecamatanSpinner.setAdapter(kecamatanAdapter);
        kelurahanSpinner.setAdapter(kelurahanAdapter);

    }

    @Override
    protected void initOnCreate() {
        ButterKnife.bind(this);
        initSpinners();

        inputTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(getClass().getName(), "onItemSelected");
        int viewId = adapterView.getId();
        LocationDbService locationDbService = new LocationDbService(this);

        if(viewId == R.id.sp_kota) {
            String kota = kotaList.get(i);
            List<String> kecamatans = locationDbService.getKecamatan(kota);

            Log.d(getClass().getName(), "Kota: " + kota);
            kecamatanList.clear();
            kecamatanList.addAll(kecamatans);
            kecamatanAdapter.notifyDataSetChanged();
        }
        else if(viewId == R.id.sp_kecamatan) {
            String kecamatan = kecamatanList.get(i);
            List<String> kelurahans = locationDbService.getKelurahan(kecamatan);

            Log.d(getClass().getName(), "Kecamatan: " + kecamatan);

            kelurahanList.clear();
            kelurahanList.addAll(kelurahans);
            kelurahanAdapter.notifyDataSetChanged();
        }

        locationDbService.close();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean isContainSpesialCharacter(String toCheck) {
        String forbidenChars = "!@#$%^&*()_+";

        for (char character : forbidenChars.toCharArray()) {
            String forbidenChar = String.valueOf(character);

            if (toCheck.contains(forbidenChar)) {
                return true;
            }
        }

        return false;
    }

    private boolean validateNama() {
        String nama = getValueFrom(inputNamaLengkap);
        if (nama.isEmpty()) {
            inputNamaLengkap.setError("Nama Lengkap Masih Kosong");
            return false;
        }

        if (isContainSpesialCharacter(nama)) {
            inputNamaLengkap.setError("Nama Tidak Valid");
            return false;
        }

        return true;
    }

    private boolean validateTglLahir() {
        String tglLahir = getValueFrom(inputTglLahir);
        if (tglLahir.isEmpty()) {
            inputTglLahir.setError("Tanggal Lahir Masih Kosong");
            return false;
        }

        return true;
    }

    private boolean validateAlamat() {
        String alamat = getValueFrom(inputAlamat);
        if (alamat.isEmpty()) {
            inputAlamat.setError("Alamat Masih Kosong");
            return false;
        }

        if (isContainSpesialCharacter(alamat)) {
            inputAlamat.setError("Alamat Tidak Valid");
            return false;
        }

        return true;
    }


    private boolean validateNoKtp() {
        String noKtp = getValueFrom(inputNoKtp);
        if (noKtp.isEmpty()) {
            inputNoKtp.setError("No. KTP Masih Kosong");
            return false;
        }

        return true;
    }

    private boolean validateNoTelp() {
        String noTelp = getValueFrom(inputNoTelp);
        if (noTelp.isEmpty()) {
            inputNoTelp.setError("No. Telepon Masih Kosong");
            return false;
        }

        return true;
    }

    @Override
    protected boolean validateAllInput() {
        return validateNama() &
                validateNoKtp() &
                validateNoTelp() &
                validateTglLahir() &
                validateAlamat();
    }

    @Override
    public void onDateSet(DatePicker datePicker,  int year, int month, int dayOfMonth) {
        String chosenDate = String.valueOf(dayOfMonth) +
                "/"+
                (month + 1) +
                "/" +
                year;

        inputTglLahir.setText(chosenDate);
    }
}
