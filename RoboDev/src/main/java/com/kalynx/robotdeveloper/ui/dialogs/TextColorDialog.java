package com.kalynx.robotdeveloper.ui.dialogs;

import com.kalynx.robotdeveloper.configuration.ConfigWriter;
import com.kalynx.robotdeveloper.configuration.PalletConfig;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.function.Consumer;

public class TextColorDialog extends JDialog {

    private static TextColorDialog INSTANCE;
    private PalletConfig colorConfig = new PalletConfig(Color.GREEN, Color.RED, Color.ORANGE, Color.LIGHT_GRAY, Color.MAGENTA, Color.PINK, Color.GRAY);

    public static final SimpleAttributeSet SECTION_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet TEST_OR_KEYWORD_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet RESOURCE_LIBRARY_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet SPECIAL_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet VARIABLES_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet COMMENT_ATTRIBUTES = new SimpleAttributeSet();
    public static final SimpleAttributeSet IMPORTED_KEYWORD_ATTRIBUTES = new SimpleAttributeSet();

    public synchronized static TextColorDialog getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TextColorDialog();
        }
        return INSTANCE;
    }

    private TextColorDialog() {

        PalletConfig c = ConfigWriter.loadConfig(PalletConfig.class);

        if(c != null) {
            colorConfig = c;
        }

        setColor(SECTION_ATTRIBUTES, colorConfig.getSections());
        setColor(TEST_OR_KEYWORD_ATTRIBUTES, colorConfig.getNewKeyWords());
        setColor(SPECIAL_ATTRIBUTES, colorConfig.getSetupWords());
        setColor(RESOURCE_LIBRARY_ATTRIBUTES, colorConfig.getResourceAndLib());
        setColor(VARIABLES_ATTRIBUTES, colorConfig.getVariables());
        setColor(COMMENT_ATTRIBUTES, colorConfig.getComments());
        setColor(IMPORTED_KEYWORD_ATTRIBUTES, colorConfig.getImportedWords());

        setTitle("Options");
        setLayout(new MigLayout("", "[][]", "[][][][][][]"));
        JLabel label = new JLabel("Sections");
        label.setForeground(colorConfig.getSections());
        add(label, "cell 0 0");
        JButton button = new JButton("Change");
        button.addActionListener(a -> setColor(label, SECTION_ATTRIBUTES, colorConfig::setSections));
        add(button, "cell 1 0");

        JLabel label2 = new JLabel("New Key Word");
        label2.setForeground(colorConfig.getNewKeyWords());
        add(label2, "cell 0 1");
        button = new JButton("Change");
        button.addActionListener(a -> setColor(label2, TEST_OR_KEYWORD_ATTRIBUTES, colorConfig::setNewKeyWords));
        add(button, "cell 1 1");

        JLabel label3 = new JLabel("Setup Words");
        label3.setForeground(colorConfig.getSetupWords());
        add(label3, "cell 0 2");
        button = new JButton("Change");
        button.addActionListener(a -> setColor(label3, SPECIAL_ATTRIBUTES, colorConfig::setSetupWords));
        add(button, "cell 1 2");

        JLabel label4 = new JLabel("Resource/Library");
        label4.setForeground(colorConfig.getResourceAndLib());
        add(label4, "cell 0 3");
        button = new JButton("Change");
        button.addActionListener(a -> setColor(label4, RESOURCE_LIBRARY_ATTRIBUTES, colorConfig::setResourceAndLib));
        add(button, "cell 1 3");

        JLabel label5 = new JLabel("Variables");
        label5.setForeground(colorConfig.getVariables());
        add(label5, "cell 0 4");
        button = new JButton("Change");
        button.addActionListener(a -> setColor(label5, VARIABLES_ATTRIBUTES, colorConfig::setVariables));
        add(button, "cell 1 4");

        JLabel label6 = new JLabel("Comments");
        label6.setForeground(colorConfig.getComments());
        add(label6, "cell 0 5");
        button = new JButton("Change");
        button.addActionListener(a -> setColor(label6, COMMENT_ATTRIBUTES, colorConfig::setComments));
        add(button, "cell 1 5");

        JLabel label7 = new JLabel("Imported Keywords");
        label7.setForeground(colorConfig.getImportedWords());
        add(label7, "cell 0 6");
        button = new JButton("Change");
        button.addActionListener(a -> setColor(label7, IMPORTED_KEYWORD_ATTRIBUTES, colorConfig::setImportedWords));
        add(button, "cell 1 6");

        pack();
    }

    private void setColor(JLabel label, SimpleAttributeSet attrSet, Consumer<Color> colorChange) {
        Color c = JColorChooser.showDialog(this, "Select Text Colour",  Color.WHITE);
        label.setForeground(c);
        ConfigWriter.writeConfig(colorConfig);
        StyleConstants.setForeground(attrSet, c);
    }

    private void setColor(SimpleAttributeSet attrSet, Color c) {
        StyleConstants.setForeground(attrSet, c);
    }
}
