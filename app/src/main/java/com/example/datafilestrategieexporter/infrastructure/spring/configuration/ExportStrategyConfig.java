
package com.example.datafilestrategieexporter.infrastructure.spring.configuration;

import com.example.datafilestrategieexporter.adapters.export.CsvExportService;
import com.example.datafilestrategieexporter.adapters.export.ExcelExportService;
import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.interfaces.IExport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class ExportStrategyConfig<T> {

    @Bean
    public Map<ExportType, IExport<T>> exportStrategies(ExcelExportService<T> excelExportServiceStrategy, CsvExportService<T> csvExportServiceStrategy) {
        Map<ExportType, IExport<T>> strategies = new EnumMap<>(ExportType.class);
        strategies.put(ExportType.EXCEL, excelExportServiceStrategy);
        strategies.put(ExportType.CSV, csvExportServiceStrategy);
        return strategies;
    }
}
