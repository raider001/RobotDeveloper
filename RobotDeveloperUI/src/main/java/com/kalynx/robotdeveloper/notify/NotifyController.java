package com.kalynx.robotdeveloper.notify;

import java.util.*;
import java.util.function.Consumer;

import static javax.swing.UIManager.get;

public class NotifyController implements Notify {
    private Map<NotifyKey<?>, Set<Consumer<?>>> listeners = new HashMap<>();
    @Override
    public <T> void addListener(NotifyKey<T> key, Consumer<T> consumer) {
        if(!listeners.containsKey(key)) {
            listeners.put(key, new HashSet<>());
        }
        listeners.get(key).add(consumer);
    }

    @Override
    public <T> void removeListener(NotifyKey<T> key, Consumer<T> consumer) {
        Set<?> consumers = listeners.get(key);
        if(consumers != null) {
            consumers.remove(consumer);
        }
    }

    public <T> void notify(NotifyKey<T> key, T valueChange) {
        // Is protected by the listener interfaces and key. The NotifyKey ensures safety with the exception
        // for when key equivallency being bruteforced.
        Set<T> listenerList = (Set<T>) listeners.get(key);
        if(listenerList != null) {
            listenerList.parallelStream().forEach(consumer -> ((Consumer<T>) consumer).accept(valueChange));
        }


    }


}
