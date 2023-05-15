package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.command.CommandHandler;
import com.kalynx.robotdeveloper.datastructure.WorkingDirectoryModel;
import com.kalynx.robotdeveloper.datastructure.WorkspaceModel;
import com.kalynx.robotdeveloper.graphic.ImageFactory;
import com.kalynx.robotdeveloper.ui.customcomponents.keyreference.KeyReferencePane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class WorkspacePanel extends JPanel {

    private final FileTabbedPane editorPanes;
    private final WorkingDirectoryModel workingDirectoryModel;
    private final CommandHandler commandHandler;
    public WorkspacePanel(WorkspaceModel workspaceModel, WorkingDirectoryModel workingDirectoryModel, CommandHandler commandHandler, KeyReferencePane referencePane) {
        this.workingDirectoryModel = Objects.requireNonNull(workingDirectoryModel);
        this.commandHandler = Objects.requireNonNull(commandHandler);
        editorPanes = new FileTabbedPane(workspaceModel, commandHandler);
        setLayout(new MigLayout("", "0[][grow]0", "0[grow]0"));
        add(createControls(), "cell 0 0, top");
        JSplitPane workspaceSplitPane = new JSplitPane();
        add(workspaceSplitPane, "grow, cell 1 0");
        workspaceSplitPane.setLeftComponent(editorPanes);
        workspaceSplitPane.setResizeWeight(.80);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(referencePane);
        workspaceSplitPane.setRightComponent(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        workspaceModel.addEditorAddedListener(editor -> {
            SwingUtilities.invokeLater(() -> {
                editorPanes.addTab(editor);
            });
        });

        workingDirectoryModel.addModelObserver(file -> {
            workspaceModel.getEditorModels().forEach(workspaceModel::removeEditorModel);
        });
    }
    private Component createControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("","0[]0","0[]2[]2[]0"));
        JButton button = new JButton();
        Icon i = new ImageIcon(Main.DI.getDependency(ImageFactory.class).getImage("diskette.png"));
        button.setIcon(i);
        button.setBorderPainted(false);
        button.addActionListener(new SaveSelectedEditorAL());
        panel.add(button, "cell 0 0");

        i = new ImageIcon(Main.DI.getDependency(ImageFactory.class).getImage("play-button.png"));
        button = new JButton();
        button.setIcon(i);
        button.setBorderPainted(false);
        button.addActionListener(new RunTestAL());
        panel.add(button, "cell 0 1");

        i = new ImageIcon(Main.DI.getDependency(ImageFactory.class).getImage("stop.png"));
        button = new JButton();
        button.setIcon(i);
        button.setBorderPainted(false);
        button.addActionListener(a -> {
            EditorPanel ep =  (EditorPanel)editorPanes.getSelectedComponent();
            SwingUtilities.invokeLater(ep::clearTestResults);
        });
        panel.add(button, "cell 0 2");

        return panel;
    }

    private class RunTestAL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPanel panel =  (EditorPanel)editorPanes.getSelectedComponent();
            SwingUtilities.invokeLater(panel::clearTestResults);
            commandHandler.runTest(workingDirectoryModel.getModel(), panel.getEditorModel().getFile());
        }
    }
    private class SaveSelectedEditorAL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
           EditorPanel panel =  (EditorPanel)editorPanes.getSelectedComponent();
           panel.save();
        }
    }
}
