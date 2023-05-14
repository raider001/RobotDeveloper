package com.kalynx.robotdeveloper.ui.customcomponents.keyreference;

import com.kalynx.robotdeveloper.datastructure.LibraryResourceModel;
import com.kalynx.robotdeveloper.datastructure.SimpleDocumentListener;
import com.kalynx.robotdeveloper.datastructure.keywordspec.Argument;
import com.kalynx.robotdeveloper.datastructure.keywordspec.Keyword;
import com.kalynx.robotdeveloper.datastructure.keywordspec.KeywordSpec;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class KeyReferencePane extends JPanel {
    private final LibraryResourceModel model;

    public KeyReferencePane(LibraryResourceModel model) {
        this.model = model;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JTextField filterField = new JTextField();
        add(filterField);

        filterField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void onDocumentChange(DocumentEvent e) {

            }
        });

        model.addLibraryAddedNotifier(spec -> {
            SwingUtilities.invokeLater(() -> {
                add(new LibraryPanel(spec));
            });
        });

        model.addLibraryRemovedNotifier(spec -> {
            SwingUtilities.invokeLater(() -> {
                Component[] components = getComponents();
                for(int i = 0; i < components.length; i++) {
                    LibraryPanel p = (LibraryPanel) components[i];

                    if(p.libraryName.equals(spec.getName())) {
                        remove(i);
                    }
                }
            });
        });
    }

    private class LibraryPanel extends JPanel {
        final String libraryName;

        public LibraryPanel(KeywordSpec kSpec) {
            libraryName = kSpec.getName();
            setBorder(new TitledBorder(libraryName));
            setLayout(new MigLayout("", "[]", "[][]"));
            List<Keyword> keywordsForLib = kSpec.getKeywords();
            for (int j = 0; j < keywordsForLib.size(); j++) {
                JLabel label = new JLabel(keywordsForLib.get(j).getName());
                label.setToolTipText(toolTipTextGenerator(keywordsForLib.get(j)));
                add(label, "cell 0 " + j);
            }
        }
    }

    public String toolTipTextGenerator(Keyword keyword) {
        String html = "<html><body><h3>Arguments</h3>";

        for(Argument argument: keyword.getArgs()) {
            html +=  argument.getName() + (argument.isRequired() ? " - Required": "") + "<br />";
        }
        System.out.println(keyword.getShortDoc());
        html += "<h3>Documentation</h3>" + keyword.getShortDoc() + "<br /></body></html>";

        return html;
    }
}
