package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvExportStrategy<T> extends AbstractExportStrategy<T> implements IExport<T> {

    private static final Logger logger = LoggerFactory.getLogger(CsvExportStrategy.class);

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final String fileName = "DTO_Export.csv";

    private List<T> dataList;

    @Override
    public void export(T header, List<T> data) throws IOException {
        this.dataList = data;

        // Preparar o conteúdo do CSV
        String csvContent = prepareCsvContent();

        // Salvar conteúdo no sistema de arquivos ou enviar para armazenamento remoto
        saveCsvContent(csvContent);
    }

    /**
     * Prepara o conteúdo completo do CSV (cabeçalho e linhas de dados).
     *
     * @return Conteúdo do CSV como String.
     */
    private String prepareCsvContent() {
        StringBuilder csvContent = new StringBuilder();

        // Criar cabeçalho e preencher as colunas
        List<Field> orderedFields = super.getOrderedFields(dataList.get(0).getClass());
        createCsvHeader(csvContent, orderedFields);

        // Criar linhas de dados e preencher o CSV
        createCsvRows(csvContent, orderedFields, dataList);

        return csvContent.toString();
    }

    /**
     * Cria o cabeçalho do CSV com os nomes das colunas.
     */
    private void createCsvHeader(StringBuilder csvContent, List<Field> orderedFields) {
        String header = orderedFields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
        csvContent.append(header).append("\n");
    }

    /**
     * Cria as linhas de dados do CSV usando os valores de cada campo.
     */
    private void createCsvRows(StringBuilder csvContent, List<Field> orderedFields, List<T> dataList) {
        for (T data : dataList) {
            String row = orderedFields.stream()
                    .map(field -> {
                        try {
                            field.setAccessible(true);
                            Object value = field.get(data);
                            return value != null ? value.toString() : "";
                        } catch (IllegalAccessException e) {
                            logger.error("Erro ao acessar valor do campo {}: {}", field.getName(), e.getMessage());
                            return "";
                        }
                    })
                    .collect(Collectors.joining(","));
            csvContent.append(row).append("\n");
        }
    }

    /**
     * Salva o conteúdo CSV no sistema de arquivos ou envia para armazenamento remoto.
     */
    private void saveCsvContent(String csvContent) throws IOException {
        if (saveToFile) {
            super.saveToFile(csvContent, fileName);
            logger.info("CSV file saved to local storage as {}", fileName);
        } else {
            logger.info("CSV file needs to be saved to a remote storage.");
        }
    }

}
