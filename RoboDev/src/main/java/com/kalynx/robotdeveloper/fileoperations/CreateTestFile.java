package com.kalynx.robotdeveloper.fileoperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class CreateTestFile {

    public static Optional<String> create(File file) {
        if(file.exists()) {
            return Optional.of("File already exists. Cannot create Test here.");
        }
        try {
            Files.createFile(file.toPath());
            writeContents(file);
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
                
                *** Test Cases ***
                Test Something
                    [Documentation]   Tests something
                    [Arguments]    ${args}
                    Some Keyword    ${arg}
                    Another Keyword    
                               
                *** Keywords ***
                Do Something
                    [Documentation] Does something
                    [Arguments]    ${args}
                    Some Keyword    ${arg}
                    Another Keyword
                """;
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(contents);
        writer.close();

    }
}
