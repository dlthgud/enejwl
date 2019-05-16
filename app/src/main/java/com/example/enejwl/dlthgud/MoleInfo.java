package com.example.enejwl.dlthgud;

public class MoleInfo extends ItemInfo {
    String name;    // 이름
    int touch;  // 터치 수
    int score;  // 점수

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public MoleInfo(String name, int touch, int score) {
        super(name, touch);
        this.score = score;
    }

//    public String getName() {
//        return name;
//    }

//    public void setName(String name) {
//        this.name = name;
//    }

//    public int getTouch() {
//        return touch;
//    }

//    public void setTouch(int touch) {
//        this.touch = touch;
//    }
}
