
package com.example.datafilestrategieexporter.application.factories;

import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import com.example.datafilestrategieexporter.domain.interfaces.IExportFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ExportStrategyFactory implements IExportFactory {

    private final Map<ExportType, IExport> strategies = new EnumMap<>(ExportType.class);

    public ExportStrategyFactory(IExport excelExportStrategy, IExport csvExportStrategy) {
        strategies.put(ExportType.EXCEL, excelExportStrategy);
        strategies.put(ExportType.CSV, csvExportStrategy);
    }

    public IExport getStrategy(ExportType type) {
        if (!strategies.containsKey(type)) {
            throw new IllegalArgumentException("Invalid export type: " + type);
        }
        return strategies.get(type);
    }

}
