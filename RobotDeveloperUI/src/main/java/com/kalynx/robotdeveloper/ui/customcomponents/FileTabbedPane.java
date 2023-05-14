package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.command.CommandHandler;
import com.kalynx.robotdeveloper.datastructure.EditorModel;
import com.kalynx.robotdeveloper.datastructure.WorkspaceModel;
import com.kalynx.robotdeveloper.graphic.ImageFactory;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class FileTabbedPane extends JTabbedPane {

    private final Map<EditorModel, Component>  tabbedPanels = new HashMap<>();
    private final CommandHandler commandHandler;
    private final WorkspaceModel workspaceModel;
    public FileTabbedPane(WorkspaceModel workspaceModel, CommandHandler commandHandler) {
        this.workspaceModel = Objects.requireNonNull(workspaceModel);
        this.commandHandler = Objects.requireNonNull(commandHandler);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem closeMenu = new JMenuItem(new AbstractAction("Close") {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            int index = indexAtLocation(e.getX(),e.getY());
                            if(index  == -1 ) return;
                            workspaceModel.removeEditorModel(((EditorPanel)getComponentAt(index)).getEditorModel());
                            removeTabAt(index);
                        }
                    });
                    popupMenu.add(closeMenu);

                    popupMenu.show(FileTabbedPane.this, e.getX(), e.getY());
                }
            }
        });

        workspaceModel.addEditorRemovedListener(this::removeTab);
    }

    public void addTab(EditorModel model) {
        EditorPanel editorPanel = new EditorPanel(model, commandHandler);
        addTab(model.getEditorName(), editorPanel, model);
        model.addFileChangeNotifier(file -> {
           setTitleAt(indexOfComponent(editorPanel), file.getName());
        });
    }

    public void removeTab(EditorModel editorModel) {
        Component tab = tabbedPanels.get(editorModel);

        if(tab != null) {
            remove(tab);
        }
    }

    public void addTab(String title, Icon icon, Component component, String tip, EditorModel editorModel) {
        super.addTab(title, icon, component, tip);
        tabbedPanels.put(editorModel,component);
        int count = this.getTabCount() - 1;
        setTabComponentAt(count, new CloseButtonTab(component, title, icon, editorModel));
    }

    public void addTab(String title, Icon icon, Component component, EditorModel editorModel) {
        addTab(title, icon, component, null, editorModel);
    }

    public void addTab(String title, Component component, EditorModel editorModel) {
        addTab(title, null, component, editorModel);
    }

    /* Button */
    private class CloseButtonTab extends JPanel {
        private final Component tab;
        public CloseButtonTab(final Component tab, String title, Icon icon, EditorModel editorModel) {
            this.tab = tab;
            Objects.requireNonNull(editorModel);
            setOpaque(false);
            MigLayout flowLayout = new MigLayout("", "[][grow]", "[]");
            setLayout(flowLayout);
            JLabel jLabel = new JLabel(title);
            Consumer<String> nameChangeListener = jLabel::setText;
            editorModel.addNameChangeNotifier(nameChangeListener);

            jLabel.setIcon(icon);
            add(jLabel, "cell 0 0");
            JButton button = new JButton();
            button.setBorderPainted(false);
            Icon i = new ImageIcon(Main.DI.getDependency(ImageFactory.class).getImage("clear.png"));
            button.setIcon(i);
            button.addMouseListener(new CloseListener(tab, editorModel, nameChangeListener));
            add(button, "cell 1 0, gapleft push");
        }
    }
    /* ClickListener */
    public class CloseListener extends MouseAdapter {
        private final Component tab;
        private EditorModel editorModel;
        private Consumer<String> nameChangeListener;
        public CloseListener(Component tab, EditorModel editorModel, Consumer<String> nameChangeListener){
            this.tab=Objects.requireNonNull(tab);
            this.editorModel = Objects.requireNonNull(editorModel);
            this.nameChangeListener = Objects.requireNonNull(nameChangeListener);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() instanceof JButton clickedButton){
                JTabbedPane tabbedPane = (JTabbedPane) clickedButton.getParent().getParent().getParent();
                FileTabbedPane.this.workspaceModel.removeEditorModel(editorModel);
                tabbedPane.remove(tab);
                editorModel.removeNameChangeNotifier(nameChangeListener);
            }
        }
    }
}
