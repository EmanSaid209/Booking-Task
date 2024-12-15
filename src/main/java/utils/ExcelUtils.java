package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    public static List<String[]> readTestData(String filePath) {
        List<String[]> testData = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0); // Get first sheet
            int lastRowNum = sheet.getLastRowNum();
            
            // Skip header row and read data
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String[] rowData = new String[3];
                    
                    // Read Location
                    Cell locationCell = row.getCell(0);
                    rowData[0] = getCellValueAsString(locationCell);
                    
                    // Read Check-in Date
                    Cell checkInCell = row.getCell(1);
                    rowData[1] = getCellValueAsString(checkInCell);
                    
                    // Read Check-out Date
                    Cell checkOutCell = row.getCell(2);
                    rowData[2] = getCellValueAsString(checkOutCell);
                    
                    testData.add(rowData);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            // Add default test data if file reading fails
            testData.add(new String[]{
                "Tolip Alexandria",
                "2024-12-25",
                "2024-12-28"
            });
        }
        
        return testData;
    }
    
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getStringCellValue());
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }
}

