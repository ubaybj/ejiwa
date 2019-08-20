package com.puskesmascilandak.e_jiwa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.business.DetermineScore;
import com.puskesmascilandak.e_jiwa.model.CheckUp;
import com.puskesmascilandak.e_jiwa.model.DetailCheckUp;
import com.puskesmascilandak.e_jiwa.model.Pasien;
import com.puskesmascilandak.e_jiwa.service.DetailCheckUpDbService;

import java.util.List;

public class CheckUpItemAdapter extends ArrayAdapter<CheckUp> {
    public CheckUpItemAdapter(@NonNull Context context) {
        super(context, R.layout.check_up_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.check_up_item, null);
        }

        TextView namaTxt = view.findViewById(R.id.nama_txt);
        TextView scoreTxt = view.findViewById(R.id.score_txt);
        TextView noHpTxt = view.findViewById(R.id.no_hp_txt);
        TextView tglCheckUpTxt = view.findViewById(R.id.tgl_check_up_txt);
        TextView statusTextView = view.findViewById(R.id.tv_status);

        CheckUp checkUp = getItem(position);
        DetailCheckUpDbService service = new DetailCheckUpDbService(getContext());
        List<DetailCheckUp> detailCheckUps = service.findBy(checkUp);
        DetermineScore determineScore = new DetermineScore(getContext());
        scoreTxt.setBackgroundColor(determineScore.getColor(detailCheckUps));

        if (checkUp != null) {
            Pasien pasien = checkUp.getPasien();

            if(pasien != null) {
                namaTxt.setText(pasien.getNama());
                scoreTxt.setText(String.valueOf(checkUp.getScore()));
                noHpTxt.setText(pasien.getNoTelp());
                tglCheckUpTxt.setText(checkUp.getTglCheckUp());

                if (pasien.getStatus() == 0) {
                    statusTextView.setText("New");
                    statusTextView.setTextColor(Color.YELLOW);
                } else if (pasien.getStatus() == 1) {
                    statusTextView.setText("Uploaded");
                    statusTextView.setTextColor(Color.GREEN);
                }
            }
        }

        return view;
    }
}
