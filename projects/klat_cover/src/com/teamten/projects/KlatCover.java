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

package com.teamten.projects;

import com.teamten.image.ImageUtils;
import com.teamten.image.Typeface;

import java.awt.*;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

/**
 * Make the cover for the André Klat book.
 */
public class KlatCover {
    // Sizes.
    // DPI from http://en.wikipedia.org/wiki/List_of_displays_by_pixel_density
    private static final int SCREEN_DPI = 128; // MacBook Air 13"
    private static final int PRINT_DPI = 300;
    // Important text/images must be 0.375" away from edge.
    // http://connect.lulu.com/t5/Cover-Formatting/What-dimensions-should-my-book-cover-images-have/ta-p/33279
    private static final double BLEED_IN = 0.125;
    private static final double PAGE_WIDTH_IN = 4.25; // Not including bleed, including margin.
    private static final double PAGE_HEIGHT_IN = 6.87;
    private static final double SPINE_WIDTH_IN = 0.489; // Get from Lulu, depends on page count
    private static final double WIDTH_IN = 2*PAGE_WIDTH_IN + SPINE_WIDTH_IN + 2*BLEED_IN;
    private static final double HEIGHT_IN = PAGE_HEIGHT_IN + 2*BLEED_IN;
    private static final double MARGIN_IN = 0.19;
    private static final double HLINE_HEIGHT_IN = 0.1;
    // Positions are relative to top of page, not including bleed. (They start where bleed ends.)
    private static final double MIDDLE_HLINE_POS_IN = 3.5;
    private static final double NAME1_HEIGHT_IN = 0.6;
    private static final double NAME2_HEIGHT_IN = 0.6;
    private static final double NUT_HEIGHT_IN = 0.55;
    private static final double SUBTITLE_HEIGHT_IN = 0.55;
    private static final double TEAM_TEN_HEIGHT_IN = 0.50;
    private static final double BACK_TITLE_HEIGHT_IN = 0.50;
    private static final double ABOVE_HEAD_MARGIN = 0.25;
    private static final double BACK_PICTURE_IN = 0.80;
    private static final double PICTURE_MARGIN_IN = 0.1;
    private static final double BACK_TITLE_BOTTOM_MARGIN_IN = 0.1;

    // Font sizes.
    private static final double NAME_FONT_SIZE_IN = 0.68;
    private static final double NUT_FONT_SIZE_IN = 0.45;
    private static final double SUBTITLE_FONT_SIZE_IN = 0.20;
    private static final double FRONT_TEAM_TEN_PRESS_FONT_SIZE_IN = 0.15;
    private static final double BACK_TEAM_TEN_FONT_SIZE_IN = 0.25;
    private static final double BACK_TITLE_FONT_SIZE_IN = 0.25;
    private static final double BACK_TEXT_FONT_SIZE_IN = 0.15;
    private static final double SPINE_TITLE_FONT_SIZE_IN = 0.30;
    private static final double SPINE_TEAM_TEN_PRESS_FONT_SIZE_IN = 0.10;

    // Colors.
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color HIGHLIGHT_COLOR = new Color(0.6f, 0.0f, 0.0f, 1.0f);
    private static final Color DEBUG_COLOR = Color.GRAY;
    private static final Color BLACK_TEXT_COLOR = Color.BLACK;
    private static final Color WHITE_TEXT_COLOR = Color.WHITE;

    // Flags.
    private static final boolean DRAW_MARGIN = false;
    private static final boolean DRAW_BLEED = false;

    private final String mFilename;
    private final int mDpi;

    public static void main(String[] args) throws IOException, FontFormatException {
        int count = 0;
        int dpi = SCREEN_DPI;

        if (count < args.length && args[count].equals("--print")) {
            dpi = PRINT_DPI;
            count++;
        }

        if (count >= args.length) {
            System.err.println("Usage: make_cover [--print] filename");
            System.exit(-1);
        }

        new KlatCover(args[count], dpi).run();
    }

    private KlatCover(String filename, int dpi) {
        mFilename = filename;
        mDpi = dpi;
    }

    private void run() throws IOException, FontFormatException {
        int width = toPixels(WIDTH_IN);
        int height = toPixels(HEIGHT_IN);

        BufferedImage image = ImageUtils.make(width, height, BACKGROUND_COLOR);
        Graphics2D g = ImageUtils.createGraphics(image);

        // ----------------------------------------------------------------------------------
        // Front page

        int pageLeft = toPixels(BLEED_IN + PAGE_WIDTH_IN + SPINE_WIDTH_IN);
        int pageTop = toPixels(BLEED_IN);

        // HLine on top. Extend into the bleed.
        g.setPaint(HIGHLIGHT_COLOR);
        int topLineHeight = toPixels(HLINE_HEIGHT_IN + BLEED_IN);
        g.fillRect(pageLeft + toPixels(MARGIN_IN), 0,
                toPixels(PAGE_WIDTH_IN - 2*MARGIN_IN), topLineHeight);

        // Picture.
        int y = toPixels(MIDDLE_HLINE_POS_IN + BLEED_IN);

        // HLine in middle.
        int itemHeight = toPixels(HLINE_HEIGHT_IN);
        g.setPaint(HIGHLIGHT_COLOR);
        g.fillRect(pageLeft + toPixels(MARGIN_IN), y,
                toPixels(PAGE_WIDTH_IN - 2*MARGIN_IN), itemHeight);
        y += itemHeight;

        // First name
        itemHeight = toPixels(NAME1_HEIGHT_IN);
        Font font = ImageUtils.getFont(Typeface.GARAMOND, false, false, false,
                toPixels(NAME_FONT_SIZE_IN));
        g.setColor(BLACK_TEXT_COLOR);
        g.setFont(font);
        TextLayout textLayout = new TextLayout("LAWRENCE", font, g.getFontRenderContext());
        int textWidth = (int) textLayout.getBounds().getWidth();
        int textHeight = (int) textLayout.getBounds().getHeight();
        if (true) {
            justifyText(g, font, BLACK_TEXT_COLOR, "LAWRENCE", pageLeft + toPixels(MARGIN_IN),
                    pageLeft + toPixels(PAGE_WIDTH_IN - MARGIN_IN),
                    y + itemHeight/2 + textHeight/2);
        } else {
            textLayout.draw(g, pageLeft + toPixels(PAGE_WIDTH_IN)/2 - textWidth/2,
                    y + itemHeight/2 + textHeight/2);
        }
        y += itemHeight;

        // Last name
        itemHeight = toPixels(NAME2_HEIGHT_IN);
        textLayout = new TextLayout("KESTELOOT", font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        if (true) {
            justifyText(g, font, BLACK_TEXT_COLOR, "KESTELOOT", pageLeft + toPixels(MARGIN_IN),
                    pageLeft + toPixels(PAGE_WIDTH_IN - MARGIN_IN),
                    y + itemHeight/2 + textHeight/2);
        } else {
            textLayout.draw(g, pageLeft + toPixels(PAGE_WIDTH_IN)/2 - textWidth/2,
                    y + itemHeight/2 + textHeight/2);
        }
        y += itemHeight;

        // Nutshell
        itemHeight = toPixels(NUT_HEIGHT_IN);
        g.setPaint(HIGHLIGHT_COLOR);
        g.fillRect(pageLeft + toPixels(MARGIN_IN), y,
                toPixels(PAGE_WIDTH_IN - 2*MARGIN_IN), itemHeight);
        font = ImageUtils.getFont(Typeface.GARAMOND, false, false, false,
                toPixels(NUT_FONT_SIZE_IN));
        g.setColor(WHITE_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("IN A NUTSHELL", font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, pageLeft + toPixels(PAGE_WIDTH_IN)/2 - textWidth/2,
                y + itemHeight/2 + textHeight/2);
        y += itemHeight;

        // Subtitle
        itemHeight = toPixels(SUBTITLE_HEIGHT_IN);
        font = ImageUtils.getFont(Typeface.GARAMOND, false, true, false,
                toPixels(SUBTITLE_FONT_SIZE_IN));
        g.setColor(BLACK_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("Collected stories and essays",
                font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, pageLeft + toPixels(PAGE_WIDTH_IN)/2 - textWidth/2,
                y + itemHeight/2 + textHeight/2);
        y += itemHeight;

        // Team Ten Press.
        font = ImageUtils.getFont(Typeface.HELVETICA, true, false, false,
                toPixels(FRONT_TEAM_TEN_PRESS_FONT_SIZE_IN));
        g.setColor(BLACK_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("TEAM TEN PRESS", font, g.getFontRenderContext());
        textLayout.draw(g, pageLeft + toPixels(MARGIN_IN),
                toPixels(BLEED_IN + PAGE_HEIGHT_IN - MARGIN_IN));

        // ----------------------------------------------------------------------------------
        // Back page

        pageLeft = toPixels(BLEED_IN);
        y = toPixels(BLEED_IN + MARGIN_IN);

        // Team Ten
        itemHeight = toPixels(TEAM_TEN_HEIGHT_IN);
        font = ImageUtils.getFont(Typeface.HELVETICA, true, false, false,
                toPixels(BACK_TEAM_TEN_FONT_SIZE_IN));
        g.setColor(BLACK_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("TEAM TEN PRESS", font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, pageLeft + toPixels(PAGE_WIDTH_IN)/2 - textWidth/2,
                y + itemHeight/2 + textHeight/2);
        y += itemHeight;

        // HLine
        itemHeight = toPixels(HLINE_HEIGHT_IN);
        g.setPaint(HIGHLIGHT_COLOR);
        g.fillRect(pageLeft + toPixels(MARGIN_IN), y,
                toPixels(PAGE_WIDTH_IN - 2*MARGIN_IN), itemHeight);
        y += itemHeight;

        // Title
        itemHeight = toPixels(BACK_TITLE_HEIGHT_IN);
        font = ImageUtils.getFont(Typeface.GARAMOND, false, false, false,
                toPixels(BACK_TITLE_FONT_SIZE_IN));
        g.setColor(BLACK_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("Lawrence Kesteloot in a Nutshell",
                font, g.getFontRenderContext());
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, pageLeft + toPixels(MARGIN_IN), y + itemHeight/2 + textHeight/2);
        y += itemHeight + toPixels(BACK_TITLE_BOTTOM_MARGIN_IN);

        // Text
        font = ImageUtils.getFont(Typeface.GARAMOND, false, false, false,
                toPixels(BACK_TEXT_FONT_SIZE_IN));
        Font italicFont = ImageUtils.getFont(Typeface.GARAMOND, false, true, false,
                toPixels(BACK_TEXT_FONT_SIZE_IN));
        String text = "Lawrence Kesteloot, an author and software developer, has been thinking and writing about technology, philosophy, science, and atheism for the past twenty years. Lawrence Kesteloot in a Nutshell is a collection of Lawrence\u2019s most thought-provoking pieces, including the acclaimed technical short story Coding Machines. For more of Lawrence\u2019s writings, see lawrence.teamten.com.";
        AttributedString attributedString = new AttributedString(text);
        attributedString.addAttribute(TextAttribute.FONT, font);
        attributedString.addAttribute(TextAttribute.FOREGROUND, BLACK_TEXT_COLOR);
        // Doesn't seem to make a difference:
        attributedString.addAttribute(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        for (String italicPhrase : new String[] { "Lawrence Kesteloot in a Nutshell",
                "Coding Machines", "lawrence.teamten.com" }) {

            int beginIndex = text.indexOf(italicPhrase);
            if (beginIndex == -1) {
                throw new IllegalStateException("Cannot find phrase \"" + italicPhrase + "\"");
            }
            int endIndex = beginIndex + italicPhrase.length();
            attributedString.addAttribute(TextAttribute.FONT, italicFont, beginIndex, endIndex);
        }
        AttributedCharacterIterator styledText = attributedString.getIterator();
        LineBreakMeasurer lbm = new LineBreakMeasurer(styledText, g.getFontRenderContext());
        int wrappingWidth = toPixels(PAGE_WIDTH_IN - 2*MARGIN_IN);
        while (lbm.getPosition() < styledText.getEndIndex()) {
            int lineWidth = wrappingWidth;
            int linePos = pageLeft + toPixels(MARGIN_IN);
            textLayout = lbm.nextLayout(lineWidth);

            y += textLayout.getAscent();
            textLayout.draw(g, linePos, y);
            y += textLayout.getDescent() + textLayout.getLeading();
        }

        // Visit Team Ten
        font = ImageUtils.getFont(Typeface.HELVETICA, true, false, false,
                toPixels(FRONT_TEAM_TEN_PRESS_FONT_SIZE_IN));
        g.setColor(BLACK_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("Visit Team Ten on the Web at",
                font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, pageLeft + toPixels(PAGE_WIDTH_IN)/2 - textWidth/2,
                toPixels(BLEED_IN + PAGE_HEIGHT_IN - MARGIN_IN) - textHeight*15/10);
        textLayout = new TextLayout("www.teamten.com", font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, pageLeft + toPixels(PAGE_WIDTH_IN)/2 - textWidth/2,
                toPixels(BLEED_IN + PAGE_HEIGHT_IN - MARGIN_IN));

        // ----------------------------------------------------------------------------------
        // Spine

        itemHeight = toPixels(SPINE_WIDTH_IN);

        int spineX1 = toPixels(BLEED_IN + PAGE_WIDTH_IN);
        g.setPaint(HIGHLIGHT_COLOR);
        g.fillRect(spineX1, 0, itemHeight, toPixels(HEIGHT_IN)); // Full bleed.

        // Rotate the whole thing so we're in the spine's transform. (0,0) is the upper-right
        // of the spine when the cover is seen normally.
        AffineTransform affineTransform = g.getTransform();
        g.translate(spineX1 + itemHeight, 0);
        g.rotate(Math.PI/2);

        // Black stripe. Extend into the bleed.
        int x = 0;
        int itemWidth = toPixels(BLEED_IN + HLINE_HEIGHT_IN);
        g.setPaint(Color.BLACK);
        g.fillRect(x, 0, itemWidth, itemHeight);
        x += itemWidth;

        // Title
        font = ImageUtils.getFont(Typeface.GARAMOND, false, false, false,
                toPixels(SPINE_TITLE_FONT_SIZE_IN));
        g.setColor(WHITE_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("Lawrence Kesteloot in a Nutshell",
                font, g.getFontRenderContext());
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, x + toPixels(MARGIN_IN), itemHeight/2 + textHeight/2);

        // Team Ten Press.
        font = ImageUtils.getFont(Typeface.HELVETICA, true, false, false,
                toPixels(SPINE_TEAM_TEN_PRESS_FONT_SIZE_IN));
        g.setFont(font);
        textLayout = new TextLayout("TEAM TEN", font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        itemWidth = toPixels(BLEED_IN + 2*MARGIN_IN) + textWidth;
        x = toPixels(HEIGHT_IN) - itemWidth;
        g.setColor(Color.BLACK);
        g.fillRect(x, 0, itemWidth, itemHeight);
        g.setColor(WHITE_TEXT_COLOR);
        int padding = toPixels(0.04);
        textLayout.draw(g, x + toPixels(MARGIN_IN), itemHeight/2 - padding);
        int teamTenWidth = textWidth;

        textLayout = new TextLayout("PRESS", font, g.getFontRenderContext());
        textWidth = (int) textLayout.getBounds().getWidth();
        textHeight = (int) textLayout.getBounds().getHeight();
        textLayout.draw(g, x + toPixels(MARGIN_IN) + (teamTenWidth - textWidth)/2,
                itemHeight/2 + textHeight + padding);

        // Rotate back.
        g.setTransform(affineTransform);

        // ----------------------------------------------------------------------------------
        // Debug

        if (DRAW_MARGIN) {
            g.setColor(DEBUG_COLOR);
            g.drawRect(toPixels(BLEED_IN + MARGIN_IN), toPixels(BLEED_IN + MARGIN_IN),
                    toPixels(PAGE_WIDTH_IN - 2*MARGIN_IN),
                    toPixels(PAGE_HEIGHT_IN - 2*MARGIN_IN));
            g.drawRect(toPixels(BLEED_IN + PAGE_WIDTH_IN + SPINE_WIDTH_IN + MARGIN_IN),
                    toPixels(BLEED_IN + MARGIN_IN),
                    toPixels(PAGE_WIDTH_IN - 2*MARGIN_IN),
                    toPixels(PAGE_HEIGHT_IN - 2*MARGIN_IN));
        }

        if (DRAW_BLEED) {
            g.setColor(DEBUG_COLOR);
            g.drawRect(toPixels(BLEED_IN), toPixels(BLEED_IN),
                    toPixels(WIDTH_IN - 2*BLEED_IN), toPixels(HEIGHT_IN - 2*BLEED_IN));
        }

        g.dispose();
        ImageUtils.save(image, mFilename);
    }

    /**
     * Draw the string at y so that the letters are evenly spread from x1 to x2.
     */
    private void justifyText(Graphics2D g, Font font, Color color,
                             String text, int x1, int x2, int y) {

        GlyphVector glyphVector = font.createGlyphVector(g.getFontRenderContext(), text);
        double textWidth = glyphVector.getLogicalBounds().getWidth();

        // Adjust kerning.
        double adjust = 0;
        if (text.length() != glyphVector.getNumGlyphs()) {
            throw new IllegalStateException("Glyph count is unexpected (" +
                    glyphVector.getNumGlyphs() + " vs. " + text.length() + ")");
        }
        Map<String,Double> kerning = new HashMap<String,Double>();
        kerning.put("LA", -0.00);
        kerning.put("AW", -0.10);
        kerning.put("NC", -0.10);
        kerning.put("CE", -0.05);
        for (int i = 1; i < glyphVector.getNumGlyphs(); i++) {
            String pair = text.substring(i - 1, i + 1);
            Double kern = kerning.get(pair);
            if (kern != null) {
                GlyphMetrics glyphMetrics = glyphVector.getGlyphMetrics(i);
                double delta = glyphMetrics.getAdvanceX()*kern;
                adjust += delta;
                textWidth += delta;
            }

            Point2D p = glyphVector.getGlyphPosition(i);
            p.setLocation(p.getX() + adjust, p.getY());
            glyphVector.setGlyphPosition(i, p);
        }

        // Spread out the letters.
        double padding = x2 - x1 - textWidth;
        double characterPadding = padding / (text.length() - 1);

        for (int i = 1; i < glyphVector.getNumGlyphs(); i++) {
            Point2D p = glyphVector.getGlyphPosition(i);
            p.setLocation(p.getX() + i*characterPadding, p.getY());
            glyphVector.setGlyphPosition(i, p);
        }

        g.setPaint(color);
        g.fill(glyphVector.getOutline(x1, y));
    }

    private int toPixels(double inches) {
        return (int) (inches*mDpi + 0.5);
    }
}
