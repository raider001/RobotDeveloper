package com.kalynx.robotdeveloper.xml;

import com.kalynx.robotdeveloper.datastructure.keywordspec.Argument;
import com.kalynx.robotdeveloper.datastructure.keywordspec.Keyword;
import com.kalynx.robotdeveloper.datastructure.keywordspec.KeywordSpec;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentParser {

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    public KeywordSpec parse(File file) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(file);
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        KeywordSpec wordSpec = new KeywordSpec();

        wordSpec.setName(root.getAttribute("name"));
        wordSpec.setType(root.getAttribute("type"));
        wordSpec.setDoc(root.getElementsByTagName("doc").item(0).getTextContent());
        wordSpec.setSource(root.getAttribute("source"));

        NodeList kwNodes = root.getElementsByTagName("kw");
        List<Keyword> keywords = new ArrayList<>();
        for(int i = 0; i < kwNodes.getLength(); i++) {
            Node nNode = kwNodes.item(i);

            if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elmnt = (Element) nNode;
                Keyword keyword = new Keyword();
                keyword.setName(elmnt.getAttribute("name"));
                keyword.setDoc(elmnt.getElementsByTagName("doc").item(0).getTextContent());
                keyword.setShortDoc(elmnt.getElementsByTagName("shortdoc").item(0).getTextContent());

                Node arg =  elmnt.getElementsByTagName("arguments").item(0);
                NodeList args = arg.getChildNodes();
                List<Argument> arguments = new ArrayList<>();
                for(int j = 0; j < args.getLength(); j++) {
                    Node argNode = args.item(j);
                    if(argNode.getNodeType() == Node.ELEMENT_NODE) {
                        Argument argument = new Argument();
                        Element argElmnt = (Element) argNode;
                        argument.setKind(argElmnt.getAttribute("kind"));
                        argument.setRequired(Boolean.parseBoolean(argElmnt.getAttribute("required")));
                        argument.setName(argElmnt.getElementsByTagName("name").item(0).getTextContent());
                        arguments.add(argument);
                    }
                }
                keyword.setArgs(arguments);
                keywords.add(keyword);
            }
        }
        wordSpec.setKeywords(keywords);;

        return wordSpec;
    }
}
