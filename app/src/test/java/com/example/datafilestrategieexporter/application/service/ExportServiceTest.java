package com.example.datafilestrategieexporter.application.service;

import com.example.datafilestrategieexporter.application.factories.ExportStrategyFactory;
import com.example.datafilestrategieexporter.domain.enums.ExportType;
import com.example.datafilestrategieexporter.domain.interfaces.ExportStrategy;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExportServiceTest {

    @Mock
    private ExportStrategyFactory exportStrategyFactory;

    @Mock
    private ExportStrategy csvExportStrategy;

    @Mock
    private ExportStrategy excelExportStrategy;

    private ExportService exportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        exportService = new ExportService(exportStrategyFactory);

        // Configurar a fábrica para retornar estratégias simuladas
        when(exportStrategyFactory.getStrategy(ExportType.CSV)).thenReturn(csvExportStrategy);
        when(exportStrategyFactory.getStrategy(ExportType.EXCEL)).thenReturn(excelExportStrategy);
    }

    @Test
    public void testExecuteExportCommandWithCsv_ShouldUseCsvStrategy() throws IOException {
        // Configurar o exportBuilder com tipo CSV usando o Builder Pattern
        ExportBuilder exportBuilder = new ExportBuilder.Builder()
                .withType(ExportType.CSV.name()) // Definir o tipo como CSV
                .build();

        // Executar o método de exportação
        exportService.executeExportCommand(exportBuilder);

        // Verificar se a estratégia CSV foi chamada
        verify(csvExportStrategy, times(1)).export(any(ExportBuilder.class));
        verify(excelExportStrategy, never()).export(any(ExportBuilder.class));
    }

    @Test
    public void testExecuteExportCommandWithExcel_ShouldUseExcelStrategy() throws IOException {
        // Configurar o exportBuilder com tipo EXCEL usando o Builder Pattern
        ExportBuilder exportBuilder = new ExportBuilder.Builder()
                .withType(ExportType.EXCEL.name()) // Definir o tipo como EXCEL
                .build();

        // Executar o método de exportação
        exportService.executeExportCommand(exportBuilder);

        // Verificar se a estratégia EXCEL foi chamada
        verify(excelExportStrategy, times(1)).export(any(ExportBuilder.class));
        verify(csvExportStrategy, never()).export(any(ExportBuilder.class));
    }

    @Test
    public void testExecuteExportCommandWithNullType_ShouldThrowException() {
        // Configurar o exportBuilder com tipo nulo usando o Builder Pattern
        ExportBuilder exportBuilder = new ExportBuilder.Builder()
                .withType(null) // Definir o tipo como nulo
                .build();

        // Verificar se a exceção IllegalArgumentException é lançada ao executar o comando
        Exception exception = assertThrows(IllegalArgumentException.class, () -> exportService.executeExportCommand(exportBuilder));
        assertEquals("O tipo de exportação no command não pode ser nulo.", exception.getMessage());
    }

}
