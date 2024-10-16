// FieldFilterService.java
package com.example.datafilestrategieexporter.adapters.export.utils;

import com.example.datafilestrategieexporter.domain.annotations.IgnoreIfEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldFilterService<T> {

    private static final Logger logger = LoggerFactory.getLogger(FieldFilterService.class);

    public Set<Field> getFieldsToIgnore(List<T> dataList, List<Field> fields) {
        Set<Field> fieldsToIgnore = new HashSet<>();
        for (Field field : fields) {
            if (shouldIgnoreField(field) && isEmptyInAllData(dataList, field)) {
                fieldsToIgnore.add(field);
            }
        }
        return fieldsToIgnore;
    }

    private boolean shouldIgnoreField(Field field) {
        return field.isAnnotationPresent(IgnoreIfEmpty.class);
    }

    private boolean isEmptyInAllData(List<T> dataList, Field field) {
        try {
            for (T data : dataList) {
                Object value = field.get(data);
                if (value != null && !value.toString().trim().isEmpty()) {
                    return false;
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("Erro ao acessar valor do campo {}: {}", field.getName(), e.getMessage());
        }
        return true;
    }
}
