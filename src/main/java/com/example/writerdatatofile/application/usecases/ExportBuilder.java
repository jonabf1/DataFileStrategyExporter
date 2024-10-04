
package com.example.writerdatatofile.application.usecases;

import javax.servlet.http.HttpServletResponse;

public class ExportBuilder {

    private HttpServletResponse response;
    private String type;

    // Construtor privado - só pode ser chamado pelo Builder
    private ExportBuilder(Builder builder) {
        this.response = builder.response;
        this.type = builder.type;
    }

    // Getters
    public HttpServletResponse getResponse() {
        return response;
    }

    public String getType() {
        return type;
    }

    // Classe Builder estática
    public static class Builder {
        private HttpServletResponse response;
        private String type;

        // Método para definir o atributo `response`
        public Builder withResponse(HttpServletResponse response) {
            this.response = response;
            return this; // Retorna o Builder para permitir encadeamento
        }

        // Método para definir o atributo `type`
        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        // Método para construir o `ExportCommand`
        public ExportBuilder build() {
            return new ExportBuilder(this);
        }
    }
}
