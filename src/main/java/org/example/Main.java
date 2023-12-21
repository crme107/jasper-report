package org.example;

import net.sf.jasperreports.engine.JRException;
import org.example.Export.JRMapExport;
import org.example.Export.JRResultSetExport;
import org.example.Export.JRBeanExport;

import java.io.FileNotFoundException;

public class Main {
    public static final String path = "src/main/resources/";

    public static void main(String[] args) {
        try {
            JRBeanExport.toPDF(path);
            JRResultSetExport.toPDF(path);
            JRMapExport.toPDF(path);
        } catch (JRException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}