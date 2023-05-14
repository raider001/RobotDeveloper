package com.kalynx.robotdeveloper.datastructure;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ModelUpdater<T> {

    private T model;

    public void updateModel(T model) {
        if(this.model != null && this.model.equals(model)) {
            return;
        }
        this.model = model;
        modelListeners.stream().forEach(consumer -> consumer.accept(model));
    }

    public T getModel() {
        return model;
    }

    private final Set<Consumer<T>> modelListeners = new HashSet<>();
    public void addModelObserver(Consumer<T> modelObserver) {
        modelListeners.add(modelObserver);
    }

    public void removeModelObserver(Consumer<T> modelObserver) {
        modelListeners.remove(modelObserver);
    }
}
