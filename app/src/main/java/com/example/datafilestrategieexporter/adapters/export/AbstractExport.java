package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.adapters.export.utils.FieldFilterService;
import com.example.datafilestrategieexporter.adapters.export.utils.FieldOrderingService;
import com.example.datafilestrategieexporter.adapters.export.utils.FileService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public abstract class AbstractExport<T> {

    protected final FieldOrderingService fieldOrderingService = new FieldOrderingService();
    protected final FieldFilterService<T> fieldFilterService = new FieldFilterService<>();
    protected final FileService fileService = new FileService();

    protected List<Field> getOrderedFields(Class<?> clazz) {
        return fieldOrderingService.getOrderedFields(clazz);
    }

    protected Set<Field> getFieldsToIgnore(List<T> dataList, List<Field> fields) {
        return fieldFilterService.getFieldsToIgnore(dataList, fields);
    }

    protected void saveStringToFile(String content, String fileName) throws IOException {
        fileService.saveStringToFile(content, fileName);
    }

    protected void saveWorkbookToFile(XSSFWorkbook workbook, String fileName) throws IOException {
        fileService.saveWorkbookToFile(workbook, fileName);
    }
}
