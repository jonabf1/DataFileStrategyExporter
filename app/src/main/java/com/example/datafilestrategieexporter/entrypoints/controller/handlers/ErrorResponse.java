package com.example.datafilestrategieexporter.entrypoints.controller.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int status; // Código HTTP
    private String message; // Mensagem de erro
    private String details; // Detalhes adicionais

}