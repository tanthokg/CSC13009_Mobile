package com.example.gallery;

public class Tool {
    private int iconID;
    private String name;

    public Tool(int iconID, String name) {
        this.iconID = iconID;
        this.name = name;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
