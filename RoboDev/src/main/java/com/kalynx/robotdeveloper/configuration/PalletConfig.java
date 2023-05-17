package com.kalynx.robotdeveloper.configuration;

import java.awt.*;
import java.io.Serializable;

public class PalletConfig implements Serializable {

    private Color def;
    private Color sections;
    private Color newKeyWords;
    private Color setupWords;
    private Color resourceAndLib;
    private Color variables;
    private Color comments;
    private Color importedWords;
    private int fontSize;

    public PalletConfig(Color def,
                        Color sections,
                        Color newKeyWords,
                        Color setupWords,
                        Color resourceAndLib,
                        Color variables,
                        Color comments,
                        Color importedWords, int fontSize) {
        this.def = def;
        this.sections = sections;
        this.newKeyWords = newKeyWords;
        this.setupWords = setupWords;
        this.resourceAndLib = resourceAndLib;
        this.variables = variables;
        this.comments = comments;
        this.importedWords = importedWords;
        this.fontSize = fontSize;
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

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Color getDef() {
        return def;
    }

    public void setDef(Color def) {
        this.def = def;
    }
}
