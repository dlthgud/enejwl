package com.example.enejwl.dlthgud;

public class Level {
    int[] map;  // 맵 정보 배열
    int width;  // 맵의 가로 크기
    int height; // 맵의 세로 크기
    int condition;  // 총 제한 시간 또는 놓칠 수 있는 최대 두더지 수
    int moleNum;    // 잡아야 하는 두더지 총 개수
//    Mole[] moleArr; // 레벨에서 사용할 두더지 객체 배열
//    Item[] itemArr; // 레벨에서 사용할 아이템 객체 배열
    int end;    // 종료 방식 (0: 시간 제한, 1: n마리 놓침)

    public Level(int[] map, int width, int height, int condition, int moleNum, int end) {
        this.map = map;
        this.width = width;
        this.height = height;
        this.condition = condition;
        this.moleNum = moleNum;
        this.end = end;
    }

    public int[] getMap() {
        return map;
    }

    public void setMap(int[] map) {
        this.map = map;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getMoleNum() {
        return moleNum;
    }

    public void setMoleNum(int moleNum) {
        this.moleNum = moleNum;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
