package org.example.Export;

import net.sf.jasperreports.engine.*;
import org.example.Utility.ConnectionManager;
import org.example.Utility.PdfExporter;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.example.Main.PATH;
import static org.example.Main.JASPER_TEMPLATE;

public class JRResultSetExport {
    private static final String REPORT_TITLE = "Holidays";
    private static final String EXPORT_PATH = PATH + "pdf/";
    private static final String EXPORTED_PDF_FILENAME = "HolidaysJRResultSetReport";

    public static final String QUERY_HOLIDAYS = "select holiday_name as name, holiday_date as date, country from holiday";

    public static void toPDF() throws FileNotFoundException, JRException {
        JasperReport jasperDesign = JasperCompileManager.compileReport(PATH + JASPER_TEMPLATE);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("TITLE", REPORT_TITLE);

        try (Connection connection = ConnectionManager.createConnection()) {
            JRDataSource holidaysDataSource = new JRResultSetDataSource(queryHolidays(connection));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter, holidaysDataSource);

            PdfExporter.exportReportToPdf(jasperPrint, EXPORT_PATH, EXPORTED_PDF_FILENAME);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ResultSet queryHolidays(Connection connection) {
        try {
            PreparedStatement prepareStatement = connection.prepareStatement(QUERY_HOLIDAYS);
            return prepareStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
