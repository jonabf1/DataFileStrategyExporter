
package com.example.datafilestrategieexporter.domain.interfaces;

import com.example.datafilestrategieexporter.domain.enums.ExportType;

import java.io.IOException;

public interface IExportFactory<T> {
    IExport getStrategy(ExportType exportType) throws IOException;
}
