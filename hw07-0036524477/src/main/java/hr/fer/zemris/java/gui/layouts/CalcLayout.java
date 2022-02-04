package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that defines the layout for a calculator
 *
 * @author Marko Tunjić
 */
public class CalcLayout implements LayoutManager2 {
    /** A constant that defines the number of rows in the layout */
    public static final int MAX_ROW_COUNT = 5;

    /** A constant that defines the minimal index of the row and column */
    public static final int MIN_ROW_AND_COLUMN_COUNT = 1;

    /** A constant that defines the number of columns in the layout */
    public static final int MAX_COLUMN_COUNT = 7;

    /** A constant that defines the colspan of the first element */
    public static final int FIRST_ELEMENT_COLSPAN = 5;

    /** An private attribute that contains all the component and their positions */
    private Map<Component, RCPosition> positions;

    /** An private attribute that defines the gap between the rows and columns */
    private int gapWidth;

    /** A defoult constructor that sets the gap width to 0 */
    public CalcLayout() {
        this(0);
    }

    /**
     * A constructor that sets the gap width to the given value. Throws
     * {@link IllegalArgumentException} if less than zero
     *
     * @param gapWidth defines the gap between the rows and columns
     */
    public CalcLayout(int gapWidth) {
        if (gapWidth < 0)
            throw new IllegalArgumentException("Gap wisth can not be less than 0");
        this.gapWidth = gapWidth;
        this.positions = new HashMap<>();
    }

    /**
     * Not supported method. Throws {@link UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException(
                "Method void addLayoutComponent(String name, Component comp) fortype CalcLayout is not supported");
    }

    /**
     * A method that removes the given component from the laout if existent
     *
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        positions.remove(comp);
    }

    /**
     * A method that return the prefered size for this layout based on children
     *
     * @return the prefered layout size
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calcDimension(parent, Component::getPreferredSize);
    }

    /**
     * A method that return the minium size for this layout based on children
     *
     * @return the minium layout size
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calcDimension(parent, Component::getMinimumSize);
    }

    /**
     * A method that draws this layout based on the parents constraints
     *
     * @param parent the container in which the components will be drawn
     */
    @Override
    public void layoutContainer(Container parent) {
        // get parents insets width and height
        Insets insets = parent.getInsets();
        int width = parent.getWidth();
        int height = parent.getHeight();

        // calculate the available width without insets
        int availableWidth = Math.max(0, width - gapWidth * (MAX_COLUMN_COUNT - 1) - insets.left - insets.right);
        int availableHeight = Math.max(0, height - gapWidth * (MAX_ROW_COUNT - 1) - insets.top - insets.bottom);

        // the components often wont be the same length. So this is the leftover
        int leftoverWidth = availableWidth % MAX_COLUMN_COUNT;
        int leftoverHeight = availableHeight % MAX_ROW_COUNT;

        // width and height without leftovers
        int childWidth = availableWidth / MAX_COLUMN_COUNT;
        int childHeight = availableHeight / MAX_ROW_COUNT;

        // an array that defines if a child will have bigger width
        boolean[] isBiggerWidth = new boolean[MAX_COLUMN_COUNT];

        // first add one pixel to every other child
        for (int i = 0; i < isBiggerWidth.length; i += 2) {
            if (leftoverWidth < 0)
                break;
            isBiggerWidth[i] = true;
            leftoverWidth--;
        }

        // then add a pixel to the lefover children
        for (int i = 1; i < isBiggerWidth.length; i += 2) {
            if (leftoverWidth < 0)
                break;
            isBiggerWidth[i] = true;
            leftoverWidth--;
        }
        // an array that defines if a child will have bigger height
        boolean[] isBiggerHeight = new boolean[MAX_ROW_COUNT];

        // first add one pixel to every other child
        for (int i = 0; i < isBiggerHeight.length; i += 2) {
            if (leftoverHeight < 0)
                break;
            isBiggerHeight[i] = true;
            leftoverHeight--;
        }

        // then add a pixel to the lefover children
        for (int i = 1; i < isBiggerHeight.length; i += 2) {
            if (leftoverHeight < 0)
                break;
            isBiggerHeight[i] = true;
            leftoverHeight--;
        }

        // determine the position width and height of each child
        for (Component child : parent.getComponents()) {
            // get the RCPosition from the map
            RCPosition position = positions.get(child);

            // calculate the position in pixels based on the RCPosition and children width
            // and height
            int x = (position.getColumn() - 1) * (childWidth + gapWidth) + insets.left;
            int y = (position.getRow() - 1) * (childHeight + gapWidth) + insets.top;

            // add the remaining height pixels
            int biggerWidths = 0;
            int biggerHeights = 0;
            for (int i = 0; i < position.getRow() - 1; i++)
                if (isBiggerHeight[i])
                    biggerHeights++;
            y += biggerHeights;

            // check if first element in layout if yes than set the colspan
            if (position.getRow() == 1 && position.getColumn() == 1) {
                for (int i = 0; i < FIRST_ELEMENT_COLSPAN; i++)
                    if (isBiggerWidth[i])
                        biggerWidths++;
                child.setBounds(x, y,
                        childWidth * FIRST_ELEMENT_COLSPAN + biggerWidths + gapWidth * (FIRST_ELEMENT_COLSPAN - 1),
                        isBiggerHeight[position.getRow() - 1] ? childHeight + 1 : childHeight);
                continue;
            }

            // if not the first element get the remenaing width pixels
            for (int i = 0; i < position.getColumn() - 1; i++)
                if (isBiggerWidth[i])
                    biggerWidths++;
            x += biggerWidths;

            // set the bounds of the child
            child.setBounds(x, y, isBiggerWidth[position.getColumn() - 1] ? childWidth + 1 : childWidth,
                    isBiggerHeight[position.getRow() - 1] ? childHeight + 1 : childHeight);
        }
    }

    /**
     * A method that adds a component to this layout. Throws
     * {@link NullPointerException} if null was givenm throws
     * {@link IllegalArgumentException}if not supported constraint type was given,
     * throws {@link CalcLayoutException} if a component with the given contraint
     * already exists. Throws
     * {@link CalcLayoutException}: if a row is greater than max row count, if a
     * column is greater than max column count, if row or column less than 1, if row
     * is 1 and column greater than and less than 6
     *
     * @param comp the component to be added
     * @param the  contraints of the given component
     *
     * @throws NullPointerException     if null was givenm
     * @throws IllegalArgumentException if not supported constraint type was given
     * @throws CalcLayoutExceptionif    a component with the given contraint
     *                                  already exists
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        // check if null
        if (comp == null || constraints == null)
            throw new NullPointerException("Component and contraint can not be null!");

        // cast the given object contraint to our constraint
        RCPosition position;
        if (constraints instanceof String)
            position = RCPosition.parse((String) constraints);
        else if (constraints instanceof RCPosition)
            position = (RCPosition) constraints;
        else
            throw new IllegalArgumentException("Not supported constraint");

        // check if already axists
        if (positions.containsValue(position))
            throw new CalcLayoutException("A component already has the given constraint");

        // check if valid position
        checkRCPosition(position);
        positions.put(comp, position);
    }

    /**
     * A method that return the maximum size for this layout based on children
     *
     * @return the maximum layout size
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return calcDimension(target, Component::getMaximumSize);
    }

    /**
     * A method that returns the prefered alignment of this container. he value
     * should be a number between 0 and 1 where 0 represents alignment along the
     * origin, 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param the targeted container if more of them share the same layout
     *
     * @return he value should be a number between 0 and 1 where 0 represents
     *         alignment along the origin, 1 is aligned the furthest away from the
     *         origin, 0.5 is centered, etc.
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return (float) 0.5;
    }

    /**
     * A method that returns the prefered alignment of this container. he value
     * should be a number between 0 and 1 where 0 represents alignment along the
     * origin, 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param the targeted container if more of them share the same layout
     *
     * @return he value should be a number between 0 and 1 where 0 represents
     *         alignment along the origin, 1 is aligned the furthest away from the
     *         origin, 0.5 is centered, etc.
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return (float) 0.5;
    }

    /**
     * A method that makes this layout invalid
     *
     * @param target the targeted container if more containers share the same layout
     */
    @Override
    public void invalidateLayout(Container target) {
    }

    /**
     * A strategy interface for reduced redundancy and it is used for getting
     * prefered/maximal/minimal size of a child.
     */
    private interface SizeGetter {
        Dimension getSize(Component comp);
    }

    /**
     * A method that returns the requested dimensions of a child from the given
     * parent
     *
     * @param parent     the parent from whose children the value should be get
     * @param sizeGetter the getter to get the minimal/maximal/prefered dimension of
     *                   the children
     */
    private Dimension getMaxChildDimension(Container parent, SizeGetter sizeGetter) {
        Dimension maxDimension = new Dimension(0, 0);
        for (Component c : parent.getComponents()) {
            Dimension cdim = sizeGetter.getSize(c);
            if (cdim != null) {
                maxDimension.width = Math.max(maxDimension.width, cdim.width);
                maxDimension.height = Math.max(maxDimension.height, cdim.height);
            }
        }
        return maxDimension;
    }

    /**
     * A method that calclates the requested dimensions of the container based on
     * children.
     *
     * @param parent     the parent from whose children the value should be get
     * @param sizeGetter the getter to get the minimal/maximal/prefered dimension of
     *                   the children
     */
    private Dimension calcDimension(Container parent, SizeGetter sizeGetter) {
        Dimension maxDimension = getMaxChildDimension(parent, sizeGetter);
        Dimension dim = new Dimension(0, 0);
        dim.width += maxDimension.width * MAX_COLUMN_COUNT + gapWidth * (MAX_COLUMN_COUNT - 1);
        dim.height += maxDimension.height * MAX_ROW_COUNT + gapWidth * (MAX_ROW_COUNT - 1);

        Insets insets = parent.getInsets();
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;

        return dim;
    }

    /**
     * A method used for checking if a {@link RCPosition} is valid. Throws
     * {@link CalcLayoutException}: if a row is greater than max row count, if a
     * column is greater than max column count, if row or column less than 1, if row
     * is 1 and column greater than and less than 6
     *
     * @param position the position to be checked.
     */
    private void checkRCPosition(RCPosition position) {
        if (position.getRow() > CalcLayout.MAX_ROW_COUNT || position.getRow() < CalcLayout.MIN_ROW_AND_COLUMN_COUNT)
            throw new CalcLayoutException(
                    String.format("Can not create a position with row greater that %d or lower than %d.",
                            CalcLayout.MAX_ROW_COUNT,
                            CalcLayout.MIN_ROW_AND_COLUMN_COUNT));
        if (position.getColumn() > CalcLayout.MAX_COLUMN_COUNT
                || position.getColumn() < CalcLayout.MIN_ROW_AND_COLUMN_COUNT)
            throw new CalcLayoutException(
                    String.format("Can not create a position with row greater that %d or lower than %d.",
                            CalcLayout.MAX_COLUMN_COUNT,
                            CalcLayout.MIN_ROW_AND_COLUMN_COUNT));
        if (position.getRow() == 1 && (position.getColumn() > 1 && position.getColumn() < 6))
            throw new CalcLayoutException(
                    String.format("Can not create a position with row 1 and column less than %d",
                            CalcLayout.FIRST_ELEMENT_COLSPAN + 1));
    }
}
