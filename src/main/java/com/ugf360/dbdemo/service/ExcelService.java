package com.ugf360.dbdemo.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Component
public class ExcelService implements IOservice {

    private InputStream inputStream;
    private Iterator<Row> rowIterator;

    // Method to initialize and read the Excel file
    // public ExcelService()
    // {
    //   System.out.println("XXXX ExcelService instantiated");
    // }
    
    public Iterator<Row> readFromSource(String filePath, int sheetIndex) {
        // Open the Excel file and initialize rowIterator
        try {
            this.inputStream = new FileInputStream(filePath);  // Initialize InputStream with the file path
            Workbook workbook = new XSSFWorkbook(inputStream);  // Read the Excel file using Apache POI
            Sheet sheet = workbook.getSheetAt(sheetIndex);        // Get the first sheet of the workbook
            this.rowIterator = sheet.iterator();                // Initialize the row iterator for reading rows
            System.out.println("File successfully opened, ready to read rows.");
            return this.rowIterator;  // Return the rowIterator to the caller
        } catch (IOException e) {
            throw new RuntimeException("Failed to open file: " + filePath, e);
        }
    }

    public void writeToSource(String filePath, int sheetIndex) {
      return null;
    }
    
    public void getHeader() {

        
    }
    // Method to close the InputStream after reading (optional but good practice)
    // public void close() throws IOException {
    //     if (inputStream != null) {
    //         inputStream.close();
    //     }
    // }
}