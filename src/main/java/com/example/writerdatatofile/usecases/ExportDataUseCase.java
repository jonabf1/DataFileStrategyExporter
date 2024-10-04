
package com.example.writerdatatofile.usecases;

import com.example.writerdatatofile.domain.entities.ExportData;

import java.util.List;

public interface ExportDataUseCase {
    List<ExportData> prepareExportData();
}
    