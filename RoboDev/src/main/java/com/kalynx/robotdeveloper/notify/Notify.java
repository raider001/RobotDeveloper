package com.kalynx.robotdeveloper.notify;

import java.util.function.Consumer;

public interface Notify {

    <T> void addListener(NotifyKey<T> key, Consumer<T> consumer);
    <T> void removeListener(NotifyKey<T> key, Consumer<T> consumer);
}
