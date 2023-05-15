package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.datastructure.EditorModel;
import com.kalynx.robotdeveloper.datastructure.SimpleDocumentListener;
import com.kalynx.robotdeveloper.notify.DocumentNotifier;
import com.kalynx.robotdeveloper.server.TestCollectionServer;
import com.kalynx.robotdeveloper.token.RobotTokenizer;
import com.kalynx.robotdeveloper.ui.customcomponents.keyreference.KeyReferencePane;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RobotScriptArea extends JScrollPane {

    private final JTextPane editArea;
    private LineResult lr;
    private HighlightDocumentFilter observableDocFilter = new HighlightDocumentFilter();
    public RobotScriptArea(EditorModel editorModel) {
        DefaultStyledDocument document = new DefaultStyledDocument();
        document.setDocumentFilter(observableDocFilter);
        editArea = new JTextPane(document);
        setViewportView(editArea);
        TextLineNumber tln = new TextLineNumber(editArea);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(1);
        flowLayout.setVgap(0);

        JPanel panel = new JPanel(flowLayout);
        lr = new LineResult(editArea);
        panel.add(lr);
        panel.add(tln);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setRowHeaderView(panel);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Main.DI.getDependency(TestCollectionServer.class).addEndKeyWordEventDataListener(e -> {
            SwingUtilities.invokeLater(() -> {
                Path p = Path.of(e.source());
            if(p.equals(editorModel.getFile().toPath())){
                    lr.updateLine(e.lineno(), e.status().equals("PASS") ? 'P' : 'F');
                }
            });
        });

        Main.DI.getDependency(TestCollectionServer.class).addEndTestEventDataListener(e -> {
            SwingUtilities.invokeLater(() -> {
                Path p = Path.of(e.source());
                if(p.equals(editorModel.getFile().toPath())){
                    lr.updateLine(e.lineno(), e.status().equals("PASS") ? 'P' : 'F');
                }
            });
        });
        observableDocFilter.addDocumentNotifier(new DocumentNotifier() {
            @Override
            public void update(FilterBypass fb, int offset, int length) {
                lr.clearResults();
            }
        });

        observableDocFilter.addDocumentNotifier(new DocumentNotifier() {
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
        });

    }

    public void clearTestResults() {
        lr.clearResults();
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
    private SimpleAttributeSet defaultSet = new SimpleAttributeSet();
    private SimpleAttributeSet sectionSet = new SimpleAttributeSet();
    private List<DocumentFilter> docFilter = new ArrayList<>();

    public void addDocumentNotifier(DocumentNotifier notifier) {
        docFilter.add(notifier);
    }

    public void removeDocumentNotifier(DocumentNotifier notifier) {
        docFilter.remove(notifier);
    }
    HighlightDocumentFilter() {

        StyleConstants.setForeground(defaultSet, Color.WHITE);
        StyleConstants.setForeground(sectionSet, Color.GREEN);
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

        d.setCharacterAttributes(0, d.getLength(), defaultSet, true);

        for(var token: RobotTokenizer.getInstance().getTokens()) {
            Pattern pattern = Pattern.compile(token.getRegex(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(fb.getText(0, d.getLength()));
            while(matcher.find()) {
                d.setCharacterAttributes(matcher.start(), matcher.group().length(), token.getAttrSet(), false);
            }
        }
    }
}
