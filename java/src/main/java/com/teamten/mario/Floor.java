// Copyright 2011 Lawrence Kesteloot

package com.teamten.mario;

import java.awt.Color;
import java.awt.Graphics;

/**
 * The floor that our character stands on.
 */
public class Floor {
    private static final Color COLOR = new Color(100, 60, 0);
    public static final int HEIGHT = 8;

    // Left coordinate, inclusive.
    private final int mLeft;
    // Width of floor.
    private final int mWidth;
    // Top coordinate, inclusive.
    private final int mTop;

    public Floor(int left, int width, int top) {
        mLeft = left;
        mWidth = width;
        mTop = top;
    }

    public int getLeft() {
        return mLeft;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getTop() {
        return mTop;
    }

    public void draw(Graphics g) {
        g.setColor(COLOR);
        g.fillRect(mLeft, mTop, mWidth, HEIGHT);
    }
}
