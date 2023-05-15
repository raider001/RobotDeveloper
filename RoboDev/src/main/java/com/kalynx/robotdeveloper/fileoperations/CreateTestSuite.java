package com.kalynx.robotdeveloper.fileoperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class CreateTestSuite {

    public static Optional<String> create(File file) {
        if(file.exists()) {
            return Optional.of("Directory already exists. Cannot create Test Suite here.");
        }
        try {
        Files.createDirectory(file.toPath());
        File init = Path.of(file.toPath().toString(), "__init__.robot").toFile();
         Files.createFile(init.toPath());
         writeContents(init);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    private static void writeContents(File file) throws IOException {
        String contents = """
                *** Settings ***
                Documentation    Example suite
                Suite Setup      Do Something    ${MESSAGE}
                Force Tags       example
                Library          SomeLibrary
                                
                *** Variables ***
                ${MESSAGE}       Hello, world!
                                
                *** Keywords ***
                Do Something
                    [Arguments]    ${args}
                    Some Keyword    ${arg}
                    Another Keyword
                """;
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(contents);
        writer.close();

    }
}
