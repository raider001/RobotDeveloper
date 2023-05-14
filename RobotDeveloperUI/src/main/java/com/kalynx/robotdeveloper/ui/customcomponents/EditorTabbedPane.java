package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.command.CommandHandler;
import com.kalynx.robotdeveloper.datastructure.EditorModel;
import com.kalynx.robotdeveloper.datastructure.ResourceType;
import com.kalynx.robotdeveloper.datastructure.SimpleDocumentListener;
import com.kalynx.robotdeveloper.token.RobotTokenizer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorTabbedPane extends JTabbedPane {
    private EditorModel editorModel;
    private RobotScriptArea robotScriptArea;
    public EditorTabbedPane(EditorModel editorModel, CommandHandler commandHandler) {
        this.editorModel = Objects.requireNonNull(editorModel);
        robotScriptArea = new RobotScriptArea(editorModel);
        setTabPlacement(BOTTOM);
        JPanel objectArea = new JPanel(new MigLayout("", "[fill]", "[fill]"));

        addTab("Text Area", robotScriptArea);
//        addTab("Object Area", objectArea);
//        addTab("Script results", new JPanel());

        InputMap map = robotScriptArea.getInputMap();
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        registerKeyboardAction(new SaveActionListener(robotScriptArea.getDocument()), keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        Executors.newSingleThreadExecutor().submit(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(editorModel.getFile()))) {

                Document doc = robotScriptArea.getDocument();
                reader.lines().forEach(line -> {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            robotScriptArea.getDocument().insertString(doc.getLength(), line + "\n", null);
                        } catch (BadLocationException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        editorModel.addEditedChangeNotifier(edited -> {
            if(edited) {
                editorModel.setEditorName(editorModel.getEditorName() + "*");
            } else {
                editorModel.setEditorName(editorModel.getEditorName().replace("*", ""));
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    getResources().parallelStream().forEach(commandHandler::generateDoc);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        robotScriptArea.getDocument().addDocumentListener((SimpleDocumentListener) e -> editorModel.setEdited(true));
    }

    private List<ResourceType> getResources() throws BadLocationException {

        Pattern pattern = Pattern.compile(RobotTokenizer.RESOURCE_LIBRARY_KEYWORD, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(robotScriptArea.getDocument().getText(0, robotScriptArea.getDocument().getLength()));
        List<ResourceType> resources = new ArrayList<>();
        while(matcher.find()) {
            String r = robotScriptArea.getDocument().getText(matcher.start(), matcher.group().length());

            String[] vals = r.strip().split(" ");
            if(r.startsWith("\nResource")) {
                String abs = Paths.get(editorModel.getFile().toPath().getParent().toString(), vals[vals.length - 1]).toString();
                resources.add(new ResourceType(abs, true));
            } else {
                resources.add(new ResourceType(vals[vals.length - 1],false));
            }
        }
        return resources;
    }

    public void clearTestResults() {
        robotScriptArea.clearTestResults();
    }

    public void save() {
        try {
            Document d = robotScriptArea.getDocument();
            String f = d.getText(0, d.getLength());
            Files.writeString(editorModel.getFile().toPath(), f);
            editorModel.setEdited(false);
        } catch (BadLocationException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private class SaveActionListener implements ActionListener {

        private Document d;
        public SaveActionListener(Document d) {
            this.d = Objects.requireNonNull(d);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            save();
        }
    }
}
