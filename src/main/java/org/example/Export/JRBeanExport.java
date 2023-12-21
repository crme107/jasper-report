package org.example.Export;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.Model.Holiday;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.Main.PATH;
import static org.example.Main.XML_DATA_SOURCE;
import static org.example.Main.JASPER_TEMPLATE;

public class JRBeanExport {
    private static final String REPORT_TITLE = "Holidays";
    private static final String EXPORT_PATH = PATH + "pdf/";
    private static final String EXPORTED_PDF_FILENAME = "HolidaysJRBeanReport";

    public static void toPDF() throws JRException, FileNotFoundException {
        List<Holiday> holidays = parseXML();
        JRDataSource holidaysDataSource = new JRBeanCollectionDataSource(holidays);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("TITLE", REPORT_TITLE);

        JasperReport jasperDesign = JasperCompileManager.compileReport(PATH + JASPER_TEMPLATE);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter, holidaysDataSource);

        PdfExporter.exportReportToPdf(jasperPrint, EXPORT_PATH, EXPORTED_PDF_FILENAME);
    }

    private static List<Holiday> parseXML() {
        try {
            List<Holiday> holidays = new ArrayList<>();

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(PATH + XML_DATA_SOURCE));
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

            return holidays;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}