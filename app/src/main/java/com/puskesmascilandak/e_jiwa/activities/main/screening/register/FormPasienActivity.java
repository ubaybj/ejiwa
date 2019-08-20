package com.puskesmascilandak.e_jiwa.activities.main.screening.register;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.puskesmascilandak.e_jiwa.EJiwaPreference;
import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.Session;
import com.puskesmascilandak.e_jiwa.activities.main.screening.CheckUpActivity;
import com.puskesmascilandak.e_jiwa.model.Pasien;
import com.puskesmascilandak.e_jiwa.model.Petugas;
import com.puskesmascilandak.e_jiwa.model.User;
import com.puskesmascilandak.e_jiwa.service.LocationDbService;
import com.puskesmascilandak.e_jiwa.service.PasienDbService;

import java.util.ArrayList;
import java.util.List;

public class FormPasienActivity extends FormPersonActivity implements AdapterView.OnItemSelectedListener {

    //TextView periksaTv;
    int periksaKe;
    EJiwaPreference eJiwaPreference;

    private ArrayAdapter kotaAdapter;
    private ArrayAdapter kecamatanAdapter;
    private ArrayAdapter kelurahanAdapter;

    private Spinner kotaSpinner;
    private Spinner kecamatanSpinner;
    private Spinner kelurahanSpinner;
    private List<String> kotaList;
    private List<String> kecamatanList;
    private List<String> kelurahanList;

    public FormPasienActivity() {
        super(R.layout.activity_form_pasien);
    }

    @Override
    protected void initOnCreate() {
        super.initOnCreate();
        setTitle("Input Data Pasien");
        initUpNavigation();

        eJiwaPreference=new EJiwaPreference(this);
        inputPeriksa.setVisibility(View.VISIBLE);

        Button startBtn = findViewById(R.id.start_btn);
         //periksaTv = findViewById(R.id.tv_periksa);

//        Session session = new Session(this);
//        final User user = session.getUser();

        kotaSpinner = findViewById(R.id.sp_kota);
        kecamatanSpinner = findViewById(R.id.sp_kecamatan);
        kelurahanSpinner = findViewById(R.id.sp_kelurahan);


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


        /*
        inputNoKtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence nik_pasien, int i, int i1, int i2) {
                    lastChecked(user.getPetugas().getNama(),String.valueOf(nik_pasien));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //System.out.println("Value "+getValueFrom(inputNoKtp));
            }

        });*/

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(periksaKe);
            }
        });
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
            PasienDbService service = new PasienDbService(this);
            Pasien pasien = service.findBy(noKtp);

            /*
            if (pasien != null) {
                showDialog("Gagal", "Sudah Ada Data Pasien Dengan No. KTP : "+noKtp);
                return false;
            }*/

            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            showDialog("Gagal", "Tidak Dapat Mengakses Database");
            return false;
        }
    }

    private void start(int lastChecked) {


        if (!validateAllInput()) return;

        Pasien pasien = new Pasien();
        initData(pasien);
        pasien.setKota(kotaSpinner.getSelectedItem().toString());
        pasien.setKecamatan(kecamatanSpinner.getSelectedItem().toString());
        pasien.setKelurahan(kelurahanSpinner.getSelectedItem().toString());
        pasien.setLastChecked(Integer.parseInt(inputPeriksa.getText().toString()));
        pasien.setStatus(0);

        PasienDbService service = new PasienDbService(this);

        try {
            service.simpan(pasien);
            startCheckUpActivity(pasien);
        } catch (SQLiteException e) {
            Toast.makeText(this,"Gagal Menyimpan Data Pasien",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startCheckUpActivity(Pasien pasien) {
        Session session = new Session(this);
        User user = session.getUser();

        if (user != null) {
            Petugas petugas = user.getPetugas();
            Intent intent = new Intent(this, CheckUpActivity.class);

            intent.putExtra("pasienId", pasien.getId());
            intent.putExtra("petugas", petugas);
            startActivity(intent);
            finish();
        }
    }

//    public void lastChecked(String pemeriksa,String nik_pasien) {
//
//        System.out.println(" Periksa "+nik_pasien);
//        ApiEndPoint apiEndPoint = ApiClient.getClient().create(ApiEndPoint.class);
//        Call<LastCheckedResponse> call = apiEndPoint.getLastChecked(pemeriksa,nik_pasien);
//
//        call.enqueue(new Callback<LastCheckedResponse>() {
//            @Override
//            public void onResponse(Call<LastCheckedResponse> call, Response<LastCheckedResponse> response) {
//
//                final LastCheckedResponse lastCheckedResponse = response.body();
//                if (lastCheckedResponse.getStatus()) {
//                    Log.d("Response Data ", "Total Data" + response);
//                    if (lastCheckedResponse.getLastChecked().size()>0) {
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                periksaKe = Integer.parseInt(lastCheckedResponse.getLastChecked().get(0).getLastChecked()) + 1;
//                                //periksaTv.setText("Pemeriksaan Ke "+periksaKe);
//                                //eJiwaPreference.putLastChecked(EJiwaPreference.LAST_CHECKED,periksaKe);
//
//                            }
//                        });
//                    }else{
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                System.out.println(" Masuk Sini ");
//                                periksaKe = 1;
//                                //eJiwaPreference.putLastChecked(EJiwaPreference.LAST_CHECKED,periksaKe);
//                                //periksaTv.setText("Pemeriksaan Ke "+periksaKe);
//                            }
//                        });
//                    }
//
//                } else {
//                    Log.d("StoreListFragment", "Data Null");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LastCheckedResponse> call, Throwable t) {
//            }
//        });
//
//    }

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
}
