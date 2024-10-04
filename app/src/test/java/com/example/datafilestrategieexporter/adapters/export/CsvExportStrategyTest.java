
package com.example.datafilestrategieexporter.adapters.export;

import com.example.datafilestrategieexporter.domain.entities.ExportData;
import com.example.datafilestrategieexporter.usecases.ExportDataUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class CsvExportStrategyTest {

    @Mock
    private ExportDataUseCase exportDataUseCase;

    private CsvExportStrategy csvExportStrategy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        csvExportStrategy = new CsvExportStrategy(exportDataUseCase);
    }

    @Test
    public void testExport_ShouldExportCsvWithCorrectData() throws IOException {
        // Preparação de dados simulados
        List<ExportData> dataList = Arrays.asList(
                new ExportData(1, "John Doe", 25, "john.doe@example.com"),
                new ExportData(2, "Jane Smith", 30, "jane.smith@example.com"),
                new ExportData(3, "Peter Johnson", 28, "peter.johnson@example.com")
        );
        
        // Configurando o mock para o método prepareExportData()
        when(exportDataUseCase.prepareExportData()).thenReturn(dataList);

        // Executando o método export
        csvExportStrategy.export(null);

        // Verificando se o método foi chamado corretamente
        verify(exportDataUseCase, times(1)).prepareExportData();
    }
}
