package com.kalynx.robotdeveloper.ui.customcomponents;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LineResult extends JPanel {
    public final static float LEFT = 0.0f;
    public final static float CENTER = 0.5f;
    public final static float RIGHT = 1.0f;
    private Color currentLineForeground;
    private float digitAlignment;
    private final static Border OUTER = new MatteBorder(0, 0, 0, 2, Color.GRAY);
    private final static int HEIGHT = Integer.MAX_VALUE - 1000000;
    private int borderGap;
    private int lastDigits;
    private HashMap<String, FontMetrics> fonts;
    private Map<Integer, Character> lineVals = Collections.synchronizedMap(new HashMap<>());
    private JTextComponent component;

    public LineResult(JTextComponent component) {
        this.component = component;

        setFont( component.getFont() );
        setBorderGap( 5 );
        setCurrentLineForeground( Color.BLUE );
        setDigitAlignment( RIGHT );
    }

    public void clearResults() {
        synchronized(lineVals) {
            lineVals.clear();
        }
        repaint();
    }
    public void updateLine(int line, char val) {
        synchronized(lineVals) {
            lineVals.put(line, val);
        }
        repaint();
    }

    public void setBorderGap(int borderGap)
    {
        this.borderGap = borderGap;
        Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
        setBorder( new CompoundBorder(OUTER, inner) );
        lastDigits = 0;
        setPreferredWidth();
    }

    public void setCurrentLineForeground(Color currentLineForeground)
    {
        this.currentLineForeground = currentLineForeground;
    }

    private void setPreferredWidth()
    {
        Element root = component.getDocument().getDefaultRootElement();
        int lines = root.getElementCount();
        int digits = Math.max(String.valueOf(lines).length(), 1);

        //  Update sizes when number of digits in the line number changes

        if (lastDigits != digits)
        {
            lastDigits = digits;
            FontMetrics fontMetrics = getFontMetrics( getFont() );
            int width = fontMetrics.charWidth( '0' ) * digits;
            Insets insets = getInsets();
            int preferredWidth = insets.left + insets.right + width;

            Dimension d = getPreferredSize();
            d.setSize(preferredWidth, HEIGHT);
            setPreferredSize( d );
            setSize( d );
        }
    }

    public void setDigitAlignment(float digitAlignment)
    {
        this.digitAlignment =
                digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
    }

    private int getOffsetX(int availableWidth, int stringWidth)
    {
        return (int)((availableWidth - stringWidth) * digitAlignment);
    }

    private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics)
            throws BadLocationException
    {
        //  Get the bounding rectangle of the row

        Rectangle r = component.modelToView( rowStartOffset );
        int lineHeight = fontMetrics.getHeight();
        int y = r.y + r.height;
        int descent = 0;

        //  The text needs to be positioned above the bottom of the bounding
        //  rectangle based on the descent of the font(s) contained on the row.

        if (r.height == lineHeight)  // default font is being used
        {
            descent = fontMetrics.getDescent();
        }
        else  // We need to check all the attributes for font changes
        {
            if (fonts == null)
                fonts = new HashMap<>();

            Element root = component.getDocument().getDefaultRootElement();
            int index = root.getElementIndex( rowStartOffset );
            Element line = root.getElement( index );

            for (int i = 0; i < line.getElementCount(); i++)
            {
                Element child = line.getElement(i);
                AttributeSet as = child.getAttributes();
                String fontFamily = (String)as.getAttribute(StyleConstants.FontFamily);
                Integer fontSize = (Integer)as.getAttribute(StyleConstants.FontSize);
                String key = fontFamily + fontSize;

                FontMetrics fm = fonts.get( key );

                if (fm == null)
                {
                    Font font = new Font(fontFamily, Font.PLAIN, fontSize);
                    fm = component.getFontMetrics( font );
                    fonts.put(key, fm);
                }

                descent = Math.max(descent, fm.getDescent());
            }
        }

        return y - descent;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        //	Determine the width of the space available to draw the line number

        FontMetrics fontMetrics = component.getFontMetrics( component.getFont() );
        Insets insets = getInsets();
        int availableWidth = getSize().width - insets.left - insets.right;

        //  Determine the rows to draw within the clipped bounds.

        Rectangle clip = g.getClipBounds();
        int rowStartOffset = component.viewToModel2D(new Point(0, clip.y));
        int endOffset = component.viewToModel2D(new Point(0, clip.y + clip.height));

        Map<Integer, Character> copy;
        synchronized(lineVals) {
            copy = Map.copyOf(lineVals);
        }

        while (rowStartOffset <= endOffset)
        {
            try
            {
                int y = getOffsetY(rowStartOffset, fontMetrics);
                Character val = copy.get(y / fontMetrics.getHeight() + 1);
                String lineNumber = val == null ? " " : val.toString();
                int stringWidth = fontMetrics.stringWidth( lineNumber );
                int x = getOffsetX(availableWidth, stringWidth) + insets.left;

                if(val != null) {
                    if(val == 'F') {
                        g.setColor(Color.RED);
                    } else if(val == 'P') {
                        g.setColor(Color.GREEN);
                    }
                }

                g.drawString(lineNumber, x, y);

                rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
            }
            catch(Exception e) {break;}
        }
    }
}
