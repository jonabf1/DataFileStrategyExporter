
package com.example.datafilestrategieexporter.usecases.entityTwo;

import com.example.datafilestrategieexporter.domain.entities.EntityTwoData;
import com.example.datafilestrategieexporter.domain.interfaces.EntityTwoDataUseCase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntityTwoDataUseCaseImpl implements EntityTwoDataUseCase {

    @Override
    public List<EntityTwoData> prepareExportData() {
        // Simulando a preparação dos dados para exportação
        List<EntityTwoData> dataList = new ArrayList<>();
        dataList.add(new EntityTwoData(1, "Ipsum Doe", 12, "-2321391231"));
        dataList.add(new EntityTwoData(3, "Lorem Smith", 5, null));
        dataList.add(new EntityTwoData(4, "DueLoremIpsum Johnson", 7, null));
        return dataList;
    }
}
    