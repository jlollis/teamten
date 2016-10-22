package com.teamten.typeset;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

/**
 * Interface for a font in our system.
 */
public interface Font {
    /**
     * Get the kerning between the two code points. The result is in scaled points.
     *
     * @param fontSize the size of the font in points.
     */
    long getKerning(int leftChar, int rightChar, float fontSize);

    /**
     * Replaces all ligatures (supported by this font) in the string.
     */
    String transformLigatures(String text);

    /**
     * The width of a space for a 1pt font, in scaled points.
     */
    long getSpaceWidth();

    /**
     * Return the size of a code point in the specified font size.
     */
    Metrics getCharacterMetrics(int ch, float fontSize);

    /**
     * Draw the text.
     *
     * @param text the text to draw.
     * @param fontSize the size in points.
     * @param x the left-hand edge of the text in scaled points.
     * @param y the baseline of the text in scaled points.
     * @param contents the stream to write to.
     */
    void draw(String text, float fontSize, long x, long y, PDPageContentStream contents) throws IOException;

    /**
     * Get the size of the text in the specified font size. Does not include kerning.
     */
    default Metrics getStringMetrics(String text, float fontSize) {
        long width = 0;
        long height = 0;
        long depth = 0;

        // Go through every code point and place them side by side, no kerning.
        for (int i = 0; i < text.length(); ) {
            int ch = text.codePointAt(i);
            i += Character.charCount(ch);

            Metrics metrics = getCharacterMetrics(ch, fontSize);

            width += metrics.getWidth();
            height = Math.max(height, metrics.getHeight());
            depth = Math.max(depth, metrics.getDepth());
        }

        return new Metrics(width, height, depth);
    }

    /**
     * The metrics for a character or text, in scaled points.
     */
    class Metrics extends AbstractDimensions {
        public Metrics(long width, long height, long depth) {
            super(width, height, depth);
        }

        // Nothing else yet. TODO delete?
    }
}
