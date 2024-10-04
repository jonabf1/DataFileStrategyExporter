
package com.example.writerdatatofile.frameworks.spring.configuration;

import com.example.writerdatatofile.adapters.strategies.CsvExportStrategy;
import com.example.writerdatatofile.adapters.strategies.ExcelExportStrategy;
import com.example.writerdatatofile.core.interfaces.ExportStrategy;
import com.example.writerdatatofile.domain.enums.ExportType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class ExportStrategyConfig {

    @Bean
    public Map<ExportType, ExportStrategy> exportStrategies(ExcelExportStrategy excelExportStrategy, CsvExportStrategy csvExportStrategy) {
        Map<ExportType, ExportStrategy> strategies = new EnumMap<>(ExportType.class);
        strategies.put(ExportType.EXCEL, excelExportStrategy);
        strategies.put(ExportType.CSV, csvExportStrategy);
        return strategies;
    }
}
