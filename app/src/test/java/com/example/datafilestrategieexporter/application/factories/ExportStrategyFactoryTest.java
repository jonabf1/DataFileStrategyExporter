package com.example.datafilestrategieexporter.application.factories;

import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.interfaces.ExportStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class ExportStrategyFactoryTest {

    @Mock
    private ExportStrategy csvExportStrategy;

    @Mock
    private ExportStrategy excelExportStrategy;

    private ExportStrategyFactory exportStrategyFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        exportStrategyFactory = new ExportStrategyFactory(excelExportStrategy, csvExportStrategy);
    }

    @Test
    public void testCreateStrategy_ShouldReturnCsvStrategy() {
        ExportStrategy strategy = exportStrategyFactory.getStrategy(ExportType.CSV);
        assert(strategy == csvExportStrategy);
    }

    @Test
    public void testCreateStrategy_ShouldReturnExcelStrategy() {
        ExportStrategy strategy = exportStrategyFactory.getStrategy(ExportType.EXCEL);
        assert(strategy == excelExportStrategy);
    }

    @Test
    public void testCreateStrategy_ShouldThrowExceptionForUnsupportedType() {
        assertThrows(IllegalArgumentException.class, () -> {
            exportStrategyFactory.getStrategy(null);
        });
    }

}
