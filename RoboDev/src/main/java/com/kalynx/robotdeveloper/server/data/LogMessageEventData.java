package com.kalynx.robotdeveloper.server.data;

public record LogMessageEventData(String timestamp, String message, String level, String html) {
}
