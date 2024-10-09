
package com.example.datafilestrategieexporter.entrypoints.controller;

import com.example.datafilestrategieexporter.application.service.ExportService;
import com.example.datafilestrategieexporter.usecases.ExportBuilderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/download")
    public ResponseEntity downloadExport(@RequestParam("type") String type, @RequestParam("flow") String flow) throws IOException {
        ExportBuilderDto builder = ExportBuilderDto.builder()
                .type(type.toUpperCase())
                .flow(flow.toUpperCase())
                .build();

        exportService.executeExportBuilder(builder);

        return ResponseEntity.ok().build();
    }

}
