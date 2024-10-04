
package com.example.datafilestrategieexporter.usecases;

import com.example.datafilestrategieexporter.domain.entities.ExportData;

import java.util.List;

public interface ExportDataUseCase {
    List<ExportData> prepareExportData();
}
    