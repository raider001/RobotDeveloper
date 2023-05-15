package com.kalynx.robotdeveloper.server;

import com.google.gson.Gson;
import com.kalynx.robotdeveloper.notify.NotifyController;
import com.kalynx.robotdeveloper.notify.NotifyKey;
import com.kalynx.robotdeveloper.server.data.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TestCollectionServer extends NotifyController {
    public static final String EVENT = "event";
    public static final String MESSAGE = "message";
    public static final String CLOSE = "close";
    public static final String END_KEYWORD = "end_keyword";
    public static final String END_SUITE = "end_suite";
    public static final String END_TEST = "end_test";
    public static final String LOG_FILE = "log_file";
    public static final String LOG_MESSAGE = "log_message";
    public static final String OUTPUT_FILE = "output_file";
    public static final String REPORT_FILE = "report_file";
    public static final String START_KEYWORD = "start_keyword";
    public static final String START_TEST = "start_test";
    public static final String START_SUITE = "start_suite";
    private final NotifyKey<CloseEventData> CLOSE_EVENT_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<EndKeyWordEventData> END_KEY_WORD_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<EndSuiteEventData> END_SUITE_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<EndTestEventData> END_TEST_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<LogFileEventData> LOG_FILE_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<LogMessageEventData> LOG_MESSAGE_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<MessageEventData> MESSAGE_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<OutputFileEventData> OUTPUT_FILE_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<ReportFileEventData> REPORT_FILE_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<StartKeywordEventData> START_KEYWORD_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<StartSuiteEventData> START_SUITE_DATA_KEY_LISTENER = new NotifyKey<>();
    private final NotifyKey<StartTestEventData> START_TEST_DATA_KEY_LISTENER = new NotifyKey<>();

    public TestCollectionServer(int port) {
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            try(ServerSocket socket = new ServerSocket(port)) {
                // TODO - Make this stop properly.
                while (true) {
                    Socket client = socket.accept();
                    InputStream input = client.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    Gson gson = new Gson();
                    String line = reader.readLine();
                    Map<String,?> data = gson.fromJson(line, Map.class);
                    if(data == null) continue;
                    switch((String)data.get(EVENT)) {
                        case CLOSE -> notify(CLOSE_EVENT_DATA_KEY_LISTENER, new CloseEventData());
                        case END_KEYWORD -> notify(END_KEY_WORD_DATA_KEY_LISTENER, new EndKeyWordEventData((String) data.get("doc"), (List<String>) data.get("assign"), (List<String>) data.get("tags"), ((Double)data.get("lineno")).intValue(), (String) data.get("source"), (String) data.get("type"),(String) data.get("status"), (String) data.get("starttime"), (String) data.get("endtime"), (Double) data.get("elapsedtime"), (String) data.get("kwname"), (String) data.get("libname"), (List<String>) data.get("args")));
                        case END_SUITE -> notify(END_SUITE_DATA_KEY_LISTENER, new EndSuiteEventData((String) data.get("id"), (String) data.get("longname"), (String) data.get("doc"), (Map<String, Object>) data.get("metadata"), (String) data.get("starttime"), (String) data.get("endtime"), (Double)data.get("elapsedtime"), (String) data.get("status"), (String) data.get("message"), (List<String>) data.get("tests"), (List<String>) data.get("suites"), ((Double) data.get("totaltests")).intValue(), (String) data.get("source"), (String) data.get("statistics")));
                        case END_TEST -> notify(END_TEST_DATA_KEY_LISTENER, new EndTestEventData((String) data.get("id"), (String) data.get("longname"), (String) data.get("doc"), (List<String>) data.get("tags"), ((Double)data.get("lineno")).intValue(), (String) data.get("source"), (String) data.get("starttime"), (String) data.get("endtime"), (Double) data.get("elapsedtime"), (String) data.get("status"), (String) data.get("message"), (String) data.get("template"), (String) data.get("originalname")));
                        case LOG_FILE -> notify(LOG_FILE_DATA_KEY_LISTENER, new LogFileEventData((String) data.get("path")));
                        case LOG_MESSAGE -> notify(LOG_MESSAGE_DATA_KEY_LISTENER, new LogMessageEventData((String) ((Map)data.get("message")).get("timestamp"), (String) ((Map)data.get("message")).get("message"), (String) ((Map)data.get("message")).get("level"), (String) ((Map)data.get("message")).get("html")));
                        case MESSAGE -> notify(MESSAGE_DATA_KEY_LISTENER, new MessageEventData((String) ((Map)data.get("message")).get("timestamp"), (String) ((Map)data.get("message")).get("message"), (String) ((Map)data.get("message")).get("level"), (String) ((Map)data.get("message")).get("html")));
                        case OUTPUT_FILE -> notify(OUTPUT_FILE_DATA_KEY_LISTENER, new OutputFileEventData((String) data.get("path")));
                        case REPORT_FILE -> notify(REPORT_FILE_DATA_KEY_LISTENER, new ReportFileEventData((String) data.get("path")));
                        case START_KEYWORD -> notify(START_KEYWORD_DATA_KEY_LISTENER, new StartKeywordEventData( (String) data.get("doc"), (List<String>) data.get("assign"), (List<String>) data.get("tags"), (int) ((Double)data.get("lineno")).intValue(), (String) data.get("source"), (String) data.get("type"),(String) data.get("status"), (String) data.get("starttime"),  (String) data.get("kwname"), (String) data.get("libname"), (List<String>) data.get("args")));
                        case START_SUITE -> notify(START_SUITE_DATA_KEY_LISTENER, new StartSuiteEventData((String) data.get("id"), (String) data.get("longname"), (String) data.get("doc"), (Map<String, String>) data.get("metadata"), (String) data.get("starttime"), (List<String>) data.get("tests"), (List<String>) data.get("suites"), ((Double)data.get("totaltests")).intValue(), (String) data.get("source")));
                        case START_TEST -> notify(START_TEST_DATA_KEY_LISTENER, new StartTestEventData((String) data.get("id"), (String) data.get("longname"), (String) data.get("doc"), (List<String>) data.get("tags"),  ((Double)data.get("lineno")).intValue(), (String) data.get("source"), (String) data.get("starttime"), (String) data.get("template"), (String) data.get("originalname")));
                        default -> System.out.println("event type:" + data.get(EVENT) + " not supported.");
                    }
                }
            } catch(IOException ex){
                throw new RuntimeException(ex);
            }
        });

    }

    public void addCloseEventDataListener(Consumer<CloseEventData> listener) {
        addListener(CLOSE_EVENT_DATA_KEY_LISTENER, listener);
    }

    public void removeCloseEventDataListener(Consumer<CloseEventData> listener) {
        removeListener(CLOSE_EVENT_DATA_KEY_LISTENER, listener);
    }

    public void addEndKeyWordEventDataListener(Consumer<EndKeyWordEventData> listener) {
        addListener(END_KEY_WORD_DATA_KEY_LISTENER, listener);
    }

    public void removeEndKeyWordEventDataListener(Consumer<EndKeyWordEventData> listener) {
        removeListener(END_KEY_WORD_DATA_KEY_LISTENER, listener);
    }

    public void addEndSuiteEventDataListener(Consumer<EndSuiteEventData> listener) {
        addListener(END_SUITE_DATA_KEY_LISTENER, listener);
    }

    public void removeEndSuiteEventDataListener(Consumer<EndSuiteEventData> listener) {
        removeListener(END_SUITE_DATA_KEY_LISTENER, listener);
    }

    public void addEndTestEventDataListener(Consumer<EndTestEventData> listener) {
        addListener(END_TEST_DATA_KEY_LISTENER, listener);
    }

    public void removeEndTestEventDataListener(Consumer<EndTestEventData> listener) {
        removeListener(END_TEST_DATA_KEY_LISTENER, listener);
    }

    public void addLogFileEventDataListener(Consumer<LogFileEventData> listener) {
        addListener(LOG_FILE_DATA_KEY_LISTENER, listener);
    }

    public void removeLogFileEventDataListener(Consumer<LogFileEventData> listener) {
        removeListener(LOG_FILE_DATA_KEY_LISTENER, listener);
    }

    public void addLogMessageEventDataListener(Consumer<LogMessageEventData> listener) {
        addListener(LOG_MESSAGE_DATA_KEY_LISTENER, listener);
    }

    public void removeLogMessageEventDataListener(Consumer<LogMessageEventData> listener) {
        removeListener(LOG_MESSAGE_DATA_KEY_LISTENER, listener);
    }

    public void addMessageEventDataListener(Consumer<MessageEventData> listener) {
        addListener(MESSAGE_DATA_KEY_LISTENER, listener);
    }

    public void removeMessageEventDataListener(Consumer<MessageEventData> listener) {
        removeListener(MESSAGE_DATA_KEY_LISTENER, listener);
    }

    public void addOutputFileEventDataListener(Consumer<OutputFileEventData> listener) {
        addListener(OUTPUT_FILE_DATA_KEY_LISTENER, listener);
    }

    public void removeOutputFileEventDataListener(Consumer<OutputFileEventData> listener) {
        removeListener(OUTPUT_FILE_DATA_KEY_LISTENER, listener);
    }

    public void addReportFileEventDataListener(Consumer<ReportFileEventData> listener) {
        addListener(REPORT_FILE_DATA_KEY_LISTENER, listener);
    }

    public void removeReportFileEventDataListener(Consumer<ReportFileEventData> listener) {
        removeListener(REPORT_FILE_DATA_KEY_LISTENER, listener);
    }

    public void addStartKeywordEventDataListener(Consumer<StartKeywordEventData> listener) {
        addListener(START_KEYWORD_DATA_KEY_LISTENER, listener);
    }

    public void removeStartKeywordEventDataListener(Consumer<StartKeywordEventData> listener) {
        removeListener(START_KEYWORD_DATA_KEY_LISTENER, listener);
    }

    public void addStartSuiteEventDataListener(Consumer<StartSuiteEventData> listener) {
        addListener(START_SUITE_DATA_KEY_LISTENER, listener);
    }

    public void removeStartSuiteEventDataListener(Consumer<StartSuiteEventData> listener) {
        removeListener(START_SUITE_DATA_KEY_LISTENER, listener);
    }

    public void addStartTestEventDataListener(Consumer<StartTestEventData> listener) {
        addListener(START_TEST_DATA_KEY_LISTENER, listener);
    }

    public void removeStartTestEventDataListener(Consumer<StartTestEventData> listener) {
        removeListener(START_TEST_DATA_KEY_LISTENER, listener);
    }
}
