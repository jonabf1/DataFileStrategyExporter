
package com.example.datafilestrategieexporter.domain.interfaces;

import com.example.datafilestrategieexporter.domain.entities.EntityTwoData;

import java.util.List;

public interface EntityTwoDataUseCase {
    List<EntityTwoData> prepareExportData();
}
    