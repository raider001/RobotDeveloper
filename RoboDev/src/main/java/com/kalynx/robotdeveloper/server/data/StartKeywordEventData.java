package com.kalynx.robotdeveloper.server.data;

import java.util.List;

public record StartKeywordEventData(String doc, List<String> assign, List<String> tags, int lineno, String source, String type, String status, String starttime, String kwname, String libname, List<String> args) {
}
