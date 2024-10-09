
package com.example.datafilestrategieexporter.domain.interfaces;

import java.io.IOException;
import java.util.List;

public interface IExport<T> {
    void export(T header, List<T> data) throws IOException;
}
