/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author user
 */
public class DemoDOM {

    private String name;
    private StringWriter sw;
    private String uri;
    private Document doc;
    private boolean flag;

    public DemoDOM(String name, String uri) {
        this.name = name;
        this.uri = uri;
        sw = new StringWriter();
    }

    public void init() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(uri);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DemoDOM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String find() {
        NodeList matchList = doc.getElementsByTagName(name);
        if (matchList.getLength() == 0) {
            return "Тега не найдено.";
        } else {
            sw.append("<ol>");
            for (int i = 0; i < matchList.getLength(); i++) {
                Node note = matchList.item(i); ///список всех совпадений
                sw.append("<li>"); ///имя совпадения
                recursive(note);
                sw.append("<BR>");
                sw.append("</li>");
            }
            sw.append("</ol>");
        }
        return sw.toString();
    }

    private void getAttr(Node note) {
        if (note.hasAttributes()) {
            NamedNodeMap attrList = note.getAttributes();
            sw.append(": Атрибуты: ");
            for (int j = 0; j < attrList.getLength(); j++) {
                Node attr = attrList.item(j);
                String attrName = attr.getNodeName();
                String attrValue = attr.getNodeValue();
                sw.append(attrName + "=" + attrValue);
                if (j < attrList.getLength() - 1) {
                    sw.append("; ");
                } else {
                    sw.append(". ");
                }
            }
        } else {
            sw.append(": Атрибутов нет.");
        }
    }

    private void recursive(Node note) {
        if (note.getNodeType() == Node.TEXT_NODE) {
            String text = note.getNodeValue().replace("\n", "").trim();
            if (flag) {
                if (!text.isEmpty()) {
                    // sw.append("+++");
                    sw.append(" Значение: " + text + ".");
                } else {
                    sw.append(" Нет значения.");
                }
                flag = false;
                sw.append("<BR>");
            }
        } else {
            flag = true;
            String nodeName = note.getNodeName();
            sw.append(nodeName);
            getAttr(note);
            
            if (note.hasChildNodes()) {
                
               
                NodeList childList = note.getChildNodes();  ///список детей 
                for (int j = 0; j < childList.getLength(); j++) {
                    Node child = childList.item(j);  //ребетенок
                    recursive(child);
                }
            }else {
                sw.append(" Нет значения.");
                sw.append("<BR>");
                flag = false;
            
            }
        }
    }
}
