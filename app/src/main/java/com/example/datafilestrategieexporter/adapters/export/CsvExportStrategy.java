package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.entities.ExportData;
import com.example.datafilestrategieexporter.domain.interfaces.ExportStrategy;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import com.example.datafilestrategieexporter.usecases.ExportDataUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class CsvExportStrategy extends AbstractExportStrategy implements ExportStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CsvExportStrategy.class);

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final String fileName = "DTO_Export.csv";

    private final ExportDataUseCase exportDataUseCase;

    public CsvExportStrategy(ExportDataUseCase exportDataUseCase) {
        this.exportDataUseCase = exportDataUseCase;
    }

    @Override
    public void export(ExportBuilder exportBuilder) throws IOException {
        // Obter a lista de dados para exportação (ExportData)
        List<ExportData> dataList = exportDataUseCase.prepareExportData();
        StringBuilder csvContent = new StringBuilder();

        // Obter e ordenar os campos com base na anotação @ExcelOrder
        List<Field> orderedFields = super.getOrderedFields(ExportData.class);

        // Adicionar cabeçalho ao CSV (nomes das colunas)
        for (Field field : orderedFields) {
            csvContent.append(field.getName()).append(",");
        }
        csvContent.deleteCharAt(csvContent.length() - 1).append("\n"); // Remover última vírgula e adicionar nova linha

        // Iterar sobre os dados e preencher o CSV
        for (ExportData data : dataList) {
            for (Field field : orderedFields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(data); // Pega o valor do campo via reflexão
                    csvContent.append(value != null ? value.toString() : "").append(",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // Remover a última vírgula e adicionar nova linha
            csvContent.deleteCharAt(csvContent.length() - 1).append("\n");
        }

        // Verificar se deve salvar no sistema de arquivos ou em um bucket
        if (saveToFile) {
            super.saveToFile(csvContent.toString(), fileName);
            logger.info("Excel file saved to local storage as {}", fileName);
        } else {
            logger.info("File needs to be saved to a remote storage");
        }
    }

}
