package com.example.enejwl.dlthgud;

public class Mole { // 두더지의 정보를 저장하는 클래스
    String name;    // 두더지 이름
    int touch;  // 두더지를 없애기 위한 터치 수
    int score;  // 점수
    int upMax;  // 두더지가 올라와 있는 최대 시간
    int upMin;  // 두더지가 올라와 있는 최소 시간
    int image;  // 화면에 나타날 이미지

    public void setName(String name) {
        this.name = name;
    }

    public void setTouch(int touch) {
        this.touch = touch;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUpMax(int upMax) {
        this.upMax = upMax;
    }

    public void setUpMin(int upMin) {
        this.upMin = upMin;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() { return name; }

    public int getTouch() {
        return touch;
    }

    public int getScore() {
        return score;
    }

    public int getUpMax() {
        return upMax;
    }

    public int getUpMin() {
        return upMin;
    }

    public int getImage() {
        return image;
    }

    public Mole(String name, int touch, int score, int upMax, int upMin, int image) {
        this.name = name;
        this.touch = touch;
        this.score = score;
        this.upMax = upMax;
        this.upMin = upMin;
        this.image = image;
    }
}
