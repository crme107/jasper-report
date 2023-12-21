package org.example.Export;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.Model.Holiday;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JRBeanExport {
    public static void toPDF(String path) throws JRException, FileNotFoundException {
        List<Holiday> holidays = parseXML(path + "Holidays.xml");
        JRDataSource holidaysDataSource = new JRBeanCollectionDataSource(holidays);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("TITLE", "Holidays");

        JasperReport jasperDesign = JasperCompileManager.compileReport(path + "HolidaysReport.jrxml");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter, holidaysDataSource);

        File file = new File(path + "PDF/HolidaysJRBeanReport.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        System.out.println("'HolidaysJRBeanReport.pdf' was exported successfully in the 'resources' directory.");
    }

    private static List<Holiday> parseXML(String path) {
        List<Holiday> holidays = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(path));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("holydays");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    holidays.add(new Holiday(
                            element.getElementsByTagName("NAME").item(0).getTextContent(),
                            element.getElementsByTagName("DATE").item(0).getTextContent(),
                            element.getElementsByTagName("COUNTRY").item(0).getTextContent()
                    ));
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return holidays;
    }
}
