
package com.example.writerdatatofile.usecases;

import com.example.writerdatatofile.application.usecases.ExportBuilder;

public class ExportCommandValidator {
    public static void validate(ExportBuilder command) {
        if (command.getType() == null) {
            throw new IllegalArgumentException("Export type cannot be null");
        }
        if (command.getResponse() == null) {
            throw new IllegalArgumentException("HttpServletResponse cannot be null");
        }
    }
}
