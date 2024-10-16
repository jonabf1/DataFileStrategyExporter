
package com.example.datafilestrategieexporter.application.factories;

import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import com.example.datafilestrategieexporter.domain.interfaces.IExportFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ExportStrategyFactory<T> implements IExportFactory<T> {

    private final Map<ExportType, IExport<T>> strategies = new EnumMap<>(ExportType.class);

    public ExportStrategyFactory(
            @Qualifier("csvExportService") IExport<T> csvExportService,
            @Qualifier("excelExportService") IExport<T> excelExportService) {
        strategies.put(ExportType.EXCEL, excelExportService);
        strategies.put(ExportType.CSV, csvExportService);
    }

    public IExport<T> getStrategy(ExportType type) {
        if (!strategies.containsKey(type)) {
            throw new IllegalArgumentException("Invalid export type: " + type);
        }
        return strategies.get(type);
    }

}
