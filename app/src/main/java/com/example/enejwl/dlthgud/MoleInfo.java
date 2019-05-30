package com.example.enejwl.dlthgud;

public class MoleInfo extends ItemInfo {
    int score;  // 점수

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public MoleInfo(String name, int touch, int score, int index) {
        super(name, touch, index);
        this.score = score;
    }
}
