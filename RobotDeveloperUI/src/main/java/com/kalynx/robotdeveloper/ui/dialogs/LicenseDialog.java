package com.kalynx.robotdeveloper.ui.dialogs;

import javax.swing.*;

public class LicenseDialog extends JDialog {

    private static LicenseDialog INSTANCE;

    public synchronized static LicenseDialog getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LicenseDialog();
        }
        return INSTANCE;
    }

    private LicenseDialog() {
        setTitle("Licensing");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        add(textArea);
        setResizable(false);
        textArea.append("""
                Copyright 2023 Daniel Furnell
                                
                Licensed under the Apache License, Version 2.0 (the "License");
                you may not use this file except in compliance with the License.
                You may obtain a copy of the License at
                                
                    http://www.apache.org/licenses/LICENSE-2.0
                                
                Unless required by applicable law or agreed to in writing, software
                distributed under the License is distributed on an "AS IS" BASIS,
                WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                See the License for the specific language governing permissions and
                limitations under the License.
                """);
        pack();
    }
}
