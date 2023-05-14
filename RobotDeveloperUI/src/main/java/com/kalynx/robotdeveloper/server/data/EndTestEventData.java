package com.kalynx.robotdeveloper.server.data;

import java.util.List;

public record EndTestEventData(String id, String longname, String doc, List<String> tags, int lineno, String source, String starttime, String endtime, double elapsedtime, String status, String message, String template, String originalname) {
}
