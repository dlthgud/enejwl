package com.example.enejwl.dlthgud;

class ItemInfo {
    String name;
    int touch;
    int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTouch() {
        return touch;
    }

    public void setTouch(int touch) {
        this.touch = touch;
    }

    public ItemInfo(String name, int touch, int index) {
        this.name = name;
        this.touch = touch;
        this.index = index;
    }
}
