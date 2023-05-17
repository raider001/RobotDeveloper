package com.kalynx.robotdeveloper.ui;

import com.kalynx.robotdeveloper.configuration.ConfigWriter;
import com.kalynx.robotdeveloper.configuration.WorkspaceConfiguration;
import com.kalynx.robotdeveloper.datastructure.EditorModel;
import com.kalynx.robotdeveloper.datastructure.FileTreeModel;
import com.kalynx.robotdeveloper.datastructure.WorkingDirectoryModel;
import com.kalynx.robotdeveloper.datastructure.WorkspaceModel;
import com.kalynx.robotdeveloper.fileoperations.CreateTestSuite;
import com.kalynx.robotdeveloper.graphic.ImageFactory;
import com.kalynx.robotdeveloper.ui.customcomponents.FileTree;
import com.kalynx.robotdeveloper.ui.customcomponents.FileTreeCellRenderer;
import com.kalynx.robotdeveloper.ui.customcomponents.WorkspacePanel;
import com.kalynx.robotdeveloper.ui.dialogs.AboutDialog;
import com.kalynx.robotdeveloper.ui.dialogs.LibrariesDialog;
import com.kalynx.robotdeveloper.ui.dialogs.LicenseDialog;
import com.kalynx.robotdeveloper.ui.dialogs.TextColorDialog;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class MainWindow extends JFrame {

    private final WorkingDirectoryModel workingDirectoryModel;
    private final WorkspaceModel workspaceModel;
    private final WorkspacePanel workspacePanel;
    private final ImageFactory imageFactory;
    private final TextColorDialog textColorDialog;
    private Consumer<File> workingDirectoryModelChangeListener;
    public MainWindow(WorkingDirectoryModel workingDirectoryModel, WorkspaceModel workspaceModel, WorkspacePanel workspacePanel, ImageFactory imageFactory, TextColorDialog textColorDialog) {
        this.workingDirectoryModel = Objects.requireNonNull(workingDirectoryModel);
        this.workspaceModel = Objects.requireNonNull(workspaceModel);
        this.imageFactory = Objects.requireNonNull(imageFactory);
        this.textColorDialog = Objects.requireNonNull(textColorDialog);
        this.workspacePanel = workspacePanel;
        workspacePanel.setMinimumSize(new Dimension(700, 100));
        setTitle("Robot Developer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new MigLayout("", "0[grow]0", "0[]0[grow][]0"));
        addMenuBar();
        addMainPanel();
        WorkspaceConfiguration wsConfig = ConfigWriter.loadConfig(WorkspaceConfiguration.class);
        if(wsConfig != null) {
            workingDirectoryModel.updateModel(wsConfig.lastWorkspace());
            for(File f: wsConfig.openFiles()) {
                workspaceModel.addEditorModel(new EditorModel(f.getName(), f));
            }
        }
        pack();
    }

    private void addMainPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, addFolderNavigation(), workspacePanel);
        add(splitPane, "cell 0 1, grow");
    }

    private JComponent addFolderNavigation() {
        TreeModel model =new FileTreeModel(new File(System.getProperty("user.dir")));
        JScrollPane fileScrollPane = new JScrollPane();
        fileScrollPane.setMinimumSize(new Dimension (200, 400));
        FileTree tree = new FileTree();
        tree.setCellRenderer(new FileTreeCellRenderer());
        tree.setMinimumSize(new Dimension(200,100));
        fileScrollPane.setViewportView(tree);
        workingDirectoryModelChangeListener = tree::setModel;
        workingDirectoryModel.addModelObserver(workingDirectoryModelChangeListener);

        return fileScrollPane;
    }
    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        ImageIcon icon = new ImageIcon(imageFactory.getImage("add.png"));
        JMenuItem createSuite = new JMenuItem("Create Suite", icon);

        createSuite.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if(fileChooser.showSaveDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                Optional<String> result = CreateTestSuite.create(f);

                if(result.isPresent()) {
                    JOptionPane.showMessageDialog(MainWindow.this, result.get(),"Failed to create project", JOptionPane.ERROR_MESSAGE);
                } else {
                    workingDirectoryModel.updateModel(f);
                    ConfigWriter.writeConfig(new WorkspaceConfiguration(f, new ArrayList<>()));
                }
            }
        });

        fileMenu.add(createSuite);
        icon = new ImageIcon(imageFactory.getImage("open-folder.png"));
        JMenuItem loadSuite = new JMenuItem("Load Suite", icon);
        loadSuite.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = fileChooser.showOpenDialog(MainWindow.this);

            if(result == JFileChooser.APPROVE_OPTION) {
                if(Path.of(fileChooser.getSelectedFile().getPath(), "__init__.robot").toFile().exists()) {
                    workingDirectoryModel.updateModel(fileChooser.getSelectedFile());
                    ConfigWriter.writeConfig(new WorkspaceConfiguration(fileChooser.getSelectedFile(), new ArrayList<>()));
                } else {
                    JOptionPane.showMessageDialog(MainWindow.this, "Not a valid project.","Failed to Load Project", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        fileMenu.add(loadSuite);
        menuBar.add(fileMenu);

        add(menuBar, "cell 0 0, spanx 3, grow");

        JMenu options = new JMenu("Options");

        JMenuItem textColorItem = new JMenuItem("Text Theme");
        textColorItem.addActionListener(a -> {
            textColorDialog.setLocationRelativeTo(this);
            textColorDialog.setVisible(true);
        });
        options.add(textColorItem);
        menuBar.add(options);

        JMenu help = new JMenu("Help");
        JMenuItem licenceItem = new JMenuItem("License");
        licenceItem.addActionListener(a -> {
            LicenseDialog.getInstance().setLocationRelativeTo(this);
            LicenseDialog.getInstance().setVisible(true);
        });
        help.add(licenceItem);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(a -> {
            AboutDialog.getInstance().setLocationRelativeTo(this);
            AboutDialog.getInstance().setVisible(true);
        });
        help.add(aboutItem);

        JMenuItem usedLibraries = new JMenuItem("Used Libraries");
        usedLibraries.addActionListener(a -> {
            LibrariesDialog.getInstance().setLocationRelativeTo(this);
            LibrariesDialog.getInstance().setVisible(true);
        });
        help.add(usedLibraries);
        menuBar.add(help);
    }
}
