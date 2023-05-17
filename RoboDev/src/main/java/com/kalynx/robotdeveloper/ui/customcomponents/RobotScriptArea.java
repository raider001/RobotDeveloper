package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.configuration.PalletConfig;
import com.kalynx.robotdeveloper.datastructure.EditorModel;
import com.kalynx.robotdeveloper.datastructure.SimpleDocumentListener;
import com.kalynx.robotdeveloper.datastructure.TextFormatModel;
import com.kalynx.robotdeveloper.notify.DocumentNotifier;
import com.kalynx.robotdeveloper.server.TestCollectionServer;
import com.kalynx.robotdeveloper.server.data.EndKeyWordEventData;
import com.kalynx.robotdeveloper.server.data.EndTestEventData;
import com.kalynx.robotdeveloper.token.RobotTokenizer;
import com.kalynx.robotdeveloper.ui.customcomponents.keyreference.KeyReferencePane;
import com.kalynx.robotdeveloper.ui.dialogs.TextColorDialog;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RobotScriptArea extends JScrollPane {

    private final JTextPane editArea;
    private HighlightDocumentFilter observableDocFilter = new HighlightDocumentFilter();
    private LineWidget pw;
    public RobotScriptArea(EditorModel editorModel) {
        DefaultStyledDocument document = new DefaultStyledDocument();
        document.setDocumentFilter(observableDocFilter);
        editArea = new JTextPane(document);
        setViewportView(editArea);
        pw = new LineWidget(editArea, true);
        LineWidget lw = new LineWidget(editArea, false);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(1);
        flowLayout.setVgap(0);
        JPanel panel = new JPanel(flowLayout);
        panel.add(pw);
        panel.add(lw);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setRowHeaderView(panel);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);

        Consumer<EndKeyWordEventData> endKeyWordEventDataListener = e -> {
            SwingUtilities.invokeLater(() -> {
                Path p = Path.of(e.source());
                if(p.equals(editorModel.getFile().toPath())){
                    pw.updateLine(e.lineno(), e.status().equals("PASS") ? "P" : "F", e.status().equals("PASS") ? Color.GREEN: Color.RED);
                }
            });
        };
        Consumer<EndTestEventData> endTestEventDataListener = e -> {
            SwingUtilities.invokeLater(() -> {
                Path p = Path.of(e.source());
                if(p.equals(editorModel.getFile().toPath())){
                    pw.updateLine(e.lineno(), e.status().equals("PASS") ? "P" : "F", e.status().equals("PASS") ? Color.GREEN: Color.RED);
                }
            });
        };
        DocumentNotifier clearResultsNotifier = new DocumentNotifier() {
            @Override
            public void update(FilterBypass fb, int offset, int length) {
                pw.clearResults();
            }
        };
        DocumentNotifier filterReferencePaneNotifier = new DocumentNotifier() {
            @Override
            public void update(FilterBypass fb, int offset, int length) {
                try {
                    String val = fb.getDocument().getText(0, offset + 1);
                    int lastWs = val.lastIndexOf("  ");
                    int lastnl = val.lastIndexOf("\n");
                    int fin = Math.max(lastWs,lastnl);
                    if(fin == -1) return;
                    SwingUtilities.invokeLater(() -> {
                        Main.DI.getDependency(KeyReferencePane.class).setFilter(val.substring(fin));
                    });
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Consumer<PalletConfig> palletChangeListener = p -> {
                SwingUtilities.invokeLater(() -> {
                    lw.updateSize();
                    getParent().repaint();
                    getParent().revalidate();
               });

        };



        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                Main.DI.getDependency(TestCollectionServer.class).addEndKeyWordEventDataListener(endKeyWordEventDataListener);
                Main.DI.getDependency(TestCollectionServer.class).addEndTestEventDataListener(endTestEventDataListener);
                Main.DI.getDependency(TextFormatModel.class).addModelChangeNotifier(palletChangeListener);
                observableDocFilter.addDocumentNotifier(clearResultsNotifier);
                observableDocFilter.addDocumentNotifier(filterReferencePaneNotifier);

            }

            @Override
            public void componentHidden(ComponentEvent e) {
                Main.DI.getDependency(TestCollectionServer.class).removeEndKeyWordEventDataListener(endKeyWordEventDataListener);
                Main.DI.getDependency(TestCollectionServer.class).removeEndTestEventDataListener(endTestEventDataListener);
                Main.DI.getDependency(TextFormatModel.class).removeModelChangeNotifier(palletChangeListener);
                observableDocFilter.removeDocumentNotifier(clearResultsNotifier);
                observableDocFilter.removeDocumentNotifier(filterReferencePaneNotifier);
            }
        });

    }

    public void clearTestResults() {
        pw.clearResults();
    }

    public Document getDocument() {
        return editArea.getDocument();
    }

    public void addDocumentNotifier(DocumentNotifier notifier) {
        observableDocFilter.addDocumentNotifier(notifier);
    }

    public void removeDocumentNotifier(DocumentNotifier notifier) {
        observableDocFilter.removeDocumentNotifier(notifier);
    }

}

class HighlightDocumentFilter extends DocumentFilter {
    private List<DocumentFilter> docFilter = new ArrayList<>();

    public void addDocumentNotifier(DocumentNotifier notifier) {
        docFilter.add(notifier);
    }

    public void removeDocumentNotifier(DocumentNotifier notifier) {
        docFilter.remove(notifier);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        super.replace(fb, offset, length, text, attrs);
        update(fb.getDocument());
        docFilter.parallelStream().forEach(filter -> {
            try {
                filter.replace(fb, offset, length, text, attrs);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, text, attr);
        update(fb.getDocument());
        docFilter.parallelStream().forEach(filter -> {
            try {
                filter.insertString(fb, offset, text, attr);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
        update(fb.getDocument());

        docFilter.parallelStream().forEach(filter -> {
            try {
                filter.remove(fb, offset, length);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });

    }
    private void update(Document fb) throws BadLocationException {
        StyledDocument d = (StyledDocument) fb;

        d.setCharacterAttributes(0, d.getLength(), TextColorDialog.DEFAULT_ATTRIBUTES, true);

        for(var token: RobotTokenizer.getInstance().getTokens()) {
            Pattern pattern = Pattern.compile(token.getRegex(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(fb.getText(0, d.getLength()));
            while(matcher.find()) {
                d.setCharacterAttributes(matcher.start(), matcher.group().length(), token.getAttrSet(), false);
            }
        }
    }
}
