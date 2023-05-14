package com.kalynx.robotdeveloper.server.data;

import java.util.List;

public record StartTestEventData(String id, String longname, String doc, List<String> tags, int lineno, String source, String starttime, String template, String originalname) {
}
