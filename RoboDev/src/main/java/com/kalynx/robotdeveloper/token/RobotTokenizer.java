package com.kalynx.robotdeveloper.token;

import com.kalynx.robotdeveloper.ui.dialogs.TextColorDialog;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RobotTokenizer {

    private static RobotTokenizer INSTANCE;

    public static final String SECTION_TOKEN_REGEX = "\\*{3} (Settings|Variables|Test Cases|Tasks|Comments|Keywords) \\*{3}";
    public static final String TEST_OR_KEYWORD = "(\\n[A-Za-z][A-Za-z0-9 ]*[A-Za-z0-9] {2}|\\n[A-Za-z][A-Za-z0-9 ]*[A-Za-z0-9]\\n)";
    public static final String RESOURCE_LIBRARY_KEYWORD = "\\n(Resource|Library) *[A-Za-z0-9//.]*";
    public static final String SPECIAL = "\\[(Arguments|Documentation|Teardown|Tags|Setup|Teardown|Template|Test Setup|Test Teardown)\\]";
    public static final String VARIABLES = "[$|&|@]\\{[A-Za-z0-9_-]*\\}";
    public static final String COMMENT = "#[A-Za-z0-9 !@#$%^&*()-_+=\\{\\[\\]\\};:'\\\"|,.<>/?`~]*";

    private List<TokenChecker> tokens = new ArrayList<>();
    private Set<String> dynamicTokens = new HashSet<>();
    private TokenChecker dynamicToken;
    public synchronized static RobotTokenizer getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new RobotTokenizer();
        }
        return INSTANCE;
    }
    private RobotTokenizer() {
        tokens.add(new TokenChecker(SECTION_TOKEN_REGEX, TextColorDialog.SECTION_ATTRIBUTES));
        tokens.add(new TokenChecker(VARIABLES, TextColorDialog.VARIABLES_ATTRIBUTES));
        tokens.add(new TokenChecker(TEST_OR_KEYWORD, TextColorDialog.TEST_OR_KEYWORD_ATTRIBUTES));
        tokens.add(new TokenChecker(RESOURCE_LIBRARY_KEYWORD, TextColorDialog.RESOURCE_LIBRARY_ATTRIBUTES));
        tokens.add(new TokenChecker(SPECIAL, TextColorDialog.SPECIAL_ATTRIBUTES));
        tokens.add(new TokenChecker(COMMENT, TextColorDialog.COMMENT_ATTRIBUTES));
        dynamicToken = new TokenChecker("", TextColorDialog.IMPORTED_KEYWORD_ATTRIBUTES);
        tokens.add(dynamicToken);
    }

    public List<TokenChecker> getTokens() {
        return Collections.unmodifiableList(tokens);
    }


    public void addDynamicTokens(String... tokens) {
        for(String token: tokens) {
            dynamicTokens.add(token);
        }
        updateRegex();
    }

    public void removeDynamicTokens(String... tokens) {
        for(String token: tokens) {
            dynamicTokens.remove(token);
        }
        updateRegex();
    }

    private void updateRegex() {
        String newRegex = "  (";
        String[] arr = dynamicTokens.toArray(new String[0]);
        for (int i = 0; i < arr.length; i++) {

            if(i == arr.length - 1) {
                newRegex += arr[i];
            } else {
                newRegex += arr[i] + "|";
            }
        }
        newRegex +=")( {2,}|\n|\\z)";
        dynamicToken.setTokenRegex(newRegex);
    }

    public class TokenChecker {
        private String regex;
        private SimpleAttributeSet attrSet;

        public TokenChecker(String regex, SimpleAttributeSet attrs) {
            this.regex = Objects.requireNonNull(regex);
            attrSet = Objects.requireNonNull(attrs);
        }

        public void setTokenRegex(String regex) {
            this.regex = regex;
        }

        public SimpleAttributeSet getAttrSet() {
            return attrSet;
        }
        public String getRegex() {
            return regex;
        }
    }
}
