package com.kalynx.robotdeveloper;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme;
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

import javax.swing.*;

public class Main {

    public static DependencyInjector DI = new DependencyInjector();
    private static MainWindow frame;
    public static void main(String... args) throws DependencyInjectionException {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            FlatXcodeDarkIJTheme.setup();
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
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
