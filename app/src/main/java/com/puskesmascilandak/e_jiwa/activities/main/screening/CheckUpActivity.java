package com.puskesmascilandak.e_jiwa.activities.main.screening;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.Session;
import com.puskesmascilandak.e_jiwa.activities.Activity;
import com.puskesmascilandak.e_jiwa.business.DetermineScore;
import com.puskesmascilandak.e_jiwa.model.Angket;
import com.puskesmascilandak.e_jiwa.model.CheckUp;
import com.puskesmascilandak.e_jiwa.model.DetailCheckUp;
import com.puskesmascilandak.e_jiwa.model.Pasien;
import com.puskesmascilandak.e_jiwa.model.Petugas;
import com.puskesmascilandak.e_jiwa.model.User;
import com.puskesmascilandak.e_jiwa.responses.ApiEndPoint;
import com.puskesmascilandak.e_jiwa.responses.CheckoutResponse;
import com.puskesmascilandak.e_jiwa.service.AngketDbService;
import com.puskesmascilandak.e_jiwa.service.ApiClient;
import com.puskesmascilandak.e_jiwa.service.CheckUpDbService;
import com.puskesmascilandak.e_jiwa.service.DetailCheckUpDbService;
import com.puskesmascilandak.e_jiwa.service.PasienDbService;
import com.puskesmascilandak.e_jiwa.util.CalendarHelper;
import com.puskesmascilandak.e_jiwa.util.DialogHelper;
import com.puskesmascilandak.e_jiwa.util.PopupUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckUpActivity extends Activity {
    @BindView(R.id.number_textview)
    TextView numberQuestionTextView;
    @BindView(R.id.question_textview)
    TextView questionTextView;
    @BindView(R.id.nama_txt)
    TextView namaPasienTextView;
    @BindView(R.id.no_telp_txt)
    TextView noTelpTextView;
    @BindView(R.id.no_ktp_txt)
    TextView noKtpTextView;
    @BindView(R.id.alamat_txt)
    TextView alamatTextView;
    @BindView(R.id.score_check_up_txt)
    TextView resultTextView;
    @BindView(R.id.keterangan_check_up_txt)
    TextView keteranganTextView;
    @BindView(R.id.tv_color)
    TextView colorTextView;
    @BindView(R.id.tes_info)
    TextView testInfoTv;
    @BindView(R.id.logo_result)
    ImageView logoInfo;

    @BindView(R.id.yes_rb)
    RadioButton yesRb;
    @BindView(R.id.no_rb)
    RadioButton noRb;

    @BindView(R.id.prev_question_btn)
    Button prevBtn;
    @BindView(R.id.next_question_btn)
    Button nextBtn;
    @BindView(R.id.detail_pasien_container)
    CardView containerDetail;
    @BindView(R.id.curhat_btn)
    ImageButton curhatBtn;

    @BindView(R.id.save_btn) Button saveCheckUpBtn;
    @BindView(R.id.save_self_assesment_btn) Button saveSelfAssessBtn;
    @BindView(R.id.finish_self_assesment_btn) Button finishSelfAssesBtn;

    private static int lastAnswer = 0;
    private static List<DetailCheckUp> detailCheckUps;
    private CheckUp checkUp;
    private ImageButton imagebutton;


    public CheckUpActivity() {
        super(R.layout.activity_check_up);
    }


    @Override
    protected void initOnCreate() {
        ButterKnife.bind(this);

        initCheckUp();
        initDetailCheckUp();
        viewDetailPasien();

        imagebutton = findViewById(R.id.curhat_btn);

        ImageButton mIdButtonHome = findViewById(R.id.curhat_btn);
        mIdButtonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://api.whatsapp.com/send?phone=6281212956515&text=Silahkan isi form dibawah terlebih dahulu%0ANama      :%0AUmur     :%0AAlamat     :%0AMasalah Yang Ingin Dicurhatkan     :"));
                startActivity(browserIntent);
            }
        });

        prevBtn.setVisibility(View.GONE);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        yesRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DetailCheckUp detailCheckUp = detailCheckUps.get(lastAnswer);
                    detailCheckUp.setAnswer("Ya");
                }
            }
        });
        noRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    DetailCheckUp detailCheckUp = detailCheckUps.get(lastAnswer);
                    detailCheckUp.setAnswer("Tidak");
                }
            }
        });

        containerDetail.setVisibility(View.GONE);
        colorTextView.setVisibility(View.GONE);
        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastAnswer=0;
                saveCheckUp();
            }
        });

        viewQuestion();
    }

    private void initDetailCheckUp() {
        detailCheckUps = new ArrayList<>();
        AngketDbService service = new AngketDbService(this);
        List<Angket> angkets = service.getAll();

        for (Angket angket : angkets) {
            DetailCheckUp detailCheckUp = new DetailCheckUp();
            detailCheckUp.setCheckUp(checkUp);
            detailCheckUp.setAngket(angket);
            detailCheckUp.setAnswer("Tidak");
            detailCheckUps.add(detailCheckUp);
        }
    }

    private void nextQuestion() {
        int max = detailCheckUps.size();
        System.out.println(" Last Answer "+lastAnswer+"Max "+max);
        if (lastAnswer < max) {
            lastAnswer += 1;

            if (prevBtn.getVisibility() == View.GONE) {
                prevBtn.setVisibility(View.VISIBLE);
            }
        }

        if (lastAnswer == max) {

            lastAnswer-=1;
            viewQuestion();
            determineScore();
            System.out.println(" Aneh"+lastAnswer);
            nextBtn.setVisibility(View.GONE);
            containerDetail.setVisibility(View.VISIBLE);
            colorTextView.setVisibility(View.VISIBLE);

            DetermineScore determineScore = new DetermineScore(getApplicationContext());

            colorTextView.setBackgroundColor(determineScore.getColor(detailCheckUps));

            String color = convertColor(String.valueOf(determineScore.getColor(detailCheckUps)));
            Date wajibPerika = determineScore.getDate(color);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            String wajibPeriksaInString = dateFormat.format(wajibPerika);

            switch (color) {
                case "red":
                    String message1 = "Waspada Anda mempunyai kecendrungan gangguan ";
                    String message2 = " Segera temui petugas kesehatan untuk mendapatkan pemeriksaan dan tindak lanjut. Kami siap melayani Anda.";
                    String sickness = TextUtils.join(", ", determineScore.generateKeteranganRed(detailCheckUps));
                    testInfoTv.setText(String.format("%s%s%s", message1, sickness, message2));
                    logoInfo.setImageDrawable(getResources().getDrawable(R.drawable.merahok));
                    break;
                case "yellow":
                    String warn1 = "Waspada kesehatan jiwa Anda berada diborder line. " +
                            "Anda disarankan melakukan konsultasi masalah anda dengan orang terdekat yang anda percaya, " +
                            "pemuka agama maupun petugas kesehatan. Petugas kesehatan akan memberikan konseling untuk Anda.";
                    String warn2 = " Jangan lupa periksa kembali kesehatan mental Anda pada tanggal "+wajibPeriksaInString;
                    String fullmessage = warn1+warn2;
                    testInfoTv.setText(fullmessage);
                    logoInfo.setImageDrawable(getResources().getDrawable(R.drawable.kuningok));
                    curhatBtn.setVisibility(View.VISIBLE);
                    break;
                case "green":
                    String greenMessage = "Selamat jiwa Anda sehat! Mari jaga kesehatan jiwa Anda dengan " +
                            "hindari stres, olahraga teratur, makan dan minum yang cukup, serta jangan lupa periksa kesehatan jiwa "+wajibPeriksaInString;
                    testInfoTv.setText(greenMessage);
                    logoInfo.setImageDrawable(getResources().getDrawable(R.drawable.hijauok));
                    break;
            }

        } else {
            viewQuestion();
            determineScore();
            containerDetail.setVisibility(View.GONE);
        }
    }


    private String convertColor(String keyColor){

        if (keyColor.equals("-4587006")) {
            return "red";
        }else if (keyColor.equals("-13590730")){
            return "green";
        }else if (keyColor.equals("-533228")){
            return "yellow";
        }

        return "#fff";
    }


    private void prevQuestion() {
        if (lastAnswer > 0) {
            lastAnswer -= 1;

            if (nextBtn.getVisibility() == View.GONE) {
                nextBtn.setVisibility(View.VISIBLE);
            }
        }

        if (lastAnswer == 0) {
            prevBtn.setVisibility(View.GONE);
        }

        containerDetail.setVisibility(View.GONE);
        viewQuestion();
        determineScore();
    }

    private void determineScore() {

        DetermineScore determineScore = new DetermineScore(this);
        int score = determineScore.countTotalYesAnswer(detailCheckUps);
        String keterangan = determineScore.generateKeterangan(detailCheckUps);
        checkUp.setScore(score);
        checkUp.setKeterangan(keterangan);

        String color = convertColor(String.valueOf(determineScore.getColor(detailCheckUps)));
        checkUp.setWarna(color);

        resultTextView.setText(String.valueOf(score));
        keteranganTextView.setText(keterangan);
    }

    private void viewDetailPasien() {
        Pasien pasien = checkUp.getPasien();
        if (pasien != null) {
            namaPasienTextView.setText(pasien.getNama());
            noTelpTextView.setText(pasien.getNoTelp());
            noKtpTextView.setText(pasien.getNoKtp());
            alamatTextView.setText(pasien.getAlamat());
        }
    }

    private void saveCheckUp() {
        CheckUpDbService service = new CheckUpDbService(this);
        try {
            service.simpan(checkUp);
            System.out.println(" Keterangan Kotak"+checkUp.getKeterangan());

            simpanDetailCheckUp();
            finish();
        } catch (SQLiteException e) {
            e.printStackTrace();
            DialogHelper.showDialog(this, "Gagal", "Tidak dapat menyimpan data checkup");
        }

    }

    private void simpanDetailCheckUp() {
        DetailCheckUpDbService service = new DetailCheckUpDbService(this);
        for (DetailCheckUp detailCheckUp : detailCheckUps) {
            try {
                service.simpan(detailCheckUp);
            } catch (SQLiteException e) {
                e.printStackTrace();
                DialogHelper.showDialog(this, "Gagal", "Tidak dapat menyimpan detil checkup");
                break;
            }
        }
    }

    @OnClick(R.id.finish_self_assesment_btn)
    void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        lastAnswer = 0;
        finish();
    }

    private void upload(Pasien pasien) {
        initCheckUp();
        determineScore();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        String wajibPeriksa = dateFormat.format(new DetermineScore(this).getDate(checkUp.getWarna()));

        PopupUtil.showLoading(this, "Uploading assesment", "Please wait");
        Call<CheckoutResponse> call = ApiClient.getClient().create(ApiEndPoint.class).checkout(
                dateFormat.format(date),
                pasien.getNoKtp(),
                pasien.getNama(),
                pasien.getAlamat(),
                pasien.getTglLahir(),
                checkUp.getScore(),
                checkUp.getKeterangan(),
                "SA",
                checkUp.getWarna(),
                pasien.getKelurahan(),
                pasien.getKecamatan(),
                pasien.getKota(),
                pasien.getLastChecked(),
                "",
                wajibPeriksa,
                pasien.getNoTelp()
        );

        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                PopupUtil.dismissDialog();
                if (response != null) {
                    if (response.isSuccessful()) {
                        showDialog("Success", "Your assesment is uploaded");
                        saveCheckUpBtn.setVisibility(View.GONE);
                        saveSelfAssessBtn.setVisibility(View.GONE);
                        finishSelfAssesBtn.setVisibility(View.VISIBLE);
                    } else {
                        showDialog("Uppss...", "Failed to upload your assesment");
                    }
                } else {
                    showDialog("Upps...", "Our server is not responding");
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                PopupUtil.dismissDialog();
                t.printStackTrace();
                showDialog("Upsss...", "Something went wrong");
            }
        });
    }

    @OnClick(R.id.save_self_assesment_btn)
    void uploadSelfAssesment() {
        long id  = getIntent().getLongExtra("pasienId", 0);
        PasienDbService service = new PasienDbService(this);

        Log.d("CHECK_up", "CLICKED: "+id);

        Pasien pasien = service.findBy(id);
        if (pasien != null) {
            upload(pasien);

        } else {
            Log.d("CHECK_up", "PASIEN NOT FOUND: "+id);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (lastAnswer == 0) {
            prevBtn.setVisibility(View.GONE);
        }

        if (lastAnswer > 0) {
            nextBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
        }

        if (lastAnswer == detailCheckUps.size() ) {
            nextBtn.setVisibility(View.GONE);
        }
    }

    private void viewQuestion() {

        DetailCheckUp detailCheckUp = detailCheckUps.get(lastAnswer);

        if (detailCheckUp != null) {
            Angket angket = detailCheckUp.getAngket();
            numberQuestionTextView.setText(String.valueOf(angket.getId()));
            questionTextView.setText(angket.getQuestion());

            switch (detailCheckUp.getAnswer()) {
                case "Tidak" : noRb.setChecked(true);break;
                case "Ya" : yesRb.setChecked(true);break;
            }
        }
    }

    private void initCheckUp() {
        checkUp = new CheckUp();
        /*Serializable pasienSerialized = getIntent().getSerializableExtra("pasien");
        if (pasienSerialized != null) {
            Pasien pasien = (Pasien) pasienSerialized;
            checkUp.setPasien(pasien);
        }*/
        long pasienId = getIntent().getLongExtra("pasienId", 0);
        PasienDbService pasienDbService = new PasienDbService(this);
        Pasien pasien = pasienDbService.getPasien(pasienId);

        if(pasien != null) {
            checkUp.setPasien(pasien);
        }

        Session session = new Session(this);
        User user = session.getUser();
        if (user != null) {
            Petugas petugas = user.getPetugas();

            if(petugas != null) {
                checkUp.setPetugas(petugas);
            }

            saveSelfAssessBtn.setVisibility(View.GONE);
            finishSelfAssesBtn.setVisibility(View.GONE);
            saveCheckUpBtn.setVisibility(View.VISIBLE);

        } else {
            finishSelfAssesBtn.setVisibility(View.VISIBLE);
            saveSelfAssessBtn.setVisibility(View.VISIBLE);
            saveCheckUpBtn.setVisibility(View.GONE);
        }

        /*Serializable petugasSerialized = getIntent().getSerializableExtra("petugas");
        if (petugasSerialized != null) {
            Petugas petugas = (Petugas) petugasSerialized;
            checkUp.setPetugas(petugas);
        } else {
            DialogHelper.showDialog(this, "Gagal", "Data Petugas Tidak Terkirim");
            return;
        }
*/
        checkUp.setTglCheckUp(CalendarHelper.getDefaultDateInString());
    }
}