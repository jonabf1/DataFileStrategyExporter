
package com.example.datafilestrategieexporter.usecases;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletResponse;

@Getter
@Setter
@Builder
public class ExportBuilder {

    private HttpServletResponse response;
    private String type;

}
