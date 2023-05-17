package com.kalynx.robotdeveloper.ui.dialogs;

import com.kalynx.robotdeveloper.configuration.ConfigWriter;
import com.kalynx.robotdeveloper.configuration.PalletConfig;
import com.kalynx.robotdeveloper.datastructure.TextFormatModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;
import java.util.function.Consumer;

public class TextColorDialog extends JDialog {
    private static final String CHANGE = "Change";
    public static final SimpleAttributeSet DEFAULT_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet SECTION_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet TEST_OR_KEYWORD_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet RESOURCE_LIBRARY_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet SPECIAL_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet VARIABLES_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet COMMENT_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet IMPORTED_KEYWORD_ATTRIBUTES = new SimpleAttributeSet();

    private final TextFormatModel textFormatModel;

    public TextColorDialog(TextFormatModel textFormatModel) {
        setModal(true);
        this.textFormatModel = Objects.requireNonNull(textFormatModel);

        setColor(SECTION_ATTRIBUTES, textFormatModel.getModel().getSections());
        setColor(TEST_OR_KEYWORD_ATTRIBUTES, textFormatModel.getModel().getNewKeyWords());
        setColor(SPECIAL_ATTRIBUTES, textFormatModel.getModel().getSetupWords());
        setColor(RESOURCE_LIBRARY_ATTRIBUTES, textFormatModel.getModel().getResourceAndLib());
        setColor(VARIABLES_ATTRIBUTES, textFormatModel.getModel().getVariables());
        setColor(COMMENT_ATTRIBUTES, textFormatModel.getModel().getComments());
        setColor(IMPORTED_KEYWORD_ATTRIBUTES, textFormatModel.getModel().getImportedWords());
        setColor(DEFAULT_ATTRIBUTES, textFormatModel.getModel().getDef());

        setTitle("Options");
        setLayout(new MigLayout("", "[][]", "[][][][][][]"));
        JLabel sectionsLabel = new JLabel("Sections");
        sectionsLabel.setForeground(textFormatModel.getModel().getSections());
        add(sectionsLabel, "cell 0 0");
        JButton button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(sectionsLabel, SECTION_ATTRIBUTES));
        add(button, "cell 1 0");

        JLabel newKeyWordLabel = new JLabel("New Key Word");
        newKeyWordLabel.setForeground(textFormatModel.getModel().getNewKeyWords());
        add(newKeyWordLabel, "cell 0 1");
        button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(newKeyWordLabel, TEST_OR_KEYWORD_ATTRIBUTES));
        add(button, "cell 1 1");

        JLabel setupLabel = new JLabel("Setup Words");
        setupLabel.setForeground(textFormatModel.getModel().getSetupWords());
        add(setupLabel, "cell 0 2");
        button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(setupLabel, SPECIAL_ATTRIBUTES));
        add(button, "cell 1 2");

        JLabel resourceLibraryLabel = new JLabel("Resource/Library");
        resourceLibraryLabel.setForeground(textFormatModel.getModel().getResourceAndLib());
        add(resourceLibraryLabel, "cell 0 3");
        button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(resourceLibraryLabel, RESOURCE_LIBRARY_ATTRIBUTES));
        add(button, "cell 1 3");

        JLabel variablesLabel = new JLabel("Variables");
        variablesLabel.setForeground(textFormatModel.getModel().getVariables());
        add(variablesLabel, "cell 0 4");
        button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(variablesLabel, VARIABLES_ATTRIBUTES));
        add(button, "cell 1 4");

        JLabel commentsLabel = new JLabel("Comments");
        commentsLabel.setForeground(textFormatModel.getModel().getComments());
        add(commentsLabel, "cell 0 5");
        button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(commentsLabel, COMMENT_ATTRIBUTES));
        add(button, "cell 1 5");

        JLabel importedKeywordLabel = new JLabel("Imported Keywords");
        importedKeywordLabel.setForeground(textFormatModel.getModel().getImportedWords());
        add(importedKeywordLabel, "cell 0 6");
        button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(importedKeywordLabel, IMPORTED_KEYWORD_ATTRIBUTES));
        add(button, "cell 1 6");

        JLabel defaultLabel = new JLabel("default");
        defaultLabel.setForeground(textFormatModel.getModel().getImportedWords());
        add(defaultLabel, "cell 0 7");
        button = new JButton(CHANGE);
        button.addActionListener(a -> setColor(defaultLabel, DEFAULT_ATTRIBUTES));
        add(button, "cell 1 7");

        JLabel label8 = new JLabel("Text Size");
        add(label8, "cell 0 8");
        JFormattedTextField fontSizeField = new JFormattedTextField();
        ((AbstractDocument) fontSizeField.getDocument()).setDocumentFilter(new NumericAndLengthFilter(2));
        fontSizeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                PalletConfig palletConfig = textFormatModel.getModel();
                palletConfig.setFontSize(Integer.parseInt(fontSizeField.getText()));

                setColor(SECTION_ATTRIBUTES, textFormatModel.getModel().getSections());
                setColor(TEST_OR_KEYWORD_ATTRIBUTES, textFormatModel.getModel().getNewKeyWords());
                setColor(SPECIAL_ATTRIBUTES, textFormatModel.getModel().getSetupWords());
                setColor(RESOURCE_LIBRARY_ATTRIBUTES, textFormatModel.getModel().getResourceAndLib());
                setColor(VARIABLES_ATTRIBUTES, textFormatModel.getModel().getVariables());
                setColor(COMMENT_ATTRIBUTES, textFormatModel.getModel().getComments());
                setColor(IMPORTED_KEYWORD_ATTRIBUTES, textFormatModel.getModel().getImportedWords());
                setColor(DEFAULT_ATTRIBUTES, textFormatModel.getModel().getDef());
                textFormatModel.setModel(palletConfig);
            }


        });
        add(fontSizeField, "cell 1 8");
        pack();

        Consumer<PalletConfig> consumer = e -> {
            SwingUtilities.invokeLater(() -> {
                sectionsLabel.setForeground(e.getSections());
                newKeyWordLabel.setForeground(e.getNewKeyWords());
                setupLabel.setForeground(e.getSetupWords());
                resourceLibraryLabel.setForeground(e.getResourceAndLib());
                variablesLabel.setForeground(e.getVariables());
                commentsLabel.setForeground(e.getComments());
                importedKeywordLabel.setForeground(e.getImportedWords());
                defaultLabel.setForeground(e.getDef());
                fontSizeField.setValue(e.getFontSize());
            });
        };
        textFormatModel.addModelChangeNotifier(consumer);
    }

    private void setColor(JLabel label, SimpleAttributeSet attrSet) {
        Color c = JColorChooser.showDialog(this, "Select Text Colour",  Color.WHITE);
        label.setForeground(c);
        StyleConstants.setForeground(attrSet, c);
        StyleConstants.setFontSize(attrSet, textFormatModel.getModel().getFontSize());
    }

    private void setColor(SimpleAttributeSet attrSet, Color c) {
        StyleConstants.setForeground(attrSet, c);
        StyleConstants.setFontSize(attrSet, textFormatModel.getModel().getFontSize());
    }

    private static class NumericAndLengthFilter extends DocumentFilter {
        /**
         * Number of characters allowed.
         */
        private int length = 0;
        /**
         * Restricts the number of charcacters can be entered by given length.
         *
         * @param length Number of characters allowed.
         */
        public NumericAndLengthFilter(int length) {
            this.length = length;
        }
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
                                 AttributeSet attr) throws BadLocationException {
            if (isNumeric(string)) {
                if (this.length > 0 && fb.getDocument().getLength() + string.length() > this.length) {
                    return;
                }
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                            AttributeSet attrs) throws BadLocationException {
            if (isNumeric(text)) {
                if (this.length > 0 && fb.getDocument().getLength() + text.length() > this.length) {
                    return;
                }
                super.insertString(fb, offset, text, attrs);
            }
        }

        private boolean isNumeric(String text) {
            if (text == null || text.trim().equals("")) {
                return false;
            }
            for (int iCount = 0; iCount < text.length(); iCount++) {
                if (!Character.isDigit(text.charAt(iCount))) {
                    return false;
                }
            }
            return true;
        }
    }
}
