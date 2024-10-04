
package com.example.writerdatatofile.application.service;

import com.example.writerdatatofile.adapters.factories.ExportStrategyFactory;
import com.example.writerdatatofile.application.usecases.ExportBuilder;
import com.example.writerdatatofile.domain.enums.ExportType;
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
        exportStrategyFactory.getStrategy(ExportType.valueOf(command.getType()))
                .export(command);
    }
}
