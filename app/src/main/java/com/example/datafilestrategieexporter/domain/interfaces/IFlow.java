
package com.example.datafilestrategieexporter.domain.interfaces;

import com.example.datafilestrategieexporter.domain.enums.FlowType;

import java.io.IOException;
import java.util.List;

public interface IFlow<T> {
    List<T> produce(FlowType flowType) throws IOException;
}
