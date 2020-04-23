/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.StringWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author user
 */
public class DemoSAX extends DefaultHandler {

    private boolean inTag;
    private boolean isFound;
    private boolean inElement;
    private Attributes attrList;
    private String name;
    private StringWriter sw;
    
    public DemoSAX(String name) {
        super();
        this.name = name;
        sw = new StringWriter();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        if (inTag) {
            String massage = new String(ch, start, length).trim();
            if (!massage.isEmpty()) {
                sw.append(" Значение: ");
                sw.append(massage).append(".");
                sw.append("<BR>");
            } else if (inElement) {
                sw.append(" Нет значения.");
                sw.append("<BR>");
            }
            inElement = false;
        }
    }

    public String getSw() {
        if (isFound) {
            return "<ol>" + sw.append("</ol>").toString();
        } else {
            return "Тега не найдено.";
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase(name)) {
            inTag = true;
            isFound = true;
            sw.append("<li>");
        }
        if (inTag) {
            attrList = attributes;
            sw.append(qName);
            String allAttr = listAttr().trim();
            if (!allAttr.isEmpty()) {
                sw.append(": Атрибуты: " + allAttr + ".");
            } else {
                sw.append(": Атрибутов нет.");
            }
            inElement = true;
        }
    }

    private String listAttr() {
        StringBuilder sb = new StringBuilder();
        int n = attrList.getLength();
        for (int i = 0; i < n; i++) {
            String name = attrList.getQName(i);
            String value = attrList.getValue(i);
            sb.append(name).append(" = ").append(value);
            if (i < n - 1) {
                sb.append("; ");
            }
        }
        return sb.toString();
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(name)) {
            inTag = false;
            
            if(inElement){
            sw.append(" Нет значения.");    
            }
            
            sw.append("</li><BR>");
        }
    }

}
