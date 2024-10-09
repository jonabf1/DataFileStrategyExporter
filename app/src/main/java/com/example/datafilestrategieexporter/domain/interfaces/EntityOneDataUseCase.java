
package com.example.datafilestrategieexporter.domain.interfaces;

import com.example.datafilestrategieexporter.domain.entities.EntityOneData;

import java.util.List;

public interface EntityOneDataUseCase {
    List<EntityOneData> prepareExportData();
}
    