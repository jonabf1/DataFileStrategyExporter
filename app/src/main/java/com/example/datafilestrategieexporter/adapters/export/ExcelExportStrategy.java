package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.annotations.ExcelColumn;
import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class ExcelExportStrategy<T> extends AbstractExportStrategy<T> implements IExport<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportStrategy.class);
    private static final String SHEET_NAME = "DTO Data";

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final String fileName = "DTO_Export.xlsx";

    private List<T> dataList;

    private T header;

    @Override
    public void export(T header, List<T> data) throws IOException {
        this.dataList = data;
        this.header = header;

        try (XSSFWorkbook workbook = createWorkbook()) {
            XSSFSheet sheet = workbook.getSheet(SHEET_NAME);
            CellStyle headerStyle = createHeaderStyle(workbook);

            int startRowForExportData = createHeaderSection(sheet, headerStyle);
            createExportDataSection(sheet, startRowForExportData);

            saveWorkbook(workbook);
        }
    }

    private XSSFWorkbook createWorkbook() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet(SHEET_NAME);
        return workbook;
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    private int createHeaderSection(XSSFSheet sheet, CellStyle headerStyle) throws IOException {
        if (Objects.isNull(header)) {
            return 0;
        }
        printHeaderWithLabels(sheet, header, headerStyle);
        return header.getClass().getDeclaredFields().length + 2;
    }

    private void createExportDataSection(XSSFSheet sheet, int startRow) {
        List<Field> fields = super.getOrderedFields(dataList.get(0).getClass());

        Set<Field> fieldsToIgnore = super.getFieldsToIgnore(dataList, fields);
        fields.removeAll(fieldsToIgnore);

        Map<Integer, Field> fieldIndexMapping = new HashMap<>();
        createHeaderRowWithStyle(sheet, fields, fieldIndexMapping, sheet.getWorkbook(), startRow);

        fillDataRows(sheet, fieldIndexMapping, startRow + 1, dataList);
    }

    private void createHeaderRowWithStyle(XSSFSheet sheet, List<Field> fields, Map<Integer, Field> fieldIndexMapping, XSSFWorkbook workbook, int startRow) {
        Row headerRow = sheet.createRow(startRow);
        int columnIndex = 0;

        CellStyle headerStyle = createHeaderStyle(workbook);

        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                Cell cell = headerRow.createCell(columnIndex);
                cell.setCellValue(annotation.name());
                cell.setCellStyle(headerStyle);
                fieldIndexMapping.put(columnIndex, field);
                columnIndex++;
            }
        }
    }

    private void printHeaderWithLabels(XSSFSheet sheet, T header, CellStyle style) {
        try {
            Field[] fields = header.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    field.setAccessible(true);
                    ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);

                    Row row = sheet.createRow(i);
                    Cell labelCell = row.createCell(0);
                    labelCell.setCellValue(columnAnnotation.name());
                    labelCell.setCellStyle(style);

                    Cell valueCell = row.createCell(1);
                    valueCell.setCellValue(field.get(header).toString());
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("Erro ao acessar campo do Header: {}", e.getMessage());
        }
    }

    private void fillDataRows(XSSFSheet sheet, Map<Integer, Field> fieldIndexMapping, int startRow, List<T> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(startRow + i);
            fillRowWithData(row, fieldIndexMapping, dataList.get(i));
        }
    }

    private void fillRowWithData(Row row, Map<Integer, Field> fieldIndexMapping, T data) {
        fieldIndexMapping.forEach((columnIndex, field) -> {
            try {
                field.setAccessible(true);
                Object value = field.get(data);
                Cell cell = row.createCell(columnIndex);
                setCellValue(cell, value);
            } catch (IllegalAccessException e) {
                logger.error("Erro ao acessar valor do campo {}: {}", field.getName(), e.getMessage());
            }
        });
    }

    private void saveWorkbook(XSSFWorkbook workbook) throws IOException {
        if (saveToFile) {
            super.saveToFile(workbook, fileName);
            logger.info("Arquivo Excel salvo localmente como {}", fileName);
        } else {
            logger.info("Arquivo precisa ser salvo em um armazenamento remoto");
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }
    }
}
