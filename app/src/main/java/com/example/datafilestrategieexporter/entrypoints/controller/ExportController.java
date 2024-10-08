
package com.example.datafilestrategieexporter.entrypoints.controller;

import com.example.datafilestrategieexporter.application.service.ExportService;
import com.example.datafilestrategieexporter.usecases.ExportBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/download")
    public void downloadExport(@RequestParam("type") String type, HttpServletResponse response) throws IOException {
        ExportBuilder builder = ExportBuilder.builder()
                .response(response)
                .type(type)
                .build();

        exportService.executeExportBuilder(builder);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
