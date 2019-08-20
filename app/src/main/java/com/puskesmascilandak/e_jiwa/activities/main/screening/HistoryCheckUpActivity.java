package com.puskesmascilandak.e_jiwa.activities.main.screening;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.puskesmascilandak.e_jiwa.EJiwaPreference;
import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.activities.Activity;
import com.puskesmascilandak.e_jiwa.activities.main.screening.detail.DetailAnsweredActivity;
import com.puskesmascilandak.e_jiwa.adapter.CheckUpItemAdapter;
import com.puskesmascilandak.e_jiwa.business.DetermineScore;
import com.puskesmascilandak.e_jiwa.model.CheckUp;
import com.puskesmascilandak.e_jiwa.model.Pasien;
import com.puskesmascilandak.e_jiwa.model.Petugas;
import com.puskesmascilandak.e_jiwa.responses.ApiEndPoint;
import com.puskesmascilandak.e_jiwa.responses.CheckoutResponse;
import com.puskesmascilandak.e_jiwa.service.ApiClient;
import com.puskesmascilandak.e_jiwa.service.CheckUpDbService;
import com.puskesmascilandak.e_jiwa.service.DatabaseHelper;
import com.puskesmascilandak.e_jiwa.service.PasienDbService;
import com.puskesmascilandak.e_jiwa.util.CSVUtils;
import com.puskesmascilandak.e_jiwa.util.CSVWriter;
import com.puskesmascilandak.e_jiwa.util.PopupUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class HistoryCheckUpActivity extends Activity {
    private CheckUpItemAdapter adapter;
    EJiwaPreference eJiwaPreference;
//    private static final int PERMISSION_REQUEST_EXTERMAL_STORAGE = 0;
    List<CheckUp> checkUps;

    public HistoryCheckUpActivity() {
        super(R.layout.activity_history_check_up);
    }

    private void loadDataCheckUp() {
        CheckUpDbService service = new CheckUpDbService(this);
       checkUps = service.getAll();

        adapter.addAll(checkUps);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void initOnCreate() {
        eJiwaPreference=new EJiwaPreference(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ListView listView = findViewById(R.id.list_history);
        adapter = new CheckUpItemAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewDetailCheckup(position);
            }
        });

        PopupUtil.showLoading(this, "Memuat Data Check Up", "Mohon Tunggu...");
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                loadDataCheckUp();
                PopupUtil.dismissDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_history_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.export_csv) {
            PopupUtil.showLoading(this, "Proses", "Uploading Data To Server ....");
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            for(int i=0;i<checkUps.size();i++){
                System.out.println("Color Aja"+checkUps.get(i).getWarna());
                Petugas petugas=checkUps.get(i).getPetugas();
                Pasien pasien=checkUps.get(i).getPasien();

                if(pasien.getStatus() == 0) {
                    System.out.println(" KET : " + checkUps.get(i).getKeterangan());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String wajibPeriksa = simpleDateFormat.format(new DetermineScore(this).getDate(checkUps.get(i).getWarna()));

                    checkout(pasien.getId(), dateFormat.format(date), pasien.getNoKtp(), pasien.getNama(),
                            pasien.getAlamat(), pasien.getTglLahir(), checkUps.get(i).getScore(),
                            checkUps.get(i).getKeterangan(), petugas.getNama(), checkUps.get(i).getWarna(),
                            pasien.getKelurahan(), pasien.getKecamatan(), pasien.getKota(),
                            pasien.getLastChecked(), petugas.getKecamatan(), wajibPeriksa,
                            pasien.getNoTelp());
                }
            }

            PopupUtil.dismissDialog();
            Toast.makeText(this,"Berhasil Upload",Toast.LENGTH_SHORT).show();
            //exportToCsv();
           //requestExternalStoragePermission();
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearCheckUpHistory() {
        CheckUpDbService service = new CheckUpDbService(this);
        try {
            service.deleteAll();
            adapter.clear();
            adapter.notifyDataSetChanged();
        } catch (SQLiteException error) {
            error.printStackTrace();
        }
    }

    public void checkout(final long id, String uploadDate,
                           String patientNik,
                           String patientName,
                           String address,
                           String birthDate,
                           int score,
                           String information,
                           String checker,String color, String kelurahan, String kecamatan, String kota, int lastChecked, String kecamatanPetugas,
                         String tglWajibPeriksa,
                         String noTelp) {

        System.out.println(" Keterangan "+information);
        Log.d(getClass().getName(), "kelurahan: " + kelurahan);
        Log.d(getClass().getName(), "kecamatan: " + kecamatan);
        Log.d(getClass().getName(), "kota: " + kota);
        ApiEndPoint apiEndPoint = ApiClient.getClient().create(ApiEndPoint.class);
        Call<CheckoutResponse> call = apiEndPoint.checkout(
                uploadDate,
                 patientNik,
                 patientName,
                 address,
                 birthDate,
                 score,
                 information,
                 checker,
                 color,
                 kelurahan,
                 kecamatan,
                 kota,
                 lastChecked,
                kecamatanPetugas,
                tglWajibPeriksa,
                noTelp);

        System.out.println(" Cek :"+lastChecked);

        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {

                CheckoutResponse checkoutResponse = response.body();
                if (checkoutResponse != null) {
                    if (checkoutResponse.getStatus()) {
                        Log.d("Response Data ", "Total Data" + response);

                        PasienDbService pasienDbService = new PasienDbService(HistoryCheckUpActivity.this);
                        pasienDbService.setStatus(id, 1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearCheckUpHistory();
                            }
                        });

                    } else {
                        Log.d("StoreListFragment", "Data Null");
                    }
                } else {
                    Toast.makeText(HistoryCheckUpActivity.this, "No Response from server", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    private void viewDetailCheckup(int position) {
        CheckUp checkUp = adapter.getItem(position);
        if (checkUp != null) {
            Intent intent = new Intent(this, DetailAnsweredActivity.class);

            intent.putExtra("check_up", checkUp);
            startActivity(intent);
        }
    }

//    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
//
//        private final ProgressDialog dialog = new ProgressDialog(HistoryCheckUpActivity.this);
//        DatabaseHelper databaseHelper;
//        @Override
//        protected void onPreExecute() {
//            this.dialog.setMessage("Exporting database...");
//            this.dialog.show();
//            databaseHelper = new DatabaseHelper(HistoryCheckUpActivity.this);
//        }
//
//        protected Boolean doInBackground(final String... args) {
//
//            File exportDir = new File(Environment.getExternalStorageDirectory(), "/codesss/");
//            if (!exportDir.exists()) { exportDir.mkdirs(); }
//
//            File file = new File(exportDir, "riwayat.csv");
//            try {
//                file.createNewFile();
//                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//                SQLiteDatabase db = databaseHelper.getReadableDatabase();
//                Cursor curCSV = db.rawQuery("SELECT * FROM check_up",null);
//                csvWrite.writeNext(curCSV.getColumnNames());
//                while(curCSV.moveToNext()) {
//                    String arrStr[]=null;
//                    String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
//                    for(int i=0;i<curCSV.getColumnNames().length;i++)
//                    {
//                        mySecondStringArray[i] =curCSV.getString(i);
//                    }
//                    csvWrite.writeNext(mySecondStringArray);
//                }
//                csvWrite.close();
//                curCSV.close();
//                return true;
//            } catch (IOException e) {
//                return false;
//            }
//        }
//
//        protected void onPostExecute(final Boolean success) {
//            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
//            if (success) {
//                Toast.makeText(HistoryCheckUpActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
//                //ShareGif();
//            } else {
//                Toast.makeText(HistoryCheckUpActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    private void exportToCsv() {
//
//        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
//
//        /*
//        File exportDir = new File(Environment.getExternalStoragePublicDirectory
//                (Environment.DIRECTORY_DOWNLOADS), "");
//        if (!exportDir.exists())
//        {
//            exportDir.mkdirs();
//        }
//
//        File file = new File(exportDir, "riwayat_checkup.csv");
//        */
//
//        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//        String fileName = "RiwayatCheckUp.csv";
//        String filePath = baseDir + File.separator + fileName;
//        File f = new File(filePath );
//        CSVWriter writer;
//// File exist
//
//        try
//        {
//            if(f.exists() && !f.isDirectory()){
//                FileWriter mFileWriter = new FileWriter(filePath , true);
//                writer = new CSVWriter(mFileWriter);
//            }
//            else {
//                writer = new CSVWriter(new FileWriter(filePath));
//            }
//            //file.createNewFile();
//            //CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//
//            /*
//            SQLiteDatabase db = databaseHelper.getReadableDatabase();
//            Cursor curCSV = db.rawQuery("SELECT * FROM check_up",null);
//            writer.writeNext(curCSV.getColumnNames());
//            //curCSV.moveToFirst();
//            while(curCSV.moveToNext())
//            {
//                //Which column you want to exprort
//                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2), curCSV.getString(3),
//                        curCSV.getString(4), curCSV.getString(5)};
//                writer.writeNext(arrStr);
//            }*/
//            //String[] data = {"Ship Name","Scientist Name", "..."};
//            //writer.writeNext(data);
//            PopupUtil.showLoading(this, "Proses", "Uploading Data To Server ....");
//
//            for(int i=0;i<checkUps.size();i++){
//
//                Petugas petugas=checkUps.get(i).getPetugas();
//                Pasien pasien=checkUps.get(i).getPasien();
//                String[] data = {String.valueOf(checkUps.get(i).getId()),petugas.getNama(),
//                        pasien.getNama(),String.valueOf(checkUps.get(i).getScore()),
//                        String.valueOf(checkUps.get(i).getKeterangan()),String.valueOf(checkUps.get(i).getTglCheckUp())};
//                writer.writeNext(data);
//            }
//            PopupUtil.dismissDialog();
//            writer.close();
//            //curCSV.close();
//        }
//        catch(Exception sqlEx)
//        {
//            Log.e("HistoryCheckUpActivity", sqlEx.getMessage(), sqlEx);
//        }
//    }


//    private void csv(){
//
//        File exportDir = new File(Environment.getExternalStoragePublicDirectory
//                (Environment.DIRECTORY_DOWNLOADS), "coba.csv");
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter(exportDir);
//            CSVUtils.writeLine(writer, Arrays.asList("a", "b", "c", "d"));
//
//            //custom separator + quote
//            CSVUtils.writeLine(writer, Arrays.asList("aaa", "bb,b", "cc,c"), ',', '"');
//
//            //custom separator + quote
//            CSVUtils.writeLine(writer, Arrays.asList("aaa", "bbb", "cc,c"), '|', '\'');
//
//            //double-quotes
//            CSVUtils.writeLine(writer, Arrays.asList("aaa", "bbb", "cc\"c"));
//
//
//            writer.flush();
//            writer.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void requestExternalStoragePermission(){
//        if (ContextCompat.checkSelfPermission(HistoryCheckUpActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Log.d("Permission ","Request");
//
//            ActivityCompat.requestPermissions(HistoryCheckUpActivity.this,
//                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    PERMISSION_REQUEST_EXTERMAL_STORAGE);
//        }
//    }
//
//    private boolean cekPermission(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                Log.v("Permisson","Permission is granted");
//                //File write logic here
//
//                return true;
//            }else{
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//
//            }
//        }
//
//        return false;
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        // BEGIN_INCLUDE(onRequestPermissionsResult)
//        if (requestCode == PERMISSION_REQUEST_EXTERMAL_STORAGE) {
//            // Request for camera permission.
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission has been granted. Start camera preview Activity.
//                Log.d("Permission ","Granted");
//                //exportToCsv();
//
//            } else {
//                // Permission request was denied.
//                Log.d("Permission ","Not Request");
//            }
//        }
//        // END_INCLUDE(onRequestPermissionsResult)
//    }

}
