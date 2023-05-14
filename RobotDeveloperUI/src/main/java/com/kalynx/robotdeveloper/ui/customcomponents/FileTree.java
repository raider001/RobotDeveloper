package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.datastructure.EditorModel;
import com.kalynx.robotdeveloper.datastructure.FileTreeModel;
import com.kalynx.robotdeveloper.datastructure.WorkspaceModel;
import com.kalynx.robotdeveloper.fileoperations.CreateTestFile;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileTree extends JTree {

    private transient final WatchService watchService;
    private transient WatchKey dirWatchKey;
    public FileTree() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ScheduledExecutorService watchPoller = Executors.newSingleThreadScheduledExecutor();
        watchPoller.scheduleAtFixedRate(() -> {
            try {
                List<WatchEvent<?>> events;
                if(dirWatchKey != null) {
                    synchronized (dirWatchKey) {
                        events = dirWatchKey.pollEvents();
                    }

                    if (!events.isEmpty()) {
                        SwingUtilities.invokeLater(this::updateUI);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }, 500, 500, TimeUnit.MILLISECONDS);

        addMouseListener(new RightMouseClickedMouseListener());

    }

    public void setModel(File file) {
        if(file.isFile()) throw new IllegalArgumentException("Given location is not a directory.");
        TreeModel model = new FileTreeModel(file);
        setModel(model);
        try {
            if (dirWatchKey != null) {
                dirWatchKey.cancel();
                synchronized (dirWatchKey) {
                    dirWatchKey = file.toPath().register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                }
            } else {
                dirWatchKey = file.toPath().register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    class RightMouseClickedMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            TreePath path = getClosestPathForLocation(e.getX(), e.getY());
            if(path == null) return;
            setSelectionPath(path);
            if(!(path.getLastPathComponent() instanceof File)) return;
            File file = (File)path.getLastPathComponent();
            if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && file.isFile()) {
                if(file.getPath().endsWith(".html")) {
                    try {
                        if(Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(file);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    Main.DI.getDependency(WorkspaceModel.class).addEditorModel(new EditorModel(file.getName(), file));
                }
            } else if(SwingUtilities.isRightMouseButton(e)) {

                JPopupMenu popupMenu = new JPopupMenu();

                JMenuItem item;
                item = new JMenuItem("New Directory");
                item.addActionListener(a -> createDirectory(file.getPath()));
                popupMenu.add(item);
                item = new JMenuItem("New Resource");
                item.addActionListener(a -> createFile(file.getPath(),  ".resource"));
                popupMenu.add(item);
                item = new JMenuItem("New Test");
                item.addActionListener(a -> createFile(file.getPath(), ".robot"));
                popupMenu.add(item);
                item = new JMenuItem("New Variable");
                item.addActionListener(a -> createFile(file.getPath(),  ".variables"));
                popupMenu.add(item);
                item = new JMenuItem("New Task");
                item.addActionListener(a -> createFile(file.getPath(),  ".task"));
                popupMenu.add(item);
                item = new JMenuItem("Rename");
                item.addActionListener(a -> rename(file.toPath()));
                popupMenu.add(item);
                item = new JMenuItem("Delete");
                item.addActionListener(a -> {
                    try {
                        Files.delete(file.toPath());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                popupMenu.add(item);
                popupMenu.show(FileTree.this, e.getX(), e.getY());
            }
        }

        private void rename(Path path) {
            String result = JOptionPane.showInputDialog(FileTree.this, "Rename");
            if(result != null && !result.isBlank()) {
                try {
                    String fileType = path.toString().substring(path.toString().lastIndexOf('.'));
                    Path newPath = Path.of(path.getParent().toString(), result + fileType);
                    Files.move(path,newPath);
                    WorkspaceModel wsModel = Main.DI.getDependency(WorkspaceModel.class);
                    EditorModel model =  wsModel.getEditorModel(path.toFile());
                    if(model != null) {
                        wsModel.getEditorModel(path.toFile()).setFile(newPath.toFile());
                        wsModel.replaceKey(path.toFile(), newPath.toFile());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void createDirectory(String path) {
            String result = JOptionPane.showInputDialog(FileTree.this, "Enter Directory Name");
            if(result != null && !result.isBlank()) {
                try {
                    Files.createDirectory(Path.of(path, result));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void createFile(String path, String suffix) {

            String result = JOptionPane.showInputDialog(FileTree.this, "Enter File Name");
            if(result != null && !result.isBlank()) {
                File file = Path.of(path.toString(), result + suffix).toFile();
                Optional<String> res = CreateTestFile.create(file);
                if(res.isPresent()) {
                    JOptionPane.showMessageDialog(FileTree.this, res.get(),"Failed to create project", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
