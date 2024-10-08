
package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.annotations.IgnoreIfEmpty;
import com.example.datafilestrategieexporter.domain.annotations.Order;
import com.example.datafilestrategieexporter.domain.entities.ExportData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class AbstractExportStrategy {

    /**
     * Obtém e ordena os campos de uma classe com base na anotação @Order.
     *
     * @param clazz Classe a ser inspecionada
     * @return Lista de campos ordenados
     */
    protected List<Field> getOrderedFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>(Arrays.asList(fields));

        // Ordena os campos com base no valor da anotação @Order
        fieldList.sort(Comparator.comparingInt(field -> {
            Order order = field.getAnnotation(Order.class);
            return (order != null) ? order.value() : Integer.MAX_VALUE;
        }));

        return fieldList;
    }

    /**
     * Identifica quais campos devem ser ignorados com base na anotação @IgnoreIfEmpty e se todos os registros são vazios/nulos.
     *
     * @param dataList     Lista de dados a serem exportados
     * @param orderedFields Lista de campos ordenados
     * @return Conjunto de campos a serem ignorados
     */
    protected Set<Field> getFieldsToIgnore(List<ExportData> dataList, List<Field> orderedFields) {
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

    /**
     * Salva conteúdo em um arquivo local.
     *
     * @param content Conteúdo a ser salvo
     * @param fileName Nome do arquivo
     */
    protected void saveToFile(String content, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            fileOut.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Salva um workbook do Excel em um arquivo local.
     *
     * @param workbook Workbook do Excel a ser salvo
     * @param fileName Nome do arquivo
     */
    protected void saveToFile(XSSFWorkbook workbook, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        }
    }

}
