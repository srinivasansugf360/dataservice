package com.ugf360.dbdemo.springconfig;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ugf360.dbdemo.service.ExcelService;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.configuration.annotation.JobScope;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// import com.ugf360.dbdemo.service.ExcelService;
// import com.ugf360.dbdemo.service.CsvService;
// import com.ugf360.dbdemo.service.JsonService;

@Component
public class CustomItemReader implements ItemReader<Map<String, Object>>, StepExecutionListener {

    @Autowired
    private ExcelService excelService;

    // @Autowired
    // private CsvService csvService;
    //
    // @Autowired
    // private JsonService jsonService;

    // @Autowired
    // private dbService dbService;

    private InputStream inputStream;     // InputStream for Excel file
    private Iterator<Row> rowIterator;   // Iterator to iterate over Excel rows
    private String filePath;             // File path for the Excel file
    private Map<String, Integer> headerMap = new HashMap<>();  // To store the header-to-column index mapping

    @Override
    public void beforeStep(StepExecution stepExecution) {

        // Initialize the file path and open the file
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
        String fileType = jobParameters.getString("fileType");
        filePath = jobParameters.getString("filePath");

        switch (fileType.toLowerCase()) {
            case "excel":
                // ExcelService excelService = new ExcelService();  // Custom reader for Excel

                int sheetIndex = Integer.parseInt(jobParameters.getString("sheetIndex"));
                // System.out.println("XXXX excelService.readExcel(filePath, sheetIndex) B4 CALL");

                // Open the Excel file and read the rows
                this.rowIterator = excelService.readFromExcel(filePath, sheetIndex);

                // Read the first row (header) and map the columns
                if (rowIterator.hasNext()) {
                    Row headerRow = rowIterator.next();  // The first row is the header row
                    for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
                        headerMap.put(headerRow.getCell(i).getStringCellValue(), i);
                    }
                }
                break;
            // case "csv":
            //     return new CustomCsvReader(csvService);      // Custom reader for CSV
            // case "json":
            //     return new CustomJsonReader(jsonService);    // Custom reader for JSON
            // default:
            //     throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    @Override
    public Map<String, Object> read() throws Exception {
        // Read the next row and convert it into a Map
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();  // Read the next data row
            Map<String, Object> rowData = new HashMap<>();

            // For data rows, map values to corresponding header keys
            for (Map.Entry<String, Integer> headerEntry : headerMap.entrySet()) {
                String header = headerEntry.getKey();
                Integer columnIndex = headerEntry.getValue();

                // Get the corresponding data cell using column index
                if (columnIndex < row.getPhysicalNumberOfCells()) {
                    rowData.put(header, row.getCell(columnIndex));
                }
            }
            return rowData;
        }
        // Return null when there are no more rows
        return null;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Clean up resources, if any
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            return ExitStatus.NOOP;
        } catch (Exception e) {
            throw new RuntimeException("Failed to close input stream.", e);
        }
    }
}