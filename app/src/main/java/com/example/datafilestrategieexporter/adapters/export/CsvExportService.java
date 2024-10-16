package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("csvExportService")
public class CsvExportService<T> extends AbstractExport<T> implements IExport<T> {

    private static final Logger logger = LoggerFactory.getLogger(CsvExportService.class);

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final String fileName = "DTO_Export.csv";

    @Override
    public void export(T header, List<T> dataList) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            logger.warn("Nenhum dado para exportar.");
            return;
        }

        String csvContent = generateCsvContent(dataList);
        saveCsvContent(csvContent);
    }

    private String generateCsvContent(List<T> dataList) {
        StringBuilder csvBuilder = new StringBuilder();

        List<Field> orderedFields = getOrderedFields(dataList.get(0).getClass());
        Set<Field> fieldsToIgnore = getFieldsToIgnore(dataList, orderedFields);
        orderedFields.removeAll(fieldsToIgnore);

        String headerLine = orderedFields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        csvBuilder.append(headerLine).append("\n");

        for (T record : dataList) {
            String dataLine = orderedFields.stream()
                    .map(field -> getFieldValueAsString(record, field))
                    .collect(Collectors.joining(","));
            csvBuilder.append(dataLine).append("\n");
        }

        return csvBuilder.toString();
    }

    private String getFieldValueAsString(T record, Field field) {
        try {
            Object value = field.get(record);
            return value != null ? value.toString() : "";
        } catch (IllegalAccessException e) {
            logger.error("Erro ao acessar o campo {}: {}", field.getName(), e.getMessage());
            return "";
        }
    }

    private void saveCsvContent(String csvContent) throws IOException {
        if (saveToFile) {
            saveStringToFile(csvContent, fileName);
            logger.info("Arquivo CSV salvo localmente como {}", fileName);
        } else {
            logger.info("Arquivo CSV precisa ser salvo em um armazenamento remoto.");
            // Implementar lógica de armazenamento remoto aqui
        }
    }
}
