// FileService.java
package com.example.datafilestrategieexporter.adapters.export.utils;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    public void saveStringToFile(String content, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }

    public void saveWorkbookToFile(XSSFWorkbook workbook, String fileName) throws IOException {
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName))) {
            workbook.write(out);
        }
    }
}
