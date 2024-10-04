
package com.example.writerdatatofile.adapters.strategies;

import com.example.writerdatatofile.application.usecases.ExportBuilder;
import com.example.writerdatatofile.core.interfaces.ExportStrategy;
import com.example.writerdatatofile.domain.entities.ExportData;
import com.example.writerdatatofile.usecases.ExportDataUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvExportStrategy implements ExportStrategy {

    @Value("${export.saveToFile:false}")
    private boolean saveToFile;

    private final ExportDataUseCase exportDataUseCase;

    public CsvExportStrategy(ExportDataUseCase exportDataUseCase) {
        this.exportDataUseCase = exportDataUseCase;
    }

    @Override
    public void export(ExportBuilder command) throws IOException {
        List<ExportData> dataList = exportDataUseCase.prepareExportData();

        StringBuilder csvContent = new StringBuilder();

        for (ExportData data : dataList) {
            csvContent.append(data.getId()).append(",")
                    .append(data.getName()).append(",")
                    .append(data.getAge() != null ? data.getAge() : "").append(",")
                    .append(data.getEmail() != null ? data.getEmail() : "").append("\n");
        }

        if (saveToFile) {
            // Salvar no sistema de arquivos
            try (FileOutputStream fileOut = new FileOutputStream("DTO_Export.csv")) {
                fileOut.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
            }
        } else {
            // Salvar em algum Bucket
        }
    }
}
