package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.datastructure.TextFormatModel;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class LineWidget extends JPanel implements CaretListener {

    // Controls the height of the widget
    private static final int WIDGET_HEIGHT = Integer.MAX_VALUE - 1000000;
    private int selectedLine = 0;
    private final JTextComponent mainComponent;
    private final Set<LineData> lineData = new HashSet<>();

    private final boolean manuallyManaged;
    public LineWidget(JTextComponent component, boolean manuallyManaged) {
        this.manuallyManaged = manuallyManaged;
        this.mainComponent = Objects.requireNonNull(component);
        mainComponent.addCaretListener(this);

    }

    public void clearResults() {
        synchronized(lineData) {
            lineData.clear();
        }
    }
    public void updateLine(int line, String val, Color color) {
        synchronized(lineData) {
            lineData.add(new LineData(line, val, color));
        }
    }

    /**
     * Updates the line numbers to match the main components font size.
     */
    public void updateSize() {

        Font f = new Font(mainComponent.getName(), Font.PLAIN, Main.DI.getDependency(TextFormatModel.class).getModel().getFontSize());
        FontMetrics fontMetrics = getFontMetrics(f);
        Insets insets = getInsets();
        int width = fontMetrics.charWidth('0') * 3;

        Dimension widgetSize = new Dimension(insets.left + width + insets.right, WIDGET_HEIGHT);
        setMinimumSize(widgetSize);
        setPreferredSize(widgetSize);
        setSize(widgetSize);
        setMaximumSize(widgetSize);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ((Graphics2D)g).setRenderingHints(rh);
        if(manuallyManaged) {
            generateManualLines(g);
        } else {
            int size = Main.DI.getDependency(TextFormatModel.class).getModel().getFontSize();
            setFont(new Font(mainComponent.getFont().getFontName(), Font.PLAIN, size));

            Rectangle clip = g.getClipBounds();
            int rowStartOffset = mainComponent.viewToModel2D(new Point(0, clip.y));
            int rowEndOffset = mainComponent.viewToModel2D(new Point(0, clip.y + clip.height));

            Font f = new Font(mainComponent.getName(), Font.PLAIN, Main.DI.getDependency(TextFormatModel.class).getModel().getFontSize());
            FontMetrics fontMetrics = getFontMetrics(f);
            g.setFont(f);

            try {
                while (rowStartOffset <= rowEndOffset) {
                    String lineNumber = getLineNumber(rowStartOffset);
                    Rectangle2D r = mainComponent.modelToView2D(rowStartOffset);
                    int x = getXOffset(fontMetrics.stringWidth(lineNumber), getSize().width - getInsets().left - getInsets().right);
                    int y = (int) (r.getY() + r.getHeight() - fontMetrics.getDescent());
                    g.drawString(lineNumber, x, y);
                    rowStartOffset = Utilities.getRowEnd(mainComponent, rowStartOffset) + 1;
                }
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int  getXOffset(int stringWidth, int availableWidth) {
        return (int)((availableWidth - stringWidth) * 0.9);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int caretPosition = mainComponent.getCaretPosition();

        if(selectedLine != caretPosition) {
            this.selectedLine = caretPosition;
            updateSize();
            getParent().repaint();
        }
    }


    public void generateManualLines(Graphics g) {
        Font f = new Font(mainComponent.getName(), Font.PLAIN, Main.DI.getDependency(TextFormatModel.class).getModel().getFontSize());
        FontMetrics fontMetrics = getFontMetrics(f);
        g.setFont(f);
        lineData.forEach(item -> {
            int x = getXOffset(fontMetrics.stringWidth(item.text), getSize().width - getInsets().left - getInsets().right);
            int y = item.lineNumber * fontMetrics.getHeight() - fontMetrics.getDescent();
            g.setColor(item.color);
            g.setFont(f);
            g.drawString(item.text, x, y);
        });
        getParent().repaint();
    }

    protected String getLineNumber(int rowStartOffset)
    {
        Element root = mainComponent.getDocument().getDefaultRootElement();
        int index = root.getElementIndex( rowStartOffset );
        Element line = root.getElement( index );

        if (line.getStartOffset() == rowStartOffset)
            return String.valueOf(index + 1);
        else
            return "";
    }

    private class LineData {
        int lineNumber;
        String text;
        Color color;

        public LineData(int lineNumber, String text, Color color) {
            this.lineNumber = lineNumber;
            this.text = text;
            this.color = color;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LineData lineData = (LineData) o;
            return lineNumber == lineData.lineNumber;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lineNumber);
        }
    }
}
