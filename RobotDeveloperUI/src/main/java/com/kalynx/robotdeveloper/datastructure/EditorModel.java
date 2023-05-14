package com.kalynx.robotdeveloper.datastructure;

import com.kalynx.robotdeveloper.notify.NotifyController;
import com.kalynx.robotdeveloper.notify.NotifyKey;

import java.io.File;
import java.util.function.Consumer;

public class EditorModel {

    private static final NotifyKey<String> NAME_CHANGE_KEY = new NotifyKey<>();
    private static final NotifyKey<File> FILE_CHANGE_KEY = new NotifyKey<>();
    private static final NotifyKey<Boolean> EDITED_KEY = new NotifyKey<>();
    private final NotifyController notifyController = new NotifyController();
    private String editorName;
    private File file;
    private boolean isEdited = false;

    public EditorModel(String editorName, File file) {
        this.editorName = editorName;
        this.file = file;
    }

    public void setFile(File file) {
        notifyController.notify(FILE_CHANGE_KEY, file);
        this.file = file;
    }
    public File getFile() {
        return file;
    }

    public void addFileChangeNotifier(Consumer<File> fileChangeListener) {
        notifyController.addListener(FILE_CHANGE_KEY, fileChangeListener);
    }

    public void removeFileChangeNotifier(Consumer<File> fileChangeListener) {
        notifyController.removeListener(FILE_CHANGE_KEY, fileChangeListener);
    }

    public void setEdited(boolean edited) {
        if(edited != isEdited) {
            notifyController.notify(EDITED_KEY, edited);
        }
        isEdited = edited;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void addEditedChangeNotifier(Consumer<Boolean> editedListener) {
        notifyController.addListener(EDITED_KEY, editedListener);
    }

    public void removeEditedChangeNotifier(Consumer<Boolean> editedListener) {
        notifyController.removeListener(EDITED_KEY, editedListener);
    }
    public void setEditorName(String editorName) {
        notifyController.notify(NAME_CHANGE_KEY, editorName);
        this.editorName = editorName;
    }
    public String getEditorName() {
        return editorName;
    }

    public void addNameChangeNotifier(Consumer<String> fileChangeListener) {
        notifyController.addListener(NAME_CHANGE_KEY, fileChangeListener);
    }

    public void removeNameChangeNotifier(Consumer<String> fileChangeListener) {
        notifyController.removeListener(NAME_CHANGE_KEY, fileChangeListener);
    }
}
