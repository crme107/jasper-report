package org.example.Export;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class JRMapExport {
    public static void toPDF(String path) throws JRException, FileNotFoundException {
        List<Map<String, ?>> holidays = parseXML(path + "Holidays.xml");
        JRDataSource holidaysDataSource = new JRMapCollectionDataSource(holidays);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("TITLE", "Holidays");

        JasperReport jasperDesign = JasperCompileManager.compileReport(path + "HolidaysReport.jrxml");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter, holidaysDataSource);

        File file = new File(path + "PDF/HolidaysJRMapReport.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        System.out.println("'HolidaysJRMapReport.pdf' was exported successfully in the 'resources' directory.");
    }

    private static List<Map<String, ?>> parseXML(String path) {
        List<Map<String, ?>> holidayMap = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("holydays");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Map<String, String> map = new HashMap<>();
                    map.put("NAME", element.getElementsByTagName("NAME").item(0).getTextContent());
                    map.put("DATE", element.getElementsByTagName("DATE").item(0).getTextContent());
                    map.put("COUNTRY", element.getElementsByTagName("COUNTRY").item(0).getTextContent());
                    holidayMap.add(map);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return holidayMap;
    }
}
