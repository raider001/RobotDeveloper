package com.kalynx.robotdeveloper.datastructure.keywordspec;

import java.util.ArrayList;
import java.util.List;

public class Keyword {
    private String name;
    private List<Argument> args = new ArrayList<>();
    private String doc;
    private String shortDoc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Argument> getArgs() {
        return args;
    }

    public void setArgs(List<Argument> args) {
        this.args = args;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getShortDoc() {
        return shortDoc;
    }

    public void setShortDoc(String shortDoc) {
        this.shortDoc = shortDoc;
    }
}
