package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.annotations.IgnoreIfEmpty;
import com.example.datafilestrategieexporter.domain.annotations.Order;
import com.example.datafilestrategieexporter.domain.entities.ExportData;
import com.example.datafilestrategieexporter.domain.interfaces.ExportStrategy;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import com.example.datafilestrategieexporter.usecases.ExportDataUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvExportStrategy implements ExportStrategy {

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

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
        List<Field> orderedFields = getOrderedFields(ExportData.class);

        // Determinar quais campos devem ser removidos com base na anotação @IgnoreIfEmpty
        Set<Field> fieldsToIgnore = getFieldsToIgnore(dataList, orderedFields);

        // Remover campos que devem ser ignorados
        orderedFields.removeAll(fieldsToIgnore);

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
            try (FileOutputStream fileOut = new FileOutputStream("DTO_Export.csv")) {
                fileOut.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
            }
        } else {
            System.out.println("Arquivo deve ser salvo em um bucket");
        }
    }

    /**
     * Obtém e ordena os campos de uma classe com base na anotação @ExcelOrder.
     *
     * @param clazz Classe a ser inspecionada
     * @return Lista de campos ordenados
     */
    private List<Field> getOrderedFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();

        for (Field field : fields) {
            fieldList.add(field);
        }

        // Ordena os campos com base no valor da anotação @ExcelOrder
        fieldList.sort(Comparator.comparingInt(field -> {
            Order order = field.getAnnotation(Order.class);
            return (order != null) ? order.value() : Integer.MAX_VALUE;
        }));

        return fieldList;
    }

    /**
     * Identifica quais campos devem ser ignorados no CSV com base na anotação @IgnoreIfEmpty e se todos os registros são vazios/nulos.
     *
     * @param dataList     Lista de dados a serem exportados
     * @param orderedFields Lista de campos ordenados
     * @return Conjunto de campos a serem ignorados
     */
    private Set<Field> getFieldsToIgnore(List<ExportData> dataList, List<Field> orderedFields) {
        Set<Field> fieldsToIgnore = new HashSet<>();

        for (Field field : orderedFields) {
            // Verificar se o campo possui a anotação @IgnoreIfEmpty
            if (field.isAnnotationPresent(IgnoreIfEmpty.class)) {
                boolean shouldIgnore = true;

                for (ExportData data : dataList) {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(data);
                        // Verificar se o valor é nulo ou vazio (string)
                        if (value != null && !value.toString().trim().isEmpty()) {
                            shouldIgnore = false;
                            break; // Se encontrar um valor não nulo e não vazio, a coluna não deve ser ignorada
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (shouldIgnore) {
                    fieldsToIgnore.add(field);
                }
            }
        }
        return fieldsToIgnore;
    }

}
