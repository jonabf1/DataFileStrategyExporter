// FieldOrderingService.java
package com.example.datafilestrategieexporter.adapters.export.utils;

import com.example.datafilestrategieexporter.domain.annotations.Order;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FieldOrderingService {

    private static final Map<Class<?>, List<Field>> orderedFieldsCache = new ConcurrentHashMap<>();

    public List<Field> getOrderedFields(Class<?> clazz) {
        return orderedFieldsCache.computeIfAbsent(clazz, this::computeOrderedFields);
    }

    private List<Field> computeOrderedFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<Integer, Field> orderedFieldsMap = new TreeMap<>();
        List<Field> unorderedFields = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            Order orderAnnotation = field.getAnnotation(Order.class);
            if (orderAnnotation != null) {
                orderedFieldsMap.put(orderAnnotation.value(), field);
            } else {
                unorderedFields.add(field);
            }
        }

        List<Field> orderedFields = new ArrayList<>(orderedFieldsMap.values());
        orderedFields.addAll(unorderedFields);
        return orderedFields;
    }
}
