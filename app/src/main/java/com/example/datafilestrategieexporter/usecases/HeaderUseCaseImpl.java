
package com.example.datafilestrategieexporter.usecases;

import com.example.datafilestrategieexporter.domain.entities.Header;
import com.example.datafilestrategieexporter.domain.interfaces.HeaderUseCase;
import org.springframework.stereotype.Service;

@Service
public class HeaderUseCaseImpl implements HeaderUseCase {

    @Override
    public Header prepareHeader() {
        // Simulando a preparação do dado para exportacao
        Header header = new Header(1, "Header 1", 3);
        return header;
    }

}
    