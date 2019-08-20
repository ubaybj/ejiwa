package com.puskesmascilandak.e_jiwa.responses;

import android.widget.RadioButton;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Dossy on 5/21/2018.
 */

public interface ApiEndPoint {

    @FormUrlEncoded
    @POST("checkout.php")
    Call<CheckoutResponse> checkout(@Field("uploadDate") String uploadDate,
                                    @Field("patientNik") String patientNik,
                                    @Field("patientName") String patientName,
                                    @Field("address") String address,
                                    @Field("birthDate") String birthDate,
                                    @Field("score") int score,
                                    @Field("information") String information,
                                    @Field("checker") String checker,
                                    @Field("color") String color,
                                    @Field("kelurahan") String kelurahan,
                                    @Field("kecamatan") String kecamatan,
                                    @Field("kota") String kota,
                                    @Field("lastChecked") int lastChecked,
                                    @Field("kecamatan_petugas") String kecamatanPetugas,
                                    @Field("tanggal_checkup_kembali") String wajibPeriksa,
                                    @Field("no_telpon") String noTelp);



    @GET("get_last_checked.php")
    Call<LastCheckedResponse> getLastChecked(@Query("pemeriksa") String pemeriksa,@Query("nik_pasien") String nik_pasien);
}