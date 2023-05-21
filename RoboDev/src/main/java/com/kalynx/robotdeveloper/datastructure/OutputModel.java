package com.kalynx.robotdeveloper.datastructure;

import com.kalynx.robotdeveloper.notify.NotifyController;
import com.kalynx.robotdeveloper.notify.NotifyKey;

import java.io.File;
import java.util.function.Consumer;

public class OutputModel {
    private File f;

    NotifyKey<File> INPUT_STREAM_KEY = new NotifyKey<>();
    private final NotifyController controller = new NotifyController();

    public void setVal(File f) {
        this.f = f;
        controller.notify(INPUT_STREAM_KEY, f);
    }

    public void addListener(Consumer<File> listener) {
        controller.addListener(INPUT_STREAM_KEY, listener);
    }

    public void removeListener(Consumer<File> listener) {
        controller.removeListener(INPUT_STREAM_KEY, listener);
    }
}
