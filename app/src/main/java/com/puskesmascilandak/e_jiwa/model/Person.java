package com.puskesmascilandak.e_jiwa.model;

public abstract class Person extends AbstractEntity {
        private String nama;
        private String tglLahir;
        private String alamat;
    private String noTelp;
    private String noKtp;
    private String gender;

    private int lastChecked;

    public int getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(int lastChecked) {
        this.lastChecked = lastChecked;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
