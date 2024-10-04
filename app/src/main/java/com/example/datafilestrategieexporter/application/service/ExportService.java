
package com.example.datafilestrategieexporter.application.service;

import com.example.datafilestrategieexporter.application.factories.ExportStrategyFactory;
import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExportService {

    private final ExportStrategyFactory exportStrategyFactory;

    @Autowired
    public ExportService(ExportStrategyFactory exportStrategyFactory) {
        this.exportStrategyFactory = exportStrategyFactory;
    }

    public void executeExportCommand(ExportBuilder command) throws IOException {

        // Verificação para garantir que o command não é nulo
        if (command == null) {
            throw new IllegalArgumentException("O parâmetro command não pode ser nulo.");
        }

        // Verificação para garantir que o tipo no command não é nulo
        if (command.getType() == null) {
            throw new IllegalArgumentException("O tipo de exportação no command não pode ser nulo.");
        }

        exportStrategyFactory.getStrategy(ExportType.valueOf(command.getType()))
                .export(command);
    }
}
