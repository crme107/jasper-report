package org.example;

import net.sf.jasperreports.engine.JRException;
import org.example.Export.JRMapExport;
import org.example.Export.JRResultSetExport;
import org.example.Export.JRBeanExport;

import java.io.FileNotFoundException;

public class Main {
    public static final String PATH = "src/main/resources/";
    public static final String XML_DATA_SOURCE = "Holidays.xml";
    public static final String JASPER_TEMPLATE = "HolidaysReport.jrxml";

    public static void main(String[] args) {
        try {
            JRBeanExport.toPDF();
            JRResultSetExport.toPDF();
            JRMapExport.toPDF();
        } catch (JRException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}