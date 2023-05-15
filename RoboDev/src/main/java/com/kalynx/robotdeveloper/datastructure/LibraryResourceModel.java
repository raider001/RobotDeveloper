package com.kalynx.robotdeveloper.datastructure;

import com.kalynx.robotdeveloper.datastructure.keywordspec.Keyword;
import com.kalynx.robotdeveloper.datastructure.keywordspec.KeywordSpec;
import com.kalynx.robotdeveloper.notify.NotifyController;
import com.kalynx.robotdeveloper.notify.NotifyKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LibraryResourceModel {

    private final NotifyKey<KeywordSpec> LIBRARY_ADDED_KEYWORD = new NotifyKey<>();
    private final NotifyKey<KeywordSpec> LIBRARY_REMOVED_KEYWORD = new NotifyKey<>();
    private final NotifyController notifyController = new NotifyController();
    private Map<String, KeywordSpec> libraries = new HashMap<>();

    private Object objLock = new Object();

    public void addLibrary(KeywordSpec keywordSpec) {
        synchronized (objLock) {
            if (libraries.containsKey(keywordSpec.getName())) return;
            libraries.put(keywordSpec.getName(), keywordSpec);
            notifyController.notify(LIBRARY_ADDED_KEYWORD, keywordSpec);
        }
    }

    public void removeLibrary(KeywordSpec keywordSpec) {
        libraries.remove(keywordSpec.getName(), keywordSpec);
        notifyController.notify(LIBRARY_REMOVED_KEYWORD, keywordSpec);
    }

    public void addLibraryAddedNotifier(Consumer<KeywordSpec> listener) {
        notifyController.addListener(LIBRARY_ADDED_KEYWORD, listener);
    }

    public void removeLibraryAddedNotifier(Consumer<KeywordSpec> listener) {
        notifyController.addListener(LIBRARY_ADDED_KEYWORD, listener);
    }

    public void addLibraryRemovedNotifier(Consumer<KeywordSpec> listener) {
        notifyController.addListener(LIBRARY_REMOVED_KEYWORD, listener);
    }

    public void removeLibraryRemovedNotifier(Consumer<KeywordSpec> listener) {
        notifyController.addListener(LIBRARY_REMOVED_KEYWORD, listener);
    }

    public void clearLibraries() {
        synchronized (objLock) {
            for (String lib : getAllLibraries()) {
                KeywordSpec spec = libraries.get(lib);
                removeLibrary(spec);
            }
        }
    }

    public KeywordSpec getLibrary(String library) {
        return libraries.get(library);
    }

    public List<String> getAllKeyWords() {
        List<String> res = new ArrayList<>();
        for(KeywordSpec spec: libraries.values()) {
            res.addAll(getLibraryKeyWords(spec));
        }
        return res;
    }

    public List<String> getAllLibraries() {
       return libraries.keySet().stream().toList();
    }

    public List<String> getLibraryKeyWords(KeywordSpec spec) {
        return spec.getKeywords().parallelStream().map(Keyword::getName).toList();
    }
}
