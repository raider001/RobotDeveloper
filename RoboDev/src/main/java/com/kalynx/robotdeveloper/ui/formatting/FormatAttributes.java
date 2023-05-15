package com.kalynx.robotdeveloper.ui.formatting;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class FormatAttributes {
    private static FormatAttributes INSTANCE;

    private SimpleAttributeSet sectionAttribute;
    public synchronized static FormatAttributes getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FormatAttributes();
        }
        return INSTANCE;
    }

    private FormatAttributes() {
        sectionAttribute = new SimpleAttributeSet();
        sectionAttribute.addAttribute(StyleConstants.Foreground, Color.GREEN);
    }

    public SimpleAttributeSet getSectionAttribute() {
        return sectionAttribute;
    }
}
