
package com.example.datafilestrategieexporter.usecases;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExportBuilderDto {

    private String type;
    private String flow;

}
