
package com.example.datafilestrategieexporter.usecases.entityOne;

import com.example.datafilestrategieexporter.domain.entities.EntityOneData;
import com.example.datafilestrategieexporter.domain.interfaces.EntityOneDataUseCase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntityOneDataUseCaseImpl implements EntityOneDataUseCase {

    @Override
    public List<EntityOneData> prepareExportData() {
        // Simulando a preparação dos dados para exportação
        List<EntityOneData> dataList = new ArrayList<>();
        dataList.add(new EntityOneData(1, "John Doe", 25, null));
        dataList.add(new EntityOneData(2, "Jane Smith", 30, null));
        dataList.add(new EntityOneData(3, "Peter Johnson", 28, null));
        return dataList;
    }
}
    