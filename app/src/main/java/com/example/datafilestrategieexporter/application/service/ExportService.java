
package com.example.datafilestrategieexporter.application.service;

import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.enums.FlowType;
import com.example.datafilestrategieexporter.domain.interfaces.IExportFactory;
import com.example.datafilestrategieexporter.domain.interfaces.IFlow;
import com.example.datafilestrategieexporter.usecases.ExportBuilderDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExportService {

    private final IExportFactory exportFactory;

    private IFlow flow;

    public ExportService(IExportFactory exportFactory, IFlow flow) {
        this.exportFactory = exportFactory;
        this.flow = flow;
    }

    public void executeExportBuilder(ExportBuilderDto builder) throws IOException {

        // Verificação para garantir que o builder não é nulo
        if (builder == null) {
            throw new IllegalArgumentException("O parâmetro builder não pode ser nulo.");
        }

        // Verificação para garantir que o tipo no builder não é nulo
        if (builder.getType() == null) {
            throw new IllegalArgumentException("O tipo de exportação no builder não pode ser nulo.");
        }

        ExportType exportType = ExportType.valueOf(builder.getType());
        FlowType flowType = FlowType.valueOf(builder.getFlow());

        exportFactory.getStrategy(exportType)
                .export(null, flow.produce(flowType));
    }

}
