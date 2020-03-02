package com.example.penview;

import android.graphics.Path;


public class PathWrapper {
    private Path path;
    private int color;

    public PathWrapper(Path path, int color) {
        this.path = path;
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
