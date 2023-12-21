package org.example.Export;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.example.Utility.PdfExporter;
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

import static org.example.Main.*;

public class JRMapExport {
    private static final String REPORT_TITLE = "Holidays";
    private static final String EXPORT_PATH = PATH + "pdf/";
    private static final String EXPORTED_PDF_FILENAME = "HolidaysJRMapReport";

    public static void toPDF() throws JRException, FileNotFoundException {
        List<Map<String, ?>> holidays = parseXML();
        JRDataSource holidaysDataSource = new JRMapCollectionDataSource(holidays);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("TITLE", REPORT_TITLE);

        JasperReport jasperDesign = JasperCompileManager.compileReport(PATH + JASPER_TEMPLATE);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter, holidaysDataSource);

        PdfExporter.exportReportToPdf(jasperPrint, EXPORT_PATH, EXPORTED_PDF_FILENAME);
    }

    private static List<Map<String, ?>> parseXML() {
        try {
            List<Map<String, ?>> holidayMap = new ArrayList<>();

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(PATH + XML_DATA_SOURCE));
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

            return holidayMap;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
