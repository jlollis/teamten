package com.teamten.typeset;

import com.teamten.typeset.element.Discretionary;
import com.teamten.typeset.element.Element;
import com.teamten.typeset.element.Glue;
import com.teamten.typeset.element.HBox;
import com.teamten.typeset.element.Page;
import com.teamten.typeset.element.Penalty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import static com.teamten.typeset.SpaceUnit.PT;

/**
 * Accumulates elements in a vertical list until the document is finished, at which point a list of
 * pages (type VBox) is generated.
 */
public class VerticalList extends ElementList {
    /**
     * The depth of the last box that was added.
     */
    private long mPreviousDepth = 0;
    /**
     * Whether we've seen a box before.
     */
    private boolean mSawHBox = false;
    private long mBaselineSkip = PT.toSp(11*1.2); // Default for 11pt font.
    /**
     * Map from the element index to a column change. The specified element is the first
     * with the new layout.
     */
    private final NavigableMap<Integer,ColumnLayout> mColumnChanges = new TreeMap<>();

    public VerticalList() {
        // Create a single column layout by default.
        changeColumnLayout(ColumnLayout.single());
    }

    @Override
    public void addElement(Element element) {
        // Add glue just before horizontal boxes so that the baselines are the right distance apart.
        if (element instanceof HBox) {
            // Don't do this on the first box.
            if (mSawHBox) {
                long skip = Math.max(0, mBaselineSkip - mPreviousDepth - element.getHeight());
                super.addElement(new Glue(skip, 0, 0, false));
            }

            mPreviousDepth = element.getDepth();
            mSawHBox = true;
        }

        super.addElement(element);
    }

    /**
     * Specify that there should be a new column layout after the last-inserted element.
     */
    public void changeColumnLayout(@NotNull ColumnLayout columnLayout) {
        mColumnChanges.put(getElements().size(), columnLayout);
    }

    /**
     * Specify the distance between baselines. This is normally scaled by the font size,
     * for example 120% of font size. Set this between paragraphs when the font size changes.
     */
    public void setBaselineSkip(long baselineSkip) {
        mBaselineSkip = baselineSkip;
    }

    @Override
    protected Page makeOutputBox(List<Element> elements, int counter, long shift) {
        return new Page(elements, counter, shift);
    }

    @Override
    protected long getElementSize(Element element) {
        return element.getHeight() + element.getDepth();
    }

    /**
     * Return the list of elements on this page, from beginBreakpoint (inclusive) to endBreakpoint
     * (exclusive). Combines elements of multiple columns into groups.
     */
    @Override
    protected List<Element> getElementSublist(Breakpoint beginBreakpoint, Breakpoint endBreakpoint) {
        List<Element> allElements = getElements();
        int beginIndex = beginBreakpoint.getStartIndex();
        int endIndex = endBreakpoint.getIndex();

        List<Element> elements = new ArrayList<>(Math.max(endIndex - beginIndex + 1, 10));

        for (int i = beginIndex; i < endIndex; i++) {
            Element element = allElements.get(i);

            elements.add(element);
        }

        return elements;
    }

    /**
     * Like {@link #ejectPage()}, but only if the document is not empty.
     */
    public void newPage() {
        if (!getElements().isEmpty()) {
            ejectPage();
        }
    }

    /**
     * Like {@link #newPage()}, but ensures that the next page is an odd page.
     */
    public void oddPage() {
        // Here we have two infinite glues separated by a neutral penalty. The second penalty is forced, but only
        // exists at the end of even pages. This guarantees that the next page will be odd. The second penalty
        // will either skip or include the first penalty, depending on what's best overall.

        // Note that our double-glue works for us because we don't have any other infinite vertical glue on
        // the page. If we were trying to center the text vertically with infinite glue on top, this would not work.

        if (!getElements().isEmpty()) {
            addElement(new Glue(0, PT.toSp(1), true, 0, false, false));
            addElement(new Penalty(0));
            addElement(new Glue(0, PT.toSp(1), true, 0, false, false));
            addElement(new Penalty(-Penalty.INFINITY, true));
        }
    }

    /**
     * Add infinite vertical glue and force a page break.
     */
    public void ejectPage() {
        // Add a final infinite glue at the bottom.
        addElement(new Glue(0, PT.toSp(1), true, 0, false, false));

        // And a forced page break.
        addElement(new Penalty(-Penalty.INFINITY));
    }
}
