
package com.example.datafilestrategieexporter.application.factories;

import com.example.datafilestrategieexporter.adapters.export.CsvExportStrategy;
import com.example.datafilestrategieexporter.adapters.export.ExcelExportStrategy;
import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.interfaces.ExportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ExportStrategyFactory {

    private final Map<ExportType, ExportStrategy> strategies = new EnumMap<>(ExportType.class);

    @Autowired
    public ExportStrategyFactory(ExportStrategy excelExportStrategy, ExportStrategy csvExportStrategy) {
        strategies.put(ExportType.EXCEL, excelExportStrategy);
        strategies.put(ExportType.CSV, csvExportStrategy);
    }

    public ExportStrategy getStrategy(ExportType type) {
        if (!strategies.containsKey(type)) {
            throw new IllegalArgumentException("Invalid export type: " + type);
        }
        return strategies.get(type);
    }
}
