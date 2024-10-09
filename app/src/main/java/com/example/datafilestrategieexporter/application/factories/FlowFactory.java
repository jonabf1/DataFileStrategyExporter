package com.example.datafilestrategieexporter.application.factories;

import com.example.datafilestrategieexporter.domain.enums.FlowType;
import com.example.datafilestrategieexporter.domain.interfaces.EntityOneDataUseCase;
import com.example.datafilestrategieexporter.domain.interfaces.EntityTwoDataUseCase;
import com.example.datafilestrategieexporter.domain.interfaces.HeaderUseCase;
import com.example.datafilestrategieexporter.domain.interfaces.IFlow;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlowFactory<T> implements IFlow<T> {

    private final EntityOneDataUseCase entityOneDataUseCase;
    private final EntityTwoDataUseCase entityTwoDataUseCase;
    private final HeaderUseCase headerUseCase;

    public FlowFactory(EntityOneDataUseCase entityOneDataUseCase, HeaderUseCase headerUseCase, EntityTwoDataUseCase entityTwoDataUseCase) {
        this.entityOneDataUseCase = entityOneDataUseCase;
        this.entityTwoDataUseCase = entityTwoDataUseCase;
        this.headerUseCase = headerUseCase;
    }

    @Override
    public List<T> produce(FlowType flow) {
        return switch (flow) {
            case A -> (List<T>) entityOneDataUseCase.prepareExportData();
            case B -> (List<T>) entityTwoDataUseCase.prepareExportData();
            default -> throw new IllegalArgumentException("Tipo de fluxo desconhecido: " + flow);
        };
    }
}
