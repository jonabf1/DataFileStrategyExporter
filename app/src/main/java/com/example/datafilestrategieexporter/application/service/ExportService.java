
package com.example.datafilestrategieexporter.application.service;

import com.example.datafilestrategieexporter.application.factories.ExportStrategyFactory;
import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExportService {

    private final ExportStrategyFactory exportStrategyFactory;

    public ExportService(ExportStrategyFactory exportStrategyFactory) {
        this.exportStrategyFactory = exportStrategyFactory;
    }

    public void executeExportBuilder(ExportBuilder builder) throws IOException {

        // Verificação para garantir que o builder não é nulo
        if (builder == null) {
            throw new IllegalArgumentException("O parâmetro builder não pode ser nulo.");
        }

        // Verificação para garantir que o tipo no builder não é nulo
        if (builder.getType() == null) {
            throw new IllegalArgumentException("O tipo de exportação no builder não pode ser nulo.");
        }

        exportStrategyFactory.getStrategy(ExportType.valueOf(builder.getType()))
                .export(builder);
    }

}
