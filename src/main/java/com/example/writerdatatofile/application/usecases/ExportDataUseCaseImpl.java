
package com.example.writerdatatofile.usecases;

import com.example.writerdatatofile.domain.entities.ExportData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExportDataUseCaseImpl implements ExportDataUseCase {

    @Override
    public List<ExportData> prepareExportData() {
        // Simulando a preparação dos dados para exportação
        List<ExportData> dataList = new ArrayList<>();
        dataList.add(new ExportData(1, "John Doe", 25, "jonDoe@gmail.com"));
        dataList.add(new ExportData(2, "Jane Smith", 30, null));
        dataList.add(new ExportData(3, "Peter Johnson", 28, "peter@outlook.com"));
        return dataList;
    }
}
    