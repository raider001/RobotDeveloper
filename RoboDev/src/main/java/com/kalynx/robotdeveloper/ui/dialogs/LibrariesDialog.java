package com.kalynx.robotdeveloper.ui.dialogs;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibrariesDialog extends JDialog {

    private static LibrariesDialog INSTANCE;

    public synchronized static LibrariesDialog getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LibrariesDialog();
        }
        return INSTANCE;
    }

    private final List<String> thirdPartySource = new ArrayList<>();
    {
        thirdPartySource.add("flatlaf   - https://www.formdev.com/flatlaf/");
        thirdPartySource.add("miglayout - https://www.miglayout.com/");
        thirdPartySource.add("gson      - https://github.com/google/gson");
    }

    private final List<String> thirdPartyResource = new ArrayList<>();
    {
        thirdPartyResource.add("<a href=\"https://www.flaticon.com/free-icons/save\" title=\"save icons\">Save icons created by Freepik - Flaticon</a>");
        thirdPartyResource.add("<a href=\"https://www.flaticon.com/free-icons/play-button\" title=\"play button icons\">Play button icons created by Roundicons - Flaticon</a>");
        thirdPartyResource.add("<a href=\"https://www.flaticon.com/free-icons/ui\" title=\"ui icons\">Ui icons created by Dewi Sari - Flaticon</a>");
        thirdPartyResource.add("<a href=\"https://www.flaticon.com/free-icons/open-folder\" title=\"open folder icons\">Open folder icons created by Arkinasi - Flaticon</a>");
        thirdPartyResource.add("<a href=\"https://www.flaticon.com/free-icons/create\" title=\"create icons\">Create icons created by Rizki Ahmad Fauzi - Flaticon</a>");
    }

    private LibrariesDialog() {
        setModal(true);
        setTitle("Used Libraries and Resources");
        setLayout(new MigLayout("", "[]", "[][][]"));
        JLabel label = new JLabel("This page attributes thanks to all free libraries and resources provided by other open source contributors.");
        add(label, "cell 0 0");

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        for (String s : thirdPartySource) {
            textArea.append(s + "\n");
        }

        add(textArea, "cell 0 1");

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");

        HTMLEditorKit editorKit = (HTMLEditorKit) textPane.getEditorKit();
        String html = "<html><body>";
        for(String s: thirdPartyResource) {
               html += s + "<br />";
        }
        html += "</body></html>";

        try {
            editorKit.insertHTML((HTMLDocument) textPane.getDocument(), 0, html, 0, 0, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        add(textPane, "cell 0 2");
        setResizable(false);
        pack();
    }

    private int getEndOfDoc(JTextPane textPane) {
        return textPane.getDocument().getEndPosition().getOffset() - 1;
    }
}
