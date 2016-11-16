/*
 *
 *    Copyright 2016 Lawrence Kesteloot
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

// Copyright 2011 Lawrence Kesteloot

package com.teamten.mario;

import com.teamten.image.ImageUtils;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Path2D;

import java.util.Collections;
import java.util.List;

/**
 * Draws a World.
 */
public class WorldDrawer extends Canvas {
    private static final Color LETTERBOX_COLOR = new Color(20, 20, 20);
    private static final Color EXPLORED_COLOR = new Color(120, 120, 120);
    private static final boolean DRAW_EXPLORED = true;
    private volatile World mWorld;
    private double mScale = 1;
    private double mTx = 0;
    private double mTy = 0;
    private List<Point> mPath = null;
    private List<Point> mExplored = null;
    private Point mTarget = null;
    private double mComputationTime = 0;

    public WorldDrawer(World world) {
        mWorld = world;
        setBackground(LETTERBOX_COLOR);
    }

    public void setWorld(World world) {
        mWorld = world;
        repaint();
    }

    /**
     * Debug path to draw.
     */
    public void setPath(List<Point> path, double computationTime) {
        mPath = path;
        mComputationTime = computationTime;
        repaint();
    }

    /**
     * Set of points we explored.
     */
    public void setExplored(List<Point> explored) {
        mExplored = explored;
        repaint();
    }

    public void setTarget(Point target) {
        mTarget = target;
        repaint();
    }

    public Point reverseTransform(Point point) {
        int x = (int) ((point.x - mTx)/mScale);
        int y = (int) ((point.y - mTy)/mScale);

        return new Point(x, y);
    }

    @Override // Canvas
    public void paint(Graphics g) {
        // Grab the world here so we can use it throughout the draw routine in
        // case it gets changed.
        World world = mWorld;

        // Use fancier graphics.
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(ImageUtils.getHighQualityRenderingMap());

        // Get window dimensions.
        Dimension dimension = getSize();
        int width = dimension.width;
        int height = dimension.height;

        // Scale graphics to window size.
        if (width*Env.HEIGHT > Env.WIDTH*height) {
            // Window wider than world.
            mScale = (double) height/Env.HEIGHT;
            mTx = (width - Env.WIDTH*mScale)/2.0;
            mTy = 0;
        } else {
            // Window taller than world.
            mScale = (double) width/Env.WIDTH;
            mTx = 0;
            mTy = (height - Env.HEIGHT*mScale)/2.0;
        }
        g2.translate(mTx, mTy);
        g2.scale(mScale, mScale);

        // Draw the world.
        world.draw(g);

        // Draw points we explored.
        if (mExplored != null && DRAW_EXPLORED) {
            g2.setColor(EXPLORED_COLOR);
            for (Point point : mExplored) {
                g2.drawRect(point.x, point.y, 0, 0);
            }
        }

        // Draw debug path.
        if (mPath != null) {
            Path2D.Double debugShape = new Path2D.Double();
            boolean first = true;
            for (Point point : mPath) {
                if (first) {
                    debugShape.moveTo(point.x, point.y);
                    first = false;
                } else {
                    debugShape.lineTo(point.x, point.y);
                }
            }
            double red = Math.min(mComputationTime, 1.0);
            g2.setColor(new Color((float) red, 0.0f, 0.0f));
            g2.draw(debugShape);
        }

        // Draw target.
        if (mTarget != null) {
            g2.setColor(Color.WHITE);
            g2.drawArc(mTarget.x - 2, mTarget.y - 2, 4, 4, 0, 360);
        }
    }
}
