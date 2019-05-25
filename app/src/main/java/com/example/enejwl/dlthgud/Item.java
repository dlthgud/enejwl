package com.example.enejwl.dlthgud;

public class Item { // 아이템의 정보를 저장하는 클래스
    String name; // 아이템 이름
    int touch; // 아이템 클릭을 위한 터치 수
    int score; // 아이템 클릭 시 점수
    double upMax; // 아이템이 올라와 있는 최대 시간
    double upMin; // 아이템이 올라와 있는 최소 시간
    int image; // 화면에 나타날 이미지
    int period; //아이템 주기

    public Item(String name, int touch, int score, double upMax, double upMin, int image, int period) {
        this.name = name;
        this.touch = touch;
        this.score = score;
        this.upMax = upMax;
        this.upMin = upMin;
        this.image = image;
        this.period = period;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTouch(int touch) {
        this.touch = touch;
    }

    public int getTouch() {
        return touch;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setUpMax(double upMax) {
        this.upMax = upMax;
    }

    public double getUpMax() {
        return upMax;
    }

    public void setUpMin(double upMin) {
        this.upMin = upMin;
    }

    public double getUpMin() {
        return upMin;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }
}