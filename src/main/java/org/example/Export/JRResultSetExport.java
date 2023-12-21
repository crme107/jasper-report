package org.example.Export;

import net.sf.jasperreports.engine.*;
import org.example.Utility.ConnectionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JRResultSetExport {
    public static final String QUERY_HOLIDAYS = "select holiday_name as name, holiday_date as date, country from holiday";

    public static void toPDF(String path) throws FileNotFoundException, JRException {
        JasperReport jasperDesign = JasperCompileManager.compileReport(path + "HolidaysReport.jrxml");
        Map<String, Object> parameter = new HashMap<>();

        JasperPrint jasperPrint;
        try (Connection connection = ConnectionManager.createConnection()) {
            JRDataSource holidaysDataSource = new JRResultSetDataSource(queryHolidays(connection));
            parameter.put("TITLE", "Holidays");
            jasperPrint = JasperFillManager.fillReport(jasperDesign, parameter, holidaysDataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        File file = new File(path + "PDF/HolidaysJRResultSetReport.pdf");
        OutputStream outputStream = new FileOutputStream(file);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        System.out.println("'HolidaysJRResultSetReport.pdf' was exported successfully in the 'resources' directory.");
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
