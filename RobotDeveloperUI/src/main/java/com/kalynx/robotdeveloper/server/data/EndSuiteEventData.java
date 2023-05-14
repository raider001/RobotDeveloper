package com.kalynx.robotdeveloper.server.data;

import java.util.List;
import java.util.Map;

public record EndSuiteEventData(String id, String longname, String doc, Map<String, Object> metadata, String starttime, String endtime, double elapsedtime, String status, String message, List<String> tests, List<String> suites, int totaltests, String source, String statistics) {
}
