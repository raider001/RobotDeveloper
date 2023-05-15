package com.kalynx.robotdeveloper;

import com.formdev.flatlaf.FlatDarkLaf;
import com.kalynx.lwdi.DependencyInjectionException;
import com.kalynx.lwdi.DependencyInjector;
import com.kalynx.robotdeveloper.command.CommandHandler;
import com.kalynx.robotdeveloper.datastructure.LibraryResourceModel;
import com.kalynx.robotdeveloper.datastructure.WorkingDirectoryModel;
import com.kalynx.robotdeveloper.datastructure.WorkspaceModel;
import com.kalynx.robotdeveloper.graphic.ImageFactory;
import com.kalynx.robotdeveloper.server.TestCollectionServer;
import com.kalynx.robotdeveloper.ui.MainWindow;
import com.kalynx.robotdeveloper.ui.customcomponents.WorkspacePanel;
import com.kalynx.robotdeveloper.ui.customcomponents.keyreference.KeyReferencePane;
import com.kalynx.robotdeveloper.ui.dialogs.TextColorDialog;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;

public class Main {

    public static DependencyInjector DI = new DependencyInjector();
    private static MainWindow frame;
    public static void main(String... args) throws DependencyInjectionException, IOException {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            FlatDarkLaf.setup();
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        TextColorDialog.getInstance();
        Path p = Paths.get(".", "pythonlistener");

        if(!Files.exists(p)) {
            Files.createDirectory(p);
        }

        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("pythonlistener/listener.py");
            p = Paths.get(p.toString(), "listener.py");
            Files.copy(is, p, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {

        }

        DI.inject(WorkingDirectoryModel.class);
        DI.inject(WorkspaceModel.class);
        DI.inject(LibraryResourceModel.class);
        TestCollectionServer server = new TestCollectionServer(9999);
        DI.add(server);
        DI.inject(CommandHandler.class);
        DI.inject(ImageFactory.class);
        DI.inject(KeyReferencePane.class);
        DI.inject(WorkspacePanel.class);
        frame = DI.inject(MainWindow.class);
    }
}
