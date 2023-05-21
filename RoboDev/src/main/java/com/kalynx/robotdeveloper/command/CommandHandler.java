package com.kalynx.robotdeveloper.command;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.datastructure.LibraryResourceModel;
import com.kalynx.robotdeveloper.datastructure.OutputModel;
import com.kalynx.robotdeveloper.datastructure.ResourceType;
import com.kalynx.robotdeveloper.datastructure.keywordspec.KeywordSpec;
import com.kalynx.robotdeveloper.token.RobotTokenizer;
import com.kalynx.robotdeveloper.xml.DocumentParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CommandHandler {

    private static final Path docLoc = Paths.get("doc");
    private Process activeProcess;
    public synchronized void runTest(File testFile) {

        Executors.newSingleThreadExecutor().submit(() -> {
            if(activeProcess == null || !activeProcess.isAlive()) {
                try {
                    String listenerPath = new File(".").getCanonicalPath() + "/pythonlistener/listener.py";
                    ProcessBuilder processBuilder = new ProcessBuilder("robot", "--listener", listenerPath, testFile.toString());
                    setPath(processBuilder);
                    processBuilder.environment().put("DISABLE_SIKULI_LOG", "yes");
                    processBuilder.inheritIO();
                    activeProcess = processBuilder.start();
                    activeProcess.waitFor();
                    Main.DI.getDependency(OutputModel.class).setVal(new File("output.xml"));
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public synchronized void generateDoc(ResourceType doc) {
        if(!docLoc.toFile().exists()) {
            try {
                Files.createDirectory(docLoc);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String loc = doc.resource();
        Path fullPath;
        if(doc.isPath()) {
            fullPath =Path.of(docLoc.toString(), new File(loc).getName() + ".xml");
        } else {
            fullPath = Path.of(docLoc.toString(), doc.resource() + ".xml");
        }

        if(!fullPath.toFile().exists()) {

            ProcessBuilder processBuilder = new ProcessBuilder("libdoc", loc, fullPath.toString());
            setPath(processBuilder);
            processBuilder.environment().put("DISABLE_SIKULI_LOG", "yes");
            processBuilder.inheritIO();

            try {
                Process p = processBuilder.start();
                p.waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        DocumentParser docParser = new DocumentParser();
        KeywordSpec spec = null;
        if(fullPath.toAbsolutePath().toFile().exists()) {
            try {
                spec = docParser.parse(fullPath.toAbsolutePath().toFile());
            } catch (ParserConfigurationException | XPathExpressionException | IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        }

        LibraryResourceModel model = Main.DI.getDependency(LibraryResourceModel.class);
        if(model.getLibrary(doc.resource()) == null && spec != null) {
            model.addLibrary(spec);
        }

        RobotTokenizer.getInstance().addDynamicTokens(model.getAllKeyWords().toArray(String[]::new));
    }

    private void setPath(ProcessBuilder processBuilder) {
        String os = System.getProperty("os.name");
        String path;
        if(os.startsWith("Windows")) {
            path = System.getenv().get("Path");
            processBuilder.environment().put("Path", path);
        } else {
            path = System.getenv().get("PATH");
            processBuilder.environment().put("PATH", path);
        }
    }

    private class ExecStuff {

        private Object id;
        Runnable r;
        ExecStuff(Object id, Runnable r) {
            this.id = id;
            this.r = r;
        }

        public void run() {
            r.run();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ExecStuff execStuff)) return false;
            return id.equals(execStuff.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
