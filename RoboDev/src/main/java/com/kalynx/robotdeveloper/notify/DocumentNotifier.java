package com.kalynx.robotdeveloper.notify;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public abstract class DocumentNotifier extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) {
        update(fb, offset, length);
    }
    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) {
        update(fb, offset, text.length());
    }
    @Override
    public void remove(FilterBypass fb, int offset, int length) {
        update(fb, offset, length);
    }

    public abstract void update(FilterBypass fb, int offset, int length);
}
