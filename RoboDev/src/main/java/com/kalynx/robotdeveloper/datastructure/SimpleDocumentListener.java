package com.kalynx.robotdeveloper.datastructure;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface SimpleDocumentListener extends DocumentListener {
    @Override
    default void insertUpdate(DocumentEvent e) {
        onDocumentChange(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        onDocumentChange(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        onDocumentChange(e);
    }

    void onDocumentChange(DocumentEvent e);
}
