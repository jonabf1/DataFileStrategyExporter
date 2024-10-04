
package com.example.datafilestrategieexporter.domain.interfaces;


import com.example.datafilestrategieexporter.usecases.ExportBuilder;

import java.io.IOException;

public interface ExportStrategy {
    void export(ExportBuilder exportBuilder) throws IOException;
}
