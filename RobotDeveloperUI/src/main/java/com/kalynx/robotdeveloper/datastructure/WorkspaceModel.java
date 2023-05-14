package com.kalynx.robotdeveloper.datastructure;

import com.kalynx.robotdeveloper.notify.NotifyController;
import com.kalynx.robotdeveloper.notify.NotifyKey;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class WorkspaceModel {

    Map<File, EditorModel> editorModels = new HashMap<>();

    private final NotifyController workspaceListeners = new NotifyController();
    private static final NotifyKey<EditorModel> EDITOR_ADDED_KEY = new NotifyKey<>();
    private static final NotifyKey<EditorModel> EDITOR_REMOVED_KEY = new NotifyKey<>();

    public EditorModel getEditorModel(File f) {
        return editorModels.get(f);
    }

    public void addEditorModel(EditorModel editorModel) {
        if(!editorModels.containsKey(editorModel.getFile())) {
            editorModels.put(editorModel.getFile(), editorModel);
            workspaceListeners.notify(EDITOR_ADDED_KEY, editorModel);
        }
    }

    public void replaceKey(File oldFile, File replacementFile) {
        EditorModel m = editorModels.get(oldFile);
        editorModels.remove(oldFile);
        editorModels.put(replacementFile, m);
    }

    public List<EditorModel> getEditorModels() {
        return editorModels.values().stream().toList();
    }

    public void removeEditorModel(EditorModel editorModel) {
        editorModels.remove(editorModel.getFile());
        workspaceListeners.notify(EDITOR_REMOVED_KEY, editorModel);
    }

    public void addEditorAddedListener(Consumer<EditorModel> editorModel) {
        workspaceListeners.addListener(EDITOR_ADDED_KEY, editorModel);
    }

    public void removeEditorAddedListener(Consumer<EditorModel> editorModel) {
        workspaceListeners.addListener(EDITOR_ADDED_KEY, editorModel);
    }

    public void addEditorRemovedListener(Consumer<EditorModel> editorModel) {
        workspaceListeners.addListener(EDITOR_REMOVED_KEY, editorModel);
    }

    public void removeEditorRemovedListener(Consumer<EditorModel> editorModel) {
        workspaceListeners.addListener(EDITOR_REMOVED_KEY, editorModel);
    }
}
