package com.puskesmascilandak.e_jiwa;

import android.content.Context;

public class PersonRegistrationPreference extends EJiwaPreference {

    public PersonRegistrationPreference(Context context) {
        super(context);
    }

    public String getNama() {
        return getString("nama");
    }

    public void setNama(String nama) {
        putString("nama", nama);
    }

    public String getTglLahir() {
        return getString("tgl_lahir");
    }

    public void setTglLahir(String tglLahir) {
        putString("tgl_lahir", tglLahir);
    }

    public String getAlamat() {
        return getString("gender");
    }

    public void setAlamat(String alamat) {
        putString("alamat", alamat);
    }

    public String getNoTelp() {
        return getString("no_telp");
    }

    public void setNoTelp(String noTelp) {
        putString("no_telp", noTelp);
    }

    public String getNoKtp() {
        return getString("no_ktp");
    }

    public void setNoKtp(String noKtp) {
        putString("no_ktp", noKtp);
    }

    public String getGender() {
        return getString("gender");
    }

    public void setGender(String gender) {
        putString("gender", gender);
    }
}
