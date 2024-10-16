package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.annotations.ExcelColumn;
import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service("excelExportService")
public class ExcelExportService<T> extends AbstractExport<T> implements IExport<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
    private static final String SHEET_NAME = "Dados DTO";

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final String fileName = "DTO_Export.xlsx";

    @Override
    public void export(T header, List<T> dataList) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            logger.warn("Nenhum dado para exportar.");
            return;
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
            CellStyle headerStyle = createHeaderCellStyle(workbook);

            int dataStartRow = 0;
            if (header != null) {
                dataStartRow = createHeaderSection(sheet, header, headerStyle);
            }

            createDataSection(sheet, dataList, dataStartRow, headerStyle);
            saveWorkbook(workbook);
        }
    }

    private int createHeaderSection(XSSFSheet sheet, T headerData, CellStyle headerStyle) {
        Field[] fields = headerData.getClass().getDeclaredFields();
        int rowIndex = 0;

        for (Field field : fields) {
            String columnName = getExcelColumnName(field);
            if (columnName != null) {
                Row row = sheet.createRow(rowIndex++);
                createCell(row, 0, columnName, headerStyle);
                Object value = getFieldValue(headerData, field);
                createCell(row, 1, value != null ? value.toString() : "", null);
            }
        }
        return rowIndex + 1; // Adiciona espaço entre o cabeçalho e os dados
    }

    private void createDataSection(XSSFSheet sheet, List<T> dataList, int startRow, CellStyle headerStyle) {
        if (dataList.isEmpty()) return;

        List<Field> orderedFields = getOrderedFields(dataList.get(0).getClass());
        Set<Field> fieldsToIgnore = getFieldsToIgnore(dataList, orderedFields);
        orderedFields.removeAll(fieldsToIgnore);

        Map<Integer, Field> fieldIndexMap = createFieldIndexMap(orderedFields);
        createDataHeaderRow(sheet, fieldIndexMap, startRow, headerStyle);
        populateDataRows(sheet, dataList, fieldIndexMap, startRow + 1);
    }

    private void createDataHeaderRow(XSSFSheet sheet, Map<Integer, Field> fieldIndexMap, int rowIndex, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(rowIndex);
        fieldIndexMap.forEach((columnIndex, field) -> {
            String columnName = getExcelColumnName(field);
            if (columnName == null) {
                columnName = field.getName();
            }
            createCell(headerRow, columnIndex, columnName, headerStyle);
        });
    }

    private void populateDataRows(XSSFSheet sheet, List<T> dataList, Map<Integer, Field> fieldIndexMap, int startRow) {
        int rowIndex = startRow;
        for (T record : dataList) {
            Row row = sheet.createRow(rowIndex++);
            for (Map.Entry<Integer, Field> entry : fieldIndexMap.entrySet()) {
                int columnIndex = entry.getKey();
                Field field = entry.getValue();
                Object value = getFieldValue(record, field);
                createCell(row, columnIndex, value, null);
            }
        }
    }

    private Map<Integer, Field> createFieldIndexMap(List<Field> fields) {
        Map<Integer, Field> fieldIndexMap = new LinkedHashMap<>();
        int index = 0;
        for (Field field : fields) {
            fieldIndexMap.put(index++, field);
        }
        return fieldIndexMap;
    }

    private String getExcelColumnName(Field field) {
        if (field.isAnnotationPresent(ExcelColumn.class)) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            return annotation.name();
        }
        return null;
    }

    private Object getFieldValue(T record, Field field) {
        try {
            field.setAccessible(true); // Torna o campo acessível
            return field.get(record);
        } catch (IllegalAccessException e) {
            logger.error("Erro ao acessar o campo {}: {}", field.getName(), e.getMessage());
            return null;
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void createCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            CellStyle dateStyle = row.getSheet().getWorkbook().createCellStyle();
            CreationHelper creationHelper = row.getSheet().getWorkbook().getCreationHelper();
            dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            cell.setCellStyle(dateStyle);
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private void saveWorkbook(XSSFWorkbook workbook) throws IOException {
        if (saveToFile) {
            saveWorkbookToFile(workbook, fileName);
            logger.info("Arquivo Excel salvo localmente como {}", fileName);
        } else {
            logger.info("Arquivo Excel precisa ser salvo em um armazenamento remoto.");
            // Implementar lógica de armazenamento remoto aqui
        }
    }
}
