package com.kalynx.robotdeveloper.configuration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigWriter {

    public static void writeConfig(Object o) {
        createConfigDirectory();
        Path p = Paths.get("./config/", o.getClass().getSimpleName());
        File file = p.toAbsolutePath().toFile();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(o);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T loadConfig(Class<T> clz) {
        createConfigDirectory();
        Path p = Paths.get("./config/", clz.getSimpleName());
        File file = p.toAbsolutePath().toFile();
        if(!file.exists()) return null;
        try(FileInputStream fis = new FileInputStream(file); ObjectInputStream on = new ObjectInputStream(fis)) {
            return (T) on.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private static void createConfigDirectory() {
        Path p = Paths.get("./", "config");
        if(!p.toFile().exists()) {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
