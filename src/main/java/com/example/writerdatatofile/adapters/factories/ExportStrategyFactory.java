
package com.example.writerdatatofile.adapters.factories;

import com.example.writerdatatofile.adapters.strategies.CsvExportStrategy;
import com.example.writerdatatofile.adapters.strategies.ExcelExportStrategy;
import com.example.writerdatatofile.core.interfaces.ExportStrategy;
import com.example.writerdatatofile.domain.enums.ExportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ExportStrategyFactory {

    private final Map<ExportType, ExportStrategy> strategies = new EnumMap<>(ExportType.class);

    @Autowired
    public ExportStrategyFactory(ExcelExportStrategy excelExportStrategy, CsvExportStrategy csvExportStrategy) {
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
