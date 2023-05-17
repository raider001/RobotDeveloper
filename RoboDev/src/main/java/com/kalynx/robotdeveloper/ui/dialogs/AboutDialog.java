package com.kalynx.robotdeveloper.ui.dialogs;

import javax.swing.*;

public class AboutDialog extends JDialog {

    private static AboutDialog INSTANCE;

    public synchronized static AboutDialog getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AboutDialog();
        }
        return INSTANCE;
    }

    private AboutDialog() {
        setModal(true);
        setTitle("About");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        add(textArea);
        setResizable(false);
        textArea.append("""
                A Basic IDE created to allow for easier development for robot scripts.
                """);
        pack();
    }
}
