package com.kalynx.robotdeveloper.configuration;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public record WorkspaceConfiguration(File lastWorkspace, List<File> openFiles) implements Serializable {
}
