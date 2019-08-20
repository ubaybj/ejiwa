package com.puskesmascilandak.e_jiwa.model;

public class Location extends AbstractEntity {
    private long kelurahanId;
    private long kecamatanId;
    private long kotaId;
    private String kelurahan;
    private String kecamatan;
    private String kota;

    public long getKelurahanId() {
        return kelurahanId;
    }

    public void setKelurahanId(long kelurahanId) {
        this.kelurahanId = kelurahanId;
    }

    public long getKecamatanId() {
        return kecamatanId;
    }

    public void setKecamatanId(long kecamatanId) {
        this.kecamatanId = kecamatanId;
    }

    public long getKotaId() {
        return kotaId;
    }

    public void setKotaId(long kotaId) {
        this.kotaId = kotaId;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }
}
