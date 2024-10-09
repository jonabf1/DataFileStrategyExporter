package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.annotations.IgnoreIfEmpty;
import com.example.datafilestrategieexporter.domain.annotations.Order;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractExportStrategy<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractExportStrategy.class);

    /**
     * Obtém e ordena os campos de uma classe com base na anotação @Order.
     *
     * @param clazz Classe a ser inspecionada
     * @return Lista de campos ordenados
     */
    protected List<Field> getOrderedFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .sorted(Comparator.comparingInt(field -> {
                    Order order = field.getAnnotation(Order.class);
                    return (order != null) ? order.value() : Integer.MAX_VALUE;
                }))
                .collect(Collectors.toList());
    }

    /**
     * Identifica quais campos devem ser ignorados com base na anotação @IgnoreIfEmpty e se todos os registros são vazios/nulos.
     *
     * @param dataList     Lista de dados a serem exportados
     * @param orderedFields Lista de campos ordenados
     * @return Conjunto de campos a serem ignorados
     */
    protected Set<Field> getFieldsToIgnore(List<T> dataList, List<Field> orderedFields) {
        return orderedFields.stream()
                .filter(this::shouldIgnoreField)
                .filter(field -> isEmptyInAllData(dataList, field))
                .collect(Collectors.toSet());
    }

    private boolean shouldIgnoreField(Field field) {
        return field.isAnnotationPresent(IgnoreIfEmpty.class);
    }

    private boolean isEmptyInAllData(List<T> dataList, Field field) {
        for (T data : dataList) {
            try {
                field.setAccessible(true);
                Object value = field.get(data);
                if (value != null && !value.toString().trim().isEmpty()) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                logger.error("Erro ao acessar valor do campo {}: {}", field.getName(), e.getMessage());
            }
        }
        return true;
    }

    protected void saveToFile(String content, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            fileOut.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected void saveToFile(XSSFWorkbook workbook, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        }
    }
}
