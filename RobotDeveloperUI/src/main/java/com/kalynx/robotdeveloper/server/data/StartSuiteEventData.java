package com.kalynx.robotdeveloper.server.data;

import java.util.List;
import java.util.Map;

public record StartSuiteEventData(String id, String longName, String doc, Map<String, String> metaData, String startTime, List<String> tests, List<String> suites, double totaltests, String source ) {
}
