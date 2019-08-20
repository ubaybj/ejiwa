package com.puskesmascilandak.e_jiwa.model;

public class DetailCheckUp extends AbstractEntity {
    private CheckUp checkUp;
    private Angket angket;
    private String answer;

    public CheckUp getCheckUp() {
        return checkUp;
    }

    public void setCheckUp(CheckUp checkUp) {
        this.checkUp = checkUp;
    }

    public Angket getAngket() {
        return angket;
    }

    public void setAngket(Angket angket) {
        this.angket = angket;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
