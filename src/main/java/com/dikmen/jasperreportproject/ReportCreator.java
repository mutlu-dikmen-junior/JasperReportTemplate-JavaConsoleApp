/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dikmen.jasperreportproject;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseTextField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 *
 * @author mutlu.dikmen
 */
public class ReportCreator {

    public static void main(String[] args) {

        try {
            String filePath = ".\\src\\main\\resources\\Student.jrxml";

            Subject subject1 = new Subject("Nesne Yönelimli programlama", 80);
            Subject subject2 = new Subject("Veri Yapıları ve Algoritmalar", 70);
            Subject subject3 = new Subject("Veri Tabanı Sistemleri", 50);
            Subject subject4 = new Subject("İşletim Sistemleri", 40);
            Subject subject5 = new Subject("Yazılım Mühendisliği Temelleri", 60);

            List<Subject> subjects = new ArrayList<Subject>();

            subjects.add(subject1);
            subjects.add(subject2);
            subjects.add(subject3);
            subjects.add(subject4);
            subjects.add(subject5);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjects);

            JRBeanCollectionDataSource chartDataSource = new JRBeanCollectionDataSource(subjects);

//            Create parameter data
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("studentName", "Mutlu DİKMEN");
            parameters.put("tableData", dataSource);
            parameters.put("subReport", getSubReport());
            parameters.put("subDataSource", getSubReportDataSource());
            parameters.put("subParameters", getSubParameters());

//            Compile jasper report file            
            JasperReport report = JasperCompileManager.compileReport(filePath);

//            Manipulating parameters with java code 
            JRBaseTextField textField = (JRBaseTextField) report.getTitle().getElementByKey("name");
            textField.setForecolor(Color.yellow);

//            With the help of jasperreports.properties file we can set Encodings globally. Otherwise we must to set every property we want to change 
//            textField.setPdfEncoding("Cp1254");

//            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

//            We must fill the report with chart data source if we want to show charts in the report
            JasperPrint print = JasperFillManager.fillReport(report, parameters, chartDataSource);

//            Export as a pdf file
            JasperExportManager.exportReportToPdfFile(print, ".\\src\\main\\resources\\reports\\StudentReport.pdf");

//            Export as an xlsx file
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
            new FileOutputStream(new File(".\\src\\main\\resources\\reports\\StudentReport.xlsx"))));
            exporter.exportReport();
            
            System.out.println("Reports are created.");

        } catch (Exception e) {
            System.out.println("An exception occurred while creating the report");
             e.printStackTrace();
        }
    }

    private static JasperReport getSubReport() throws JRException {
        
        String filePath = ".\\src\\main\\resources\\SubReport.jrxml";

        return JasperCompileManager.compileReport(filePath);

    }

    private static JRBeanCollectionDataSource getSubReportDataSource() {
        
        Student student1 = new Student(1L, "Aziz", "SANCAR", "Kızılay", "Ankara");
        Student student2 = new Student(2L, "Canan", "KARATAY", "Taksim", "İstanbul");

        List<Student> students = new ArrayList<Student>();
        students.add(student1);
        students.add(student2);

        return new JRBeanCollectionDataSource(students);
    }

    private static Map<String, Object> getSubParameters() {
        
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("studentName", "Dikmen Mutlu");

        return parameters;
    }

}
