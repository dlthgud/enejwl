package com.example.enejwl.dlthgud;

class ItemInfo {
    String name;
    int touch;

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

    public ItemInfo(String name, int touch) {
        this.name = name;
        this.touch = touch;
    }
}
