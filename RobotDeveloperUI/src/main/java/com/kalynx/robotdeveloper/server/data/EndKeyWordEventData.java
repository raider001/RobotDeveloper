package com.kalynx.robotdeveloper.server.data;

import java.util.List;

public record EndKeyWordEventData(String doc, List<String> assign, List<String> tags, int lineno, String source, String type, String status, String starttime, String endtime, double elapsedtime, String kwname, String libname, List<String> args) {
}
