package org.example.Utility;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PdfExporter {
    private static final String FILE_TYPE = ".pdf";

    public static void exportReportToPdf(JasperPrint jasperPrint, String exportPath, String fileName) {
        try {
            File file = new File(exportPath + fileName + FILE_TYPE);
            OutputStream outputStream = new FileOutputStream(file);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            System.out.println(fileName + FILE_TYPE + " has been exported successfully in " + exportPath);
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }
    }
}
