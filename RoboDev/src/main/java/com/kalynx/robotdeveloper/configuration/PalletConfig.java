package com.kalynx.robotdeveloper.configuration;

import java.awt.*;
import java.io.Serializable;

public class PalletConfig implements Serializable {

    private Color sections;
    private Color newKeyWords;
    private Color setupWords;
    private Color resourceAndLib;
    private Color variables;
    private Color comments;
    private Color importedWords;

    public PalletConfig(Color sections,
                        Color newKeyWords,
                        Color setupWords,
                        Color resourceAndLib,
                        Color variables,
                        Color comments,
                        Color importedWords) {
        this.sections = sections;
        this.newKeyWords = newKeyWords;
        this.setupWords = setupWords;
        this.resourceAndLib = resourceAndLib;
        this.variables = variables;
        this.comments = comments;
        this.importedWords = importedWords;
    }

    public Color getSections() {
        return sections;
    }

    public void setSections(Color sections) {
        this.sections = sections;
    }

    public Color getNewKeyWords() {
        return newKeyWords;
    }

    public void setNewKeyWords(Color newKeyWords) {
        this.newKeyWords = newKeyWords;
    }

    public Color getSetupWords() {
        return setupWords;
    }

    public void setSetupWords(Color setupWords) {
        this.setupWords = setupWords;
    }

    public Color getResourceAndLib() {
        return resourceAndLib;
    }

    public void setResourceAndLib(Color resourceAndLib) {
        this.resourceAndLib = resourceAndLib;
    }

    public Color getVariables() {
        return variables;
    }

    public void setVariables(Color variables) {
        this.variables = variables;
    }

    public Color getComments() {
        return comments;
    }

    public void setComments(Color comments) {
        this.comments = comments;
    }

    public Color getImportedWords() {
        return importedWords;
    }

    public void setImportedWords(Color importedWords) {
        this.importedWords = importedWords;
    }
}
