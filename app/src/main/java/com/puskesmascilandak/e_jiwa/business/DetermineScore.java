package com.puskesmascilandak.e_jiwa.business;

import android.content.Context;
import android.text.TextUtils;

import com.puskesmascilandak.e_jiwa.R;
import com.puskesmascilandak.e_jiwa.model.Angket;
import com.puskesmascilandak.e_jiwa.model.DetailCheckUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetermineScore {
    private Context context;
    private final List<Integer> forbiddenYesAnswered = new ArrayList<>();

    public DetermineScore(Context context) {
        this.context = context;
        for (int i= 21;i<30; i++) {
            forbiddenYesAnswered.add(i);
        }

        forbiddenYesAnswered.add(17);
    }

    private boolean isNeorosis(List<DetailCheckUp> detailCheckUps) {
        //minim answer yes 3
        int [] forbids = new int[] {9, 10, 11, 15, 18};
        int countForbid = 0;
        for (int i=0; i<forbids.length; i++) {
            DetailCheckUp detailCheckUp = detailCheckUps.get(forbids[i] -1);

            if (detailCheckUp.getAnswer().equals("Ya")) {
                countForbid += 1;
            }
        }


        boolean forbidsIsAnswered = countForbid >=3;

        int countNormalForbids = 0;
        for (int i=1; i<=20; i++) {
            DetailCheckUp detailCheckUp = detailCheckUps.get(i - 1);

            if (detailCheckUp.getAnswer().equals("Ya")) {
                countNormalForbids += 1;
            }
        }
        boolean normalForbidsAchieved = countNormalForbids >= 8;

        boolean directForbidsAchived = detailCheckUps.get(16).getAnswer().equals("Ya");

        return forbidsIsAnswered || normalForbidsAchieved || directForbidsAchived;
    }

    private  boolean isPsiKatifs(List<DetailCheckUp> detailCheckUps) {
        return detailCheckUps.get(20).getAnswer().equals("Ya");
    }

    private boolean isPsikotiks(List<DetailCheckUp> detailCheckUps) {
        int totalYesAnswer = 0;

        for (int i=22; i<=24; i++) {
            DetailCheckUp detailCheckUp = detailCheckUps.get(i - 1);
            if (detailCheckUp.getAnswer().equals("Ya")) {
                totalYesAnswer += 1;
            }
        }

        return totalYesAnswer >= 1;
    }

    private boolean isStressPascaTrauma(List<DetailCheckUp> detailCheckUps) {
        int totalYesAnswered = 0;

        for (int i=25; i<=29; i++) {
            DetailCheckUp detailCheckUp = detailCheckUps.get(i - 1);
            if (detailCheckUp.getAnswer().equals("Ya")) {
                totalYesAnswered += 1;
            }
        }

        return totalYesAnswered >= 1;
    }


    public List<String> generateKeteranganRed(List<DetailCheckUp> detailCheckUps) {
        List<String> sickness = new ArrayList<>();

        if (isNeorosis(detailCheckUps)) {
            sickness.add("Neurosis.");
        }

        if (isPsiKatifs(detailCheckUps)) {
            sickness.add("Zat Psikoatif.");
        }

        if (isPsikotiks(detailCheckUps)) {
            sickness.add("Psikotik.");
        }

        if (isStressPascaTrauma(detailCheckUps)) {
            sickness.add("Stress Pasca Trauma.");
        }

        return sickness;
    }

    private boolean isForbiddenYesBeingAnswered(List<DetailCheckUp> detailCheckUps) {
        for (int forbid : forbiddenYesAnswered) {
            DetailCheckUp detailCheckUp = detailCheckUps.get(forbid - 1);

            if (detailCheckUp != null) {
                if (detailCheckUp.getAnswer().equals("Ya")) {
                    return true;
                }
            }
        }
        return false;
    }

    public Date getDate(String color) {
        Calendar today = Calendar.getInstance();

        switch (color) {
            case "yellow":  today.add(Calendar.MONTH, 1);; break;
            case "green":  today.add(Calendar.YEAR, 1);; break;
        }

        return today.getTime();
    }

    public int getColor(List<DetailCheckUp> detailCheckUps) {

        if (isForbiddenYesBeingAnswered(detailCheckUps)) {
            return context.getResources().getColor(R.color.red);
        }

        if (countTotalHappynessBeingAnswered(detailCheckUps) >= 2) {
            return context.getResources().getColor(R.color.red);
        }

        int totalAnsweredYes = countTotalYesAnswer(detailCheckUps);
        if (totalAnsweredYes >= 8) {
            return context.getResources().getColor(R.color.red);
        }

        if (totalAnsweredYes >= 5) {
            return context.getResources().getColor(R.color.yellow);
        }

        return context.getResources().getColor(R.color.green);
    }

    public int countTotalYesAnswer(List<DetailCheckUp> detailCheckUps) {
        int total = 0;

        for (DetailCheckUp detailCheckUp: detailCheckUps) {
            if (detailCheckUp.getAnswer().equals("Ya")) {
                total += 1;
            }
        }

        return total;
    }

    public String generateKeterangan(List<DetailCheckUp> detailCheckUps) {
        List<String> descriptions = new ArrayList<>();

        int totalYesAnswered = countTotalYesAnswer(detailCheckUps);

        boolean isQuestion18Answered = detailCheckUps.get(16).getAnswer().equals("Ya");

        if (totalYesAnswered > 8 || countTotalHappynessBeingAnswered(detailCheckUps) >= 2 || isNeorisCheckOn1Until20(detailCheckUps) || isQuestion18Answered) descriptions.add("A");

        if (isQuestion21BeingYes(detailCheckUps)) descriptions.add("B");

        if (isQuestionBetween22Until24BeingYes(detailCheckUps)) descriptions.add("C");

        if (isQuestionBetween25Until29BeingYes(detailCheckUps)) descriptions.add("D");

        return TextUtils.join(", ", descriptions);
    }

    private int countTotalHappynessBeingAnswered(List<DetailCheckUp> detailCheckUps) {
        int total = 0;

        if (detailCheckUps.get(8).getAnswer().equals("Ya") || detailCheckUps.get(9).getAnswer().equals("Ya")) {
            total += 1;
        }

        if (detailCheckUps.get(10).getAnswer().equals("Ya") || detailCheckUps.get(14).getAnswer().equals("Ya")) {
            total += 1;
        }

        if (detailCheckUps.get(17).getAnswer().equals("Ya")) {
            total += 1;
        }

        return total;
    }

    private boolean isNeorisCheckOn1Until20(List<DetailCheckUp> detailCheckUps) {
        int totalYesAnswered = 0;

        for (int i=1; i<=20; i++) {
            DetailCheckUp detailCheckUp = detailCheckUps.get(i-1);
            if (detailCheckUp.getAnswer().equals("Ya")) {
                totalYesAnswered += 1;
            }
        }

        return totalYesAnswered == 8;
    }

    private boolean isQuestion21BeingYes(List<DetailCheckUp> detailCheckUps) {
        return detailCheckUps.get(20).getAnswer().equals("Ya");
    }



    private boolean isQuestionBetween22Until24BeingYes(List<DetailCheckUp> detailCheckUps) {
        for (int i=21; i<25; i++) {
            if (detailCheckUps.get(i).getAnswer().equals("Ya")) {
                return true;
            }
        }

        return false;
    }

    private boolean isQuestionBetween25Until29BeingYes(List<DetailCheckUp> detailCheckUps) {
        for (int i=24; i<29; i++) {
            if (detailCheckUps.get(i).getAnswer().equals("Ya")) {
                return true;
            }
        }
        return false;
    }
}