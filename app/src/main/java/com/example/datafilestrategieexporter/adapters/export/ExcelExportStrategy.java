package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.annotations.ExcelColumn;
import com.example.datafilestrategieexporter.domain.entities.ExportData;
import com.example.datafilestrategieexporter.domain.entities.Header;
import com.example.datafilestrategieexporter.domain.interfaces.ExportStrategy;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import com.example.datafilestrategieexporter.usecases.ExportDataUseCase;
import com.example.datafilestrategieexporter.usecases.HeaderUseCase;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ExcelExportStrategy extends AbstractExportStrategy implements ExportStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportStrategy.class);
    private static final String SHEET_NAME = "DTO Data";

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final String fileName = "DTO_Export.xlsx";

    private final ExportDataUseCase exportDataUseCase;
    private final HeaderUseCase headerUseCase;

    public ExcelExportStrategy(ExportDataUseCase exportDataUseCase, HeaderUseCase headerUseCase) {
        this.exportDataUseCase = exportDataUseCase;
        this.headerUseCase = headerUseCase;
    }

    @Override
    public void export(ExportBuilder exportBuilder) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

            // Criar estilo para os títulos
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Imprimir valores de Header (name do @ExcelColumn no lado esquerdo e valor no lado direito)
            Header header = headerUseCase.prepareHeader();
            printHeaderWithLabels(sheet, header, headerStyle);

            // Adicionar **apenas uma linha** de espaçamento antes de ExportData
            int startRowForExportData = header.getClass().getDeclaredFields().length + 2;

            // Obter campos ordenados e verificar quais devem ser ignorados
            List<Field> fields = super.getOrderedFields(ExportData.class);
            Set<Field> fieldsToIgnore = super.getFieldsToIgnore(exportDataUseCase.prepareExportData(), fields);

            // Remover campos que devem ser ignorados
            fields.removeAll(fieldsToIgnore);

            // Criar cabeçalhos e imprimir valores
            Map<Integer, Field> fieldIndexMapping = new HashMap<>();
            createHeaderRowWithStyle(sheet, fields, fieldIndexMapping, workbook, startRowForExportData);

            // Preencher dados de ExportData, ignorando campos específicos
            fillDataRows(sheet, fieldIndexMapping, startRowForExportData + 1); // Começar da linha seguinte ao cabeçalho de ExportData

            // Ajustar largura das colunas
            autoSizeColumns(sheet);

            if (saveToFile) {
                super.saveToFile(workbook, fileName);
                logger.info("Excel file saved to local storage as {}", fileName);
            } else {
                logger.info("File needs to be saved to a remote storage");
            }
        }
    }

    /**
     * Imprime a entidade Header com o name do @ExcelColumn na coluna esquerda e o valor na coluna direita.
     */
    private void printHeaderWithLabels(XSSFSheet sheet, Header header, CellStyle style) {
        try {
            Field[] fields = header.getClass().getDeclaredFields();
            int rowIndex = 0;

            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    field.setAccessible(true);
                    ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);

                    Row row = sheet.createRow(rowIndex++); // Nova linha para cada campo de Header

                    // Primeira coluna: nome do @ExcelColumn
                    Cell labelCell = row.createCell(0);
                    labelCell.setCellValue(columnAnnotation.name());
                    labelCell.setCellStyle(style);

                    // Segunda coluna: valor do campo
                    Cell valueCell = row.createCell(1);
                    valueCell.setCellValue(field.get(header).toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void createHeaderRowWithStyle(XSSFSheet sheet, List<Field> fields, Map<Integer, Field> fieldIndexMapping, XSSFWorkbook workbook, int startRow) {
        Row headerRow = sheet.createRow(startRow);
        int columnIndex = 0;

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

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

    private void fillDataRows(XSSFSheet sheet, Map<Integer, Field> fieldIndexMapping, int startRow) {
        List<ExportData> dataList = exportDataUseCase.prepareExportData();

        for (int i = 0; i < dataList.size(); i++) {
            Row row = sheet.createRow(startRow + i); // Começar a partir da linha definida
            int finalI = i;
            fieldIndexMapping.forEach((columnIndex, field) -> {
                field.setAccessible(true);
                try {
                    Object value = field.get(dataList.get(finalI));
                    Cell cell = row.createCell(columnIndex);
                    setCellValue(cell, value);
                } catch (IllegalAccessException e) {
                    logger.error("Error accessing field {}: {}", field.getName(), e.getMessage());
                }
            });
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
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

}
