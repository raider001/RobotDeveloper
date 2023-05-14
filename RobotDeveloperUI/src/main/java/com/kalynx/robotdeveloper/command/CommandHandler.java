package com.kalynx.robotdeveloper.command;

import com.kalynx.robotdeveloper.Main;
import com.kalynx.robotdeveloper.datastructure.LibraryResourceModel;
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

public class CommandHandler {

    private static final Path docLoc = Paths.get("doc");
    private Process activeProcess;
    public void runTest(File testLocation, File testFile) {

        if(activeProcess == null || !activeProcess.isAlive()) {
            try {
                String listenerPath = new File(".").getCanonicalPath() + "\\pythonlistener\\listener.py";
                ProcessBuilder process = new ProcessBuilder("robot", "--listener", listenerPath, testFile.toString());
                String path = System.getenv().get("Path");
                process.environment().put("Path", path);
                process.environment().put("DISABLE_SIKULI_LOG", "yes");
                process.redirectError(ProcessBuilder.Redirect.INHERIT);
                process.redirectInput(ProcessBuilder.Redirect.INHERIT);
                process.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                activeProcess = process.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void generateDoc(ResourceType doc) {
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
            String path = System.getenv().get("Path");
            processBuilder.environment().put("Path", path);
            processBuilder.environment().put("DISABLE_SIKULI_LOG", "yes");
            processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
            processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);

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
}
