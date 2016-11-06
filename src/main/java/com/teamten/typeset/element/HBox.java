package com.teamten.typeset.element;

import com.teamten.font.FontSize;
import com.teamten.typeset.Dimensions;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Horizontal sequence of elements.
 */
public class HBox extends Box {
    private final List<Element> mElements;

    protected HBox(List<Element> elements, Dimensions dimensions, long shift) {
        super(dimensions, shift);
        mElements = elements;
    }

    public HBox(List<Element> elements, long shift) {
        this(elements, Dimensions.horizontally(elements), shift);
    }

    public HBox(List<Element> elements) {
        this(elements, 0);
    }

    /**
     * Do not modify this list.
     */
    public List<Element> getElements() {
        return mElements;
    }

    /**
     * Whether this horizontal box is empty, i.e., has no elements in it.
     */
    public boolean isEmpty() {
        return mElements.isEmpty();
    }

    /**
     * If the HBox contains only one element, and this element is a Text, then returns the text
     * of this Text. If the HBox is empty, returns an empty string.
     *
     * @throws IllegalArgumentException if one of the above conditions do not hold.
     */
    public String getOnlyString() {
        if (mElements.isEmpty()) {
            return "";
        }

        if (mElements.size() != 1) {
            throw new IllegalArgumentException("HBox must contain exactly one element");
        }

        Element element = mElements.get(0);
        if (!(element instanceof Text)) {
            throw new IllegalArgumentException("element must be Text");
        }

        return ((Text) element).getText();
    }

    /**
     * Make an HBox that contains only a Text with the given string, font, and font size.
     */
    public static HBox makeOnlyString(String text, FontSize font) {
        return new HBox(Arrays.asList(new Text(text, font)));
    }

    @Override
    public long layOutHorizontally(long x, long y, PDPageContentStream contents) throws IOException {
        /// drawDebugRectangle(contents, x, y);

        for (Element element : mElements) {
            long advanceX = element.layOutHorizontally(x, y, contents);
            x += advanceX;
        }

        return getWidth();
    }

    @Override
    public long layOutVertically(long x, long y, PDPageContentStream contents) throws IOException {
        // Skip down our height so that "y" points to our baseline.
        y -= getHeight();

        // Lay out the elements horizontally.
        layOutHorizontally(x + getShift(), y, contents);

        // Our height is the combined height and depth.
        return getVerticalSize();
    }

    @Override
    public void visit(Consumer<Element> consumer) {
        super.visit(consumer);
        mElements.forEach((element) -> {
            element.visit(consumer);
        });
    }

    @Override
    public void println(PrintStream stream, String indent) {
        stream.println(indent + "HBox " + getDimensionString() + ":");
        Element.println(mElements, stream, indent + "    ");
    }

    @Override
    public String toTextString() {
        return Element.toTextString(mElements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HBox hBox = (HBox) o;

        return mElements.equals(hBox.mElements);

    }

    @Override
    public int hashCode() {
        return mElements.hashCode();
    }
}
