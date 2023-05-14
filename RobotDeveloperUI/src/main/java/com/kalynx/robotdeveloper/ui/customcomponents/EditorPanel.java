package com.kalynx.robotdeveloper.ui.customcomponents;

import com.kalynx.robotdeveloper.command.CommandHandler;
import com.kalynx.robotdeveloper.datastructure.EditorModel;
import com.kalynx.robotdeveloper.ui.customcomponents.keyreference.KeyReferencePane;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;


public class EditorPanel extends JPanel {
    private final EditorTabbedPane editorTabbedPane;
    private final EditorModel editorModel;
    boolean isImage;
    private final Consumer<Graphics> paintRunnable;
    private BufferedImage image;
    public EditorPanel(EditorModel editorModel, CommandHandler commandHandler) {
        this.editorModel = Objects.requireNonNull(editorModel);
        setLayout(new MigLayout("fill", "0[grow]0", "0[grow]0"));
        isImage = editorModel.getFile().getName().endsWith(".png") || editorModel.getFile().getName().endsWith(".jpg");
        if(isImage) {
            editorTabbedPane = null;
            createImageViewer();
            paintRunnable = g -> {
                g.drawImage(image,0,0, null);
            };
        } else {
            editorTabbedPane  = new EditorTabbedPane(editorModel, commandHandler);
            add(editorTabbedPane, "cell 0 0, grow");
            paintRunnable = super::paint;
        }
    }

    public void clearTestResults() {
        editorTabbedPane.clearTestResults();
    }
    public void save() {
        editorTabbedPane.save();
    }

    private void createImageViewer() {
        try {
            image = ImageIO.read(editorModel.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paint(Graphics g) {
        paintRunnable.accept(g);
    }

    public EditorModel getEditorModel() {
        return editorModel;
    }

}
