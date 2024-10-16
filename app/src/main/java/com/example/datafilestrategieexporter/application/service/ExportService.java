package com.example.datafilestrategieexporter.application.service;

import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.enums.FlowType;
import com.example.datafilestrategieexporter.domain.interfaces.HeaderUseCase;
import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import com.example.datafilestrategieexporter.domain.interfaces.IExportFactory;
import com.example.datafilestrategieexporter.domain.interfaces.IFlow;
import com.example.datafilestrategieexporter.usecases.ExportBuilderDto;
import com.example.datafilestrategieexporter.usecases.HeaderUseCaseImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExportService<T> {

    private final IExportFactory<T> exportFactory;
    private final IFlow<T> flow;

    // temporario: remover
    private final HeaderUseCase headerUseCase;

    public ExportService(IExportFactory<T> exportFactory, IFlow<T> flow, HeaderUseCase headerUseCase) {
        this.exportFactory = exportFactory;
        this.flow = flow;
        this.headerUseCase = headerUseCase;
    }

    public void executeExportBuilder(ExportBuilderDto builder) throws IOException {

        if (builder == null) {
            throw new IllegalArgumentException("O parâmetro builder não pode ser nulo.");
        }

        if (builder.getType() == null || builder.getFlow() == null) {
            throw new IllegalArgumentException("Os tipos de exportação e fluxo no builder não podem ser nulos.");
        }

        ExportType exportType = ExportType.valueOf(builder.getType());
        FlowType flowType = FlowType.valueOf(builder.getFlow());
        List<T> dataList = flow.produce(flowType);

        IExport<T> exportStrategy = exportFactory.getStrategy(exportType);
        exportStrategy.export((T) headerUseCase.prepareHeader(), dataList);
    }
}
