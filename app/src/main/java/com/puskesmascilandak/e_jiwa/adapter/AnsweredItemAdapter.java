package com.puskesmascilandak.e_jiwa.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.model.Angket;
import com.puskesmascilandak.e_jiwa.model.DetailCheckUp;

public class AnsweredItemAdapter extends ArrayAdapter<DetailCheckUp> {
    public AnsweredItemAdapter(@NonNull Context context) {
        super(context, R.layout.answered_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.answered_item, null);
        }

        TextView numberTextView = view.findViewById(R.id.number_textview);
        TextView questionTextView = view.findViewById(R.id.question_textview);
        TextView answeredTextView = view.findViewById(R.id.answered_textview);

        DetailCheckUp detailCheckUp = getItem(position);
        if (detailCheckUp != null) {
            Angket angket = detailCheckUp.getAngket();
            numberTextView.setText(String.valueOf(angket.getId()));
            questionTextView.setText(angket.getQuestion());

            String answer = detailCheckUp.getAnswer();
            answeredTextView.setText(answer);
            int color;
            Resources resources = getContext().getResources();
            if (answer.equals("Ya")) color = resources.getColor(R.color.green);
            else color = resources.getColor(R.color.red);
            answeredTextView.setBackgroundColor(color);
        }
        return view;
    }
}
