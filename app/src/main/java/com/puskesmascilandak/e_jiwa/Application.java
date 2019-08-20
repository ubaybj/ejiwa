package com.puskesmascilandak.e_jiwa;

import android.content.SharedPreferences;
import android.util.Log;

import com.puskesmascilandak.e_jiwa.service.LocationDbService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // cek apakah app pertama kali dijalankan
        SharedPreferences pref = getSharedPreferences("com.puskesmascilandak.e_jiwa", 0);
        boolean firstTime = pref.getBoolean("first_time", true);

        if(firstTime) {
            importFromCSV();

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("first_time", false);
            editor.commit();
        }
    }

    private void importFromCSV() {
        try {
            InputStreamReader is = new InputStreamReader(getAssets()
                    .open("master_wilayah_dki.csv"));

            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;

            LocationDbService locationDbService = new LocationDbService(this);

            while ((line = reader.readLine()) != null) {
                Log.d("Import excel: ", line);
                String[] locations = line.split(";");
                long kelurahanId = Long.parseLong(locations[0]);
                String kelurahan = locations[1];
                long kecamatanId = Long.parseLong(locations[2]);
                String kecamatan = locations[3];
                long kotaId = Long.parseLong(locations[4]);
                String kota = locations[5];

                // insert ke database
                locationDbService.insert(kelurahanId, kelurahan, kecamatanId, kecamatan,
                        kotaId, kota);
            }

            locationDbService.close();
        }
        catch (Exception e) {

        }
    }
}
