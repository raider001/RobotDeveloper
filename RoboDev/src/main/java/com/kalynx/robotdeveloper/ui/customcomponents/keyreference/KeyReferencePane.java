package com.kalynx.robotdeveloper.ui.customcomponents.keyreference;

import com.kalynx.robotdeveloper.datastructure.LibraryResourceModel;
import com.kalynx.robotdeveloper.datastructure.keywordspec.Argument;
import com.kalynx.robotdeveloper.datastructure.keywordspec.Keyword;
import com.kalynx.robotdeveloper.datastructure.keywordspec.KeywordSpec;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class KeyReferencePane extends JPanel {
    private final LibraryResourceModel model;

    public void setFilter(String str) {
        filter(str);
    }

    public KeyReferencePane(LibraryResourceModel model) {
        this.model = model;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        model.addLibraryAddedNotifier(spec -> {
            SwingUtilities.invokeLater(() -> {
                add(new LibraryPanel(spec));
                revalidate();
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
                revalidate();
            });
        });
    }

    private void filter(String filter) {
        for(int i = 0; i < getComponents().length; i++) {
            LibraryPanel libPanel = (LibraryPanel) getComponent(i);
            for(int j = 0; j < libPanel.getComponents().length; j++) {
                JLabel label = (JLabel) libPanel.getComponent(j);
                label.setVisible(label.getText().startsWith(filter.strip()));
            }
        }
        revalidate();
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
                add(label, "cell 0 " + j +", hidemode 3");
            }
        }
    }

    public String toolTipTextGenerator(Keyword keyword) {
        String html = "<html><body><h3>Arguments</h3>";

        for(Argument argument: keyword.getArgs()) {
            html +=  argument.getName() + (argument.isRequired() ? " - Required": "") + "<br />";
        }
        html += "<h3>Documentation</h3>" + keyword.getShortDoc() + "<br /></body></html>";

        return html;
    }
}
