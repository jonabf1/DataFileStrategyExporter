
package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.annotations.ExcelColumn;
import com.example.datafilestrategieexporter.domain.annotations.IgnoreIfEmpty;
import com.example.datafilestrategieexporter.domain.annotations.Order;
import com.example.datafilestrategieexporter.domain.entities.ExportData;
import com.example.datafilestrategieexporter.domain.interfaces.ExportStrategy;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import com.example.datafilestrategieexporter.usecases.ExportDataUseCase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class ExcelExportStrategy implements ExportStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportStrategy.class);
    private static final String EXCEL_FILE_NAME = "DTO_Export.xlsx";
    private static final String SHEET_NAME = "DTO Data";

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final ExportDataUseCase exportDataUseCase;

    public ExcelExportStrategy(ExportDataUseCase exportDataUseCase) {
        this.exportDataUseCase = exportDataUseCase;
    }

    @Override
    public void export(ExportBuilder exportBuilder) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

            List<Field> fields = getOrderedFields(ExportData.class);
            Map<Integer, Field> fieldIndexMapping = new HashMap<>();
            Set<Integer> emptyColumns = new HashSet<>();

            createHeaderRow(sheet, fields, fieldIndexMapping);
            fillDataRows(sheet, fieldIndexMapping, emptyColumns);
            adjustAndRemoveEmptyColumns(sheet, emptyColumns);

            if (saveToFile) {
                saveToFile(workbook);
                logger.info("Excel file saved to local storage as {}", EXCEL_FILE_NAME);
            } else{
                // Salvar em algum Bucket
            }
        }
    }

    private List<Field> getOrderedFields(Class<?> clazz) {
        Field[] allFields = clazz.getDeclaredFields();
        List<Field> fieldList = Arrays.asList(allFields);

        // Ordenar os campos com base no valor da anotação `ExcelOrder`
        fieldList.sort(Comparator.comparingInt(field -> {
            Order order = field.getAnnotation(Order.class);
            return (order != null) ? order.value() : Integer.MAX_VALUE;
        }));

        return fieldList;
    }

    private void createHeaderRow(XSSFSheet sheet, List<Field> fields, Map<Integer, Field> fieldIndexMapping) {
        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;

        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                Cell cell = headerRow.createCell(columnIndex);
                cell.setCellValue(annotation.name());
                fieldIndexMapping.put(columnIndex, field);
                columnIndex++;
            }
        }
    }

    private void fillDataRows(XSSFSheet sheet, Map<Integer, Field> fieldIndexMapping, Set<Integer> emptyColumns) {
        List<ExportData> dataList = exportDataUseCase.prepareExportData();

        int rowIndex = 1;

        for (ExportData dto : dataList) {
            Row row = sheet.createRow(rowIndex++);
            fillRowWithData(dto, row, fieldIndexMapping, emptyColumns);
        }
    }

    private void fillRowWithData(ExportData dto, Row row, Map<Integer, Field> fieldIndexMapping, Set<Integer> emptyColumns) {
        for (Map.Entry<Integer, Field> entry : fieldIndexMapping.entrySet()) {
            Integer columnIndex = entry.getKey();
            Field field = entry.getValue();
            field.setAccessible(true);

            try {
                Object value = field.get(dto);
                Cell cell = row.createCell(columnIndex);
                setCellValue(cell, value);

                if (field.isAnnotationPresent(IgnoreIfEmpty.class)) {
                    updateEmptyColumnSet(columnIndex, value, emptyColumns);
                }
            } catch (IllegalAccessException e) {
                logger.error("Error accessing field {}: {}", field.getName(), e.getMessage());
            }
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }
    }

    private void updateEmptyColumnSet(Integer columnIndex, Object value, Set<Integer> emptyColumns) {
        if (value == null || value.toString().trim().isEmpty()) {
            emptyColumns.add(columnIndex);
        } else {
            emptyColumns.remove(columnIndex);
        }
    }

    private void adjustAndRemoveEmptyColumns(XSSFSheet sheet, Set<Integer> emptyColumns) {
        if (!emptyColumns.isEmpty()) {
            removeEmptyColumns(sheet, emptyColumns);
        }
        autoSizeColumns(sheet);
    }

    private void removeEmptyColumns(XSSFSheet sheet, Set<Integer> emptyColumns) {
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                for (Integer columnIndex : emptyColumns) {
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        row.removeCell(cell);
                    }
                }
            }
        }
    }

    private void autoSizeColumns(XSSFSheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
                sheet.autoSizeColumn(columnIndex);
            }
        }
    }

    private void saveToFile(XSSFWorkbook workbook) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(EXCEL_FILE_NAME)) {
            workbook.write(fileOut);
        }
    }
}
